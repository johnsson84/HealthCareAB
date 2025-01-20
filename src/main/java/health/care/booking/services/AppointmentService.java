package health.care.booking.services;

import health.care.booking.dto.AppointmentAddDocumentation;
import health.care.booking.dto.AppointmentRequest;
import health.care.booking.models.Appointment;
import health.care.booking.models.Availability;
import health.care.booking.models.Status;
import health.care.booking.models.User;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Autowired
    AvailabilityService availabilityService;

    @Autowired
    MailService mailService;

    public Appointment createNewAppointment(String patientId, String reason, String caregiverId, @NotNull Date availabilityDate) {
        Appointment newAppointment = new Appointment();
        newAppointment.setReason(reason);
        newAppointment.setPatientId(setPatient(patientId));
        newAppointment.setCaregiverId(setCaregiver(caregiverId));
        newAppointment.setDateTime(availabilityDate);
        newAppointment.setStatus(Status.SCHEDULED);
        return newAppointment;
    }

    public void createNewAppointmentLogic(AppointmentRequest appointmentRequest){
        // should probably have a "this is a valid timeslot type deal"
        Appointment newAppointment = createNewAppointment(appointmentRequest.username, appointmentRequest.reason, appointmentRequest.caregiverId, appointmentRequest.availabilityDate);
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
        mailService.sendEmail(userRepository.findByUsername(appointmentRequest.username).get().getMail(), "Appointment", "Appointment has been booked for: " + appointmentRequest.availabilityDate + "\n" + "Reason for booking: " + appointmentRequest.reason);
        appointmentRepository.save(newAppointment);
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
        appointmentInfo.put("reason", appointment.getReason());
        appointmentInfo.put("dateTime", appointment.getDateTime());
        appointmentInfo.put("status", appointment.getStatus());

        return ResponseEntity.ok(appointmentInfo);
    }


    public List<Appointment> getCompletedUserAppointments(String userId){
        List<Appointment> userAppointments = appointmentRepository.findAppointmentByPatientId(userId);

        // Filter to keep only scheduled appointments
        List<Appointment> historyAppointments = userAppointments.stream()
                .filter(appointment -> Status.SCHEDULED.equals(appointment.getStatus()))
                .filter(appointment -> {
                    LocalDateTime appointmentDateTime = appointment.getDateTime().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();

                    return appointmentDateTime.isAfter(LocalDateTime.now());
                })
                .collect(Collectors.toList());

        return historyAppointments;
    }
    public List<Appointment> getCompletedDoctorAppointments(String caregiverId) {
        List<Appointment> userAppointments = appointmentRepository.findByCaregiverId(caregiverId);

        // Filter to keep only scheduled appointments
        List<Appointment> historyAppointments = userAppointments.stream()
                .filter(appointment -> Status.SCHEDULED.equals(appointment.getStatus()))
                .filter(appointment -> {
                    LocalDateTime appointmentDateTime = appointment.getDateTime().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();

                    return appointmentDateTime.isAfter(LocalDateTime.now());
                })
                .collect(Collectors.toList());

        return historyAppointments;
    }
    public ResponseEntity<?> getAppointmentHistoryFromUsernamePatient(String userId) {

        List<Appointment> userAppointments = appointmentRepository.findAppointmentByPatientId(userId);

        // Filter to keep only completed appointments and sort by date (newest first)
        List<Appointment> historyAppointments = userAppointments.stream()
                .filter(appointment -> Status.COMPLETED.equals(appointment.getStatus()))
                .sorted((appointment1, appointment2) -> appointment2.getDateTime().compareTo(appointment1.getDateTime()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(historyAppointments);
    }

    public ResponseEntity<?> getAppointmentHistoryFromUsernameCaregiver(String userId) {

        List<Appointment> userAppointments = appointmentRepository.findAppointmentByCaregiverId(userId);

        // Filter to keep only completed appointments and sort by date (newest first)
        List<Appointment> historyAppointments = userAppointments.stream()
                .filter(appointment -> Status.COMPLETED.equals(appointment.getStatus()))
                .sorted((appointment1, appointment2) -> appointment2.getDateTime().compareTo(appointment1.getDateTime()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(historyAppointments);
    }

    public String changeAppointmentStatusService(String status, String appointmentId) {

        Appointment changingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Could not find appointment."));

        changingAppointment.setStatus(returnStatus(status));
        switch (status.toUpperCase()) {
            case "CANCELLED": {
                changingAppointment.setStatus(Status.CANCELLED);
                handleCancelledAppointment(changingAppointment);
                break;
            }
            case "COMPLETED": {
                changingAppointment.setStatus(Status.COMPLETED);
                break;
            }
            default: {
                changingAppointment.setStatus(Status.ERROR);
                break;
            }
        }
        if (changingAppointment.getStatus().equals(Status.ERROR)) {
            return "Something went wrong with the status." ;
        }
        appointmentRepository.save(changingAppointment);
        return changingAppointment.getStatus().name();
    }

    public void handleCancelledAppointment(Appointment cancelledAppointment) {

        // Fetch availability where the time slot does NOT exist
        Availability updatedAvailability = availabilityRepository
                .findByCaregiverIdAndAvailableSlotsNotContaining(
                        cancelledAppointment.getCaregiverId(),
                        cancelledAppointment.getDateTime()
                )
                .orElseThrow(() -> new RuntimeException(
                        String.format("Could not find availability for caregiverId: %s at time: %s",
                                cancelledAppointment.getCaregiverId(),
                                cancelledAppointment.getDateTime())));

        // Add the cancelled appointment time back to availableSlots
        List<Date> cancelledAppointmentDates = new ArrayList<>();
        cancelledAppointmentDates.add(cancelledAppointment.getDateTime());
        availabilityService.addAvailabilityByArray(cancelledAppointmentDates, updatedAvailability.getId());
    }

    public Appointment addDocumentationToAppointment(AppointmentAddDocumentation dto) {
        // Find appointment
        Appointment appointmentToAddDokument = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Could not find appointment."));
        // Check if appointment already has documentation given
        if (!appointmentToAddDokument.getDocumentation().isEmpty()) {
            throw  new IllegalArgumentException("Documentation already exists!");
        }
        // Create updated appointment
        appointmentToAddDokument.setDocumentation(dto.getDocumentation());
        // Save new appointment
        appointmentRepository.save(appointmentToAddDokument);
        return appointmentToAddDokument;
    }


}
