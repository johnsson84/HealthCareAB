package health.care.booking.controllers;

import health.care.booking.dto.AppointmentAddDocumentation;
import health.care.booking.dto.AppointmentRequest;
import health.care.booking.models.Appointment;
import health.care.booking.models.Availability;
import health.care.booking.models.Status;
import health.care.booking.models.User;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.AppointmentService;
import health.care.booking.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private UserService userService;


    @PostMapping("/new")
    public ResponseEntity<?> createNewAppointment(@Valid @RequestBody AppointmentRequest appointmentRequest) {
            appointmentService.createNewAppointmentLogic(appointmentRequest);
        return ResponseEntity.ok("Appointment has been made.");
    }

    @GetMapping("/get/{username}")
    public List<Appointment> getUsersAppointments(@Valid @PathVariable String username) {
        String userId = userRepository.findByUsername(username).get().getId();
        return appointmentRepository.findAppointmentByPatientId(userId);

    }
    @GetMapping("/get/scheduled/user/{username}")
    public List<Appointment> getCompletedUsersAppointments(@Valid @PathVariable String username) {
        String userId = userRepository.findByUsername(username).get().getId();
        return appointmentService.getCompletedUserAppointments(userId);

    }
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    @GetMapping("/get/scheduled/caregiver/{username}")
    public List<Appointment> getCompletedDoctorAppointments(@Valid @PathVariable String username) {
        String caregiverId = userRepository.findByUsername(username).get().getId();
        return appointmentService.getCompletedDoctorAppointments(caregiverId);
    }

    @PreAuthorize("hasAnyRole('DOCTOR', 'USER')")
    @PostMapping("/change-status/{status}/{appointmentId}")
    public ResponseEntity<?> changeAppointmentStatus(@Valid @PathVariable String status, @PathVariable String appointmentId) {
       String appointmentStatus =  appointmentService.changeAppointmentStatusService(status, appointmentId);
        return ResponseEntity.ok("The status has been changed to: " + appointmentStatus);
    }


    @GetMapping("/info/{appointmentId}")
    public Appointment getAppointmentInfo(@Valid @PathVariable String appointmentId) {
        Appointment foundAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Could not find appointment."));

        return foundAppointment;
    }

    @GetMapping("/info/no-id/{appointmentId}")
    public ResponseEntity<?> getAppointmentInfoWithNames(@Valid @PathVariable String appointmentId) {
        Appointment foundAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Could not find appointment."));

        return ResponseEntity.ok(appointmentService.getAppointmentWithNames(foundAppointment));
    }

    @GetMapping("/history/1/{username}")
    public ResponseEntity<?> getAppointmentHistoryFromUsernamePatient(@Valid @PathVariable String username) {
        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("No user found with that username: " + username));
            return appointmentService.getAppointmentHistoryFromUsernamePatient(user.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not get any appointment history from username: " + username + " " + e.getMessage());
        }
    }
    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/history/2/{username}")
    public ResponseEntity<?> getAppointmentHistoryFromUsernameCaregiver(@Valid @PathVariable String username) {
        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("No user found with that username: " + username));
            return appointmentService.getAppointmentHistoryFromUsernameCaregiver(user.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not get any appointment history from username: " + username + " " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/documentation/add")
    public ResponseEntity<?> addDocumentationToAppointment(@Valid @RequestBody AppointmentAddDocumentation dto) {
        try {
            appointmentService.addDocumentationToAppointment(dto);
            return ResponseEntity.ok("Appointment updated with documentation successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
