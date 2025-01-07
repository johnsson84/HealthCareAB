package health.care.booking;

import health.care.booking.dto.FeedbackDTO;
import health.care.booking.models.Appointment;
import health.care.booking.models.Feedback;
import health.care.booking.models.Status;
import health.care.booking.models.User;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.FeedbackRepository;
import health.care.booking.services.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FeedbackTests {
    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    private User patient = new User();
    private User doctor = new User();
    private Appointment appointment = new Appointment();
    private Feedback savedFeedback = new Feedback();
    private FeedbackDTO feedbackDTO = new FeedbackDTO();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // ARRANGE
        // Setup a user
        patient.setId("1");
        patient.setUsername("feedbackUser");
        patient.setPassword("Feedback123");
        patient.setFirstName("Feedback");
        patient.setLastName("Feedbacksson");
        patient.setMail("feedback@feedback.com");

        // Setup a doctor
        doctor.setId("2");
        doctor.setUsername("doctorUser");
        doctor.setPassword("Doctor123");
        doctor.setFirstName("Doctor");
        doctor.setLastName("Doctorsson");
        doctor.setMail("doctor@feedback.com");

        // Setup a appointment
        appointment.setId("3");
        appointment.setPatientId(patient.getId());
        appointment.setCaregiverId(doctor.getId());
        String appointmentTime = "2025-01-02T09:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(appointmentTime, formatter);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        appointment.setDateTime(date);
        appointment.setStatus(Status.COMPLETED);
    }

    // Used in multiple tests
    public void arrangeFeedback() {
        savedFeedback = new Feedback();
        savedFeedback.setId("4");
        savedFeedback.setAppointmentId(appointment.getId());
        savedFeedback.setCaregiverId(doctor.getId());
        savedFeedback.setPatientUsername("test");
        savedFeedback.setComment("comment");
        savedFeedback.setRating(4);

        feedbackDTO = new FeedbackDTO();
        feedbackDTO.setAppointmentId("3");
        feedbackDTO.setCaregiverId("2");
        feedbackDTO.setPatientUsername("test");
        feedbackDTO.setComment(savedFeedback.getComment());
        feedbackDTO.setRating(savedFeedback.getRating());
    }

    @Test
    public void addFeedbackToAnAppointment() throws Exception {

        // Arrange
        arrangeFeedback();

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(savedFeedback);
        when(appointmentRepository.findById(any())).thenReturn(Optional.ofNullable(appointment));

        // Act
        Feedback feedback = feedbackService.addFeedback(feedbackDTO);

        // Assert
        assertEquals("3", feedback.getAppointmentId(), "appointmentId not saved");
        assertEquals("2", feedback.getCaregiverId(), "caregiverId not saved");
        assertEquals("comment", feedback.getComment(), "comment not saved");
        assertEquals(4, feedback.getRating(), "rating not saved");
        System.out.println("Success! Feedback added.");
    }

    @Test
    public void cantAddFeedbackToAppointmentThatDoesNotExist() throws Exception {

        // Arrange
        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setAppointmentId("56");

        when(appointmentRepository.findById(any())).thenReturn(Optional.empty());

        // Act
        Exception exception = assertThrows(Exception.class, () -> {
            feedbackService.addFeedback(feedbackDTO);
        });

        // Assert
        assertEquals("Appointment not found!", exception.getMessage(), "Failed! Appointment exists...");
        System.out.println("Success! Appointment not found.");

    }

    @Test
    public void cantAddFeedbackTwice() throws Exception {
        // Arrange
        arrangeFeedback();

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(savedFeedback);
        when(appointmentRepository.findById(any())).thenReturn(Optional.ofNullable(appointment));
        when(feedbackRepository.findAllByCaregiverId(any())).thenReturn(List.of(savedFeedback));

        // Act
        Exception exception = assertThrows(Exception.class, () -> {
            feedbackService.addFeedback(feedbackDTO);
        });

        // Assert
        assertEquals("Feedback already given!", exception.getMessage(), "Failed! Feedback wasn't given twice...");
        System.out.println("Success! Cant add feedback twice.");
    }

    @Test
    public void failIfAppointmentIsNotCompleted() throws Exception {
        // Arrange
        arrangeFeedback();
        appointment.setStatus(Status.SCHEDULED);

        when(appointmentRepository.findById(any())).thenReturn(Optional.ofNullable(appointment));
        when(feedbackRepository.findAllByCaregiverId(any())).thenReturn(List.of(savedFeedback));

        // Act
        Exception exception = assertThrows(Exception.class, () -> {
            feedbackService.addFeedback(feedbackDTO);
        });

        // Assert
        assertEquals("Appointment status is not set to COMPLETED", exception.getMessage(), "Failed! Appointment status is set to COMPLETED");
        System.out.println("Success! Failed to create feedback because appointment status is not COMPLETED.");
    }
}
