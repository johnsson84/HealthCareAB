package health.care.booking;

import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.TokenPasswordResetRepository;
import health.care.booking.respository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BookingApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(BookingApplicationTests.class);


	@MockBean
	private TokenPasswordResetRepository tokenPasswordResetRepository;

	@MockBean
	private AvailabilityRepository availabilityRepository;

	@MockBean
	private UserRepository userRepository;


	@Test
	void contextLoads() {
		logger.info("Active profiles: {}", System.getProperty("spring.profiles.active"));
		// Add any additional logging if needed
	}

}
