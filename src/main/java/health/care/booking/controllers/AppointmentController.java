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
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/new")
    public ResponseEntity<?> createNewAppointment(@Valid @RequestBody AppointmentRequest appointmentRequest){
        // should probably have a "this is a valid timeslot type deal"

        appointmentService.removeAvailabilitySlots(appointmentRequest.availabilityId, appointmentRequest.availabilityDate);
        Appointment newAppointment = new Appointment();
        newAppointment.setPatientId(appointmentService.setPatient(appointmentRequest.username));
        newAppointment.setCaregiverId(appointmentService.setPatient(appointmentRequest.caregiverId));
        newAppointment.setDateTime(appointmentRequest.availabilityDate);
        newAppointment.setStatus(Status.SCHEDULED);
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
