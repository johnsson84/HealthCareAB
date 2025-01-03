package health.care.booking;

import health.care.booking.respository.TokenPasswordResetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


@SpringBootTest
class PasswordResetServiceTests {

    @MockBean
    private TokenPasswordResetRepository tokenRepository;

    @Test
    void testContextLoads() {
        // Ett test för att säkerställa att applikationen startar
    }
}