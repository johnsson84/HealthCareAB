package health.care.booking;

import health.care.booking.models.TokenPasswordReset;
import health.care.booking.respository.TokenPasswordResetRepository;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.MailService;
import health.care.booking.services.PasswordResetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PasswordResetServiceTest {

    // Mockade beroenden för att simulera mailService tjänster och lösenords kryptering.
    @Mock
    private MailService mailService;
    @Mock
    private PasswordEncoder passwordEncoder;

    // Mockade beroenden för att simulera repository samt andra tjänster
    @Mock
    private TokenPasswordResetRepository tokenPasswordResetRepository;
    @Mock
    private UserRepository userRepository;

    // här injicerar jag dom mockade beroendena i passwordResetService
    @InjectMocks
    private PasswordResetService passwordResetService;

    // här initierar jag mock objekten innan varje test körs.
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testSendPasswordResetLink_success() {

        // Arrange
        String token = UUID.randomUUID().toString(); // Skapar en unik token
        String email = "johnmessoa@gmail.com"; // Detta blir E-posten dit återställningslänken skickas till.

        // Mockar beteende för att ta bort den unika token som är kopplad till E-postadressen.
        doNothing().when(tokenPasswordResetRepository).deleteByMail(email);

        // Mockar beteende för att kunna spara en ny genererad token.
        when(tokenPasswordResetRepository.save(any())).thenAnswer(InvocationOnMock::getArguments);

        // Act
        passwordResetService.sendPasswordResetLink(email); // Här anropar jag metoden som testas

        // Assert
        verify(tokenPasswordResetRepository, times(1)).deleteByMail(email); // Här verifierar jag att token raderas
        verify(tokenPasswordResetRepository, times(1)).save(any(TokenPasswordReset.class)); // verifierar att en ny token sparas
        verify(mailService, times(1)).sendEmail(
                eq(email),
                eq("Password Reset Request"),
                contains("http://localhost:5173/resetPassword")); // Verifierar att en korrekt länk skickas med i mejlet.
    }

    @Test
    public void testSendPasswordResetLink_failure() {

        // Arrange
        String email = "emailfinnsinte@gmail.com"; // här kör jag istället en E-post som inte finns i systemet

        // Mockar beteende för att kasta fel/undantag när en token ska raderas
        doThrow(new RuntimeException("Email was not found")).when(tokenPasswordResetRepository).deleteByMail(email);

        // Act & Assert
        RuntimeException runtimeEx = assertThrows(
                RuntimeException.class,
                () -> passwordResetService.sendPasswordResetLink(email), // Försök till att skicka en reset länk.
                "Was expecting exception when email does not exist.");

        // Kollar så att meddelandet i exception är korrekt.
        assertEquals("Email was not found", runtimeEx.getMessage(), "Exception message should be a match");

        // Här verifierar jag att inga fler metoder kallas på om ett fel inträffar
        verify(tokenPasswordResetRepository, times(1)).deleteByMail(email); // kollar att deleteByMail anropas
        verify(tokenPasswordResetRepository, times(1)).save(any(TokenPasswordReset.class)); // kollar att save INTE anropas.
        verify(mailService, never()).sendEmail(any(), any(), any()); // Kollar så att inget mejl har skickas
    }



}
