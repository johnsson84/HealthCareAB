package health.care.booking.controllers;

import health.care.booking.dto.AppointmentRequest;
import health.care.booking.models.Appointment;
import health.care.booking.models.Availability;
import health.care.booking.models.Status;
import health.care.booking.models.User;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    //a
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private AvailabilityRepository availabilityRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/new")
    public ResponseEntity<?> createNewAppointment(@Valid @RequestBody AppointmentRequest appointmentRequest) {
        // should probably have a "this is a valid timeslot type deal"
        Appointment newAppointment = appointmentService.createNewAppointment(appointmentRequest.username, appointmentRequest.caregiverId, appointmentRequest.availabilityDate);
        Availability removeAvailability = availabilityRepository.findById(appointmentRequest.availabilityId)
                .orElseThrow(() -> new RuntimeException("Could not find availability object."));
        System.out.println(appointmentRequest.availabilityDate);
        for (int i = 0; i < removeAvailability.getAvailableSlots().size(); i++) {
            if (removeAvailability.getAvailableSlots().get(i).equals(appointmentRequest.availabilityDate)) {
                System.out.println("Should remove: " + removeAvailability.getAvailableSlots().get(i).toString());
                removeAvailability.getAvailableSlots().remove(i);
                System.out.println("removed: " + appointmentRequest.availabilityDate);
                availabilityRepository.save(removeAvailability);
            }
        }
        appointmentRepository.save(newAppointment);
        return ResponseEntity.ok("Appointment has been made.");
    }

    @GetMapping("/get/{username}")
    public List<Appointment> getUsersAppointments(@Valid @PathVariable String username) {
        String userId = userRepository.findByUsername(username).get().getId();
        return appointmentRepository.findAppointmentByPatientId(userId);
    }

    @PostMapping("/change-status/{status}/{appointmentId}")
    public ResponseEntity<?> changeAppointmentStatus(@Valid @PathVariable String status, @PathVariable String appointmentId) {

        Appointment changingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Could not find appointment."));

        changingAppointment.setStatus(appointmentService.returnStatus(status));

        if (changingAppointment.getStatus().equals(Status.ERROR)) {
            return ResponseEntity.status(401).body("Something went wrong with the status.");
        }

        appointmentRepository.save(changingAppointment);

        return ResponseEntity.ok("The appointment has been changed to " + changingAppointment.getStatus().name());
    }
}
