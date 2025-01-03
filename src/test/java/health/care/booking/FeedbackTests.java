package health.care.booking;

import health.care.booking.models.User;
import health.care.booking.respository.FeedbackRepository;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.FeedbackService;
import health.care.booking.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FeedbackTests {
    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private FeedbackService feedbackService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        // Setup a user
        User user = new User();
        user.setUsername("feedbackUser");
        user.setPassword("Feedback123");
        user.setFirstName("Feedback");
        user.setLastName("Feedbacksson");
        user.setMail("feedback@feedback.com");
        userService.registerUser(user);

        // Setup a doctor
        User doctor = new User();
        user.setUsername("doctorUser");
        user.setPassword("Doctor123");
        user.setFirstName("Doctor");
        user.setLastName("Doctorsson");
        user.setMail("doctor@feedback.com");
        userService.registerUser(doctor);

        // Setup a appointment
    }
}
