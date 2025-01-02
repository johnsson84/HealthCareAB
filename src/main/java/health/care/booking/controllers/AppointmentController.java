package health.care.booking.controllers;

import health.care.booking.dto.AppointmentRequest;
import health.care.booking.models.Appointment;
import health.care.booking.models.Status;
import health.care.booking.models.User;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/new")
    public ResponseEntity<?> createNewAppointment(@Valid @RequestBody AppointmentRequest appointmentRequest){
        // should probably have a "this is a valid timeslot type deal"
        Appointment newAppointment = new Appointment();
        User patient = userRepository.findByUsername(appointmentRequest.username)
                .orElseThrow(() -> new RuntimeException("No user found with that username."));
        User caregiver = userRepository.findById(appointmentRequest.caregiverId)
                .orElseThrow(() -> new RuntimeException("No user found with that Id."));
        newAppointment.setPatientId(patient);
        newAppointment.setCaregiverId(caregiver);
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
        if (status.toUpperCase().equals(Status.CANCELLED.name())){
            changingAppointment.setStatus(Status.CANCELLED);
        } else if (status.toUpperCase().equals(Status.COMPLETED.name())) {
            changingAppointment.setStatus(Status.COMPLETED);
        }else {
            return ResponseEntity.status(403).body("Wrong type of status: " + status);
        }
        appointmentRepository.save(changingAppointment);
        return ResponseEntity.ok("The appointment has been changed to " + changingAppointment.getStatus().name());
    }
}
