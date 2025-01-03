package health.care.booking;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
class BookingApplicationTests {

	@MockBean
	private JavaMailSender mailSender;


	@Test
	void contextLoads() {

	}

}
