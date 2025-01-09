package health.care.booking;

import health.care.booking.respository.TokenPasswordResetRepository;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.MailService;
import health.care.booking.services.PasswordResetService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordResetServiceTest {

    @Mock
    private MailService mailService;
    @Mock
    private PasswordEncoder passwordEncoder;


    @Mock
    private TokenPasswordResetRepository tokenPasswordResetRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PasswordResetService passwordResetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



}
