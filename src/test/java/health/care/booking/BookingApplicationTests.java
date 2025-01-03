package health.care.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BookingApplicationTests {



	private static final Logger logger = LoggerFactory.getLogger(BookingApplicationTests.class);

	@Test
	void contextLoads() {
		logger.info("Active profiles: {}", System.getProperty("spring.profiles.active"));
		// Add any additional logging if needed
	}

}
