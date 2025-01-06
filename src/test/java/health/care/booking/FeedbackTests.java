package health.care.booking;

import health.care.booking.dto.FeedbackDTO;
import health.care.booking.models.Appointment;
import health.care.booking.models.Feedback;
import health.care.booking.models.Status;
import health.care.booking.models.User;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.FeedbackRepository;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.AppointmentService;
import health.care.booking.services.FeedbackService;
import health.care.booking.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FeedbackTests {
    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private FeedbackService feedbackService;
    @InjectMocks
    private UserService userService;
    @InjectMocks
    private AppointmentService appointmentService;

    private User patient = new User();
    private User doctor = new User();
    private Appointment appointment = new Appointment();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

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
        appointment.setPatientId(patient);
        appointment.setCaregiverId(doctor);
        String appointmentTime = "2025-01-02T09:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(appointmentTime, formatter);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        appointment.setDateTime(date);
        appointment.setStatus(Status.SCHEDULED);

    }

    @Test
    public void shouldGiveAppointmentFeedback() throws Exception {

        // Arrange
        Feedback savedFeedback = new Feedback();
        savedFeedback.setId("4");
        savedFeedback.setAppointmentId(appointment);
        savedFeedback.setCaregiverId("2");
        savedFeedback.setComment("comment");
        savedFeedback.setRating(4);

        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setAppointmentId(appointment.getId());
        feedbackDTO.setCaregiverId(doctor.getId());
        feedbackDTO.setComment(savedFeedback.getComment());
        feedbackDTO.setRating(savedFeedback.getRating());

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(savedFeedback);
        when(appointmentRepository.findById(any())).thenReturn(Optional.ofNullable(appointment));

        // Act
        Feedback feedback = feedbackService.addFeedback(feedbackDTO);

        // Assert
        assertEquals(appointment, feedback.getAppointmentId(), "appointmentId not saved");
        assertEquals("2", feedback.getCaregiverId(), "caregiverId not saved");
        assertEquals("comment", feedback.getComment(), "comment not saved");
        assertEquals(4, feedback.getRating(), "rating not saved");
    }
}
