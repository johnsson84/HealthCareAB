package health.care.booking.services;

import health.care.booking.models.Appointment;
import health.care.booking.models.Status;
import health.care.booking.models.User;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    AvailabilityRepository availabilityRepository;

    public Appointment createNewAppointment(String patientId, String summary, String caregiverId, @NotNull Date availabilityDate) {
        Appointment newAppointment = new Appointment();
        newAppointment.setSummary(summary);
        newAppointment.setPatientId(setPatient(patientId));
        newAppointment.setCaregiverId(setCaregiver(caregiverId));
        newAppointment.setDateTime(availabilityDate);
        newAppointment.setStatus(Status.SCHEDULED);
        return newAppointment;
    }

    public String setPatient(String patientId) {
        User patient = userRepository.findByUsername(patientId)
                .orElseThrow(() -> new RuntimeException("No user found with that Id."));
        return patient.getId();
    }

    public String setCaregiver(String caregiverId) {
        User caregiver = userRepository.findById(caregiverId)
                .orElseThrow(() -> new RuntimeException("No user found with that Id."));
        return caregiver.getId();
    }


    public Status returnStatus(String status) {
        if (status.toUpperCase().equals(Status.CANCELLED.name())) {
            return Status.CANCELLED;
        } else if (status.toUpperCase().equals(Status.COMPLETED.name())) {
            return Status.COMPLETED;
        } else if (status.toUpperCase().equals(Status.SCHEDULED.name())) {
            return Status.SCHEDULED;
        } else {
            return Status.ERROR;
        }
    }

    public ResponseEntity<?> getAppointmentWithNames(Appointment appointment) {

        User foundPatient = userRepository.findById(appointment.getPatientId()).orElseThrow(() ->
                new RuntimeException("User not found")
        );
        User foundCaregiver = userRepository.findById(appointment.getCaregiverId()).orElseThrow(() ->
                new RuntimeException("User not found")
        );

        Map<String, Object> appointmentInfo = new HashMap<>();
        appointmentInfo.put("caregiverUsername", foundCaregiver.getUsername());
        appointmentInfo.put("patientFirstName", foundPatient.getFirstName());
        appointmentInfo.put("patientLastName", foundPatient.getLastName());
        appointmentInfo.put("userEmail", foundPatient.getMail());
        appointmentInfo.put("summary", appointment.getSummary());
        appointmentInfo.put("dateTime", appointment.getDateTime());
        appointmentInfo.put("status", appointment.getStatus());

        return ResponseEntity.ok(appointmentInfo);
    }




}
