package health.care.booking;

import health.care.booking.models.TokenPasswordReset;
import health.care.booking.models.User;
import health.care.booking.respository.TokenPasswordResetRepository;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.PasswordResetService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestorePasswordTest {

    private PasswordResetService passwordResetService;
    private JavaMailSender mailSender;
    private TokenPasswordResetRepository tokenRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        tokenRepository = mock(TokenPasswordResetRepository.class);
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        passwordResetService = new PasswordResetService(mailSender, tokenRepository, userRepository, passwordEncoder);
    }

    @Test
    void updatePassword_shouldUpdatePasswordAndDeleteToken() {
        String token = "validToken";
        String newPassword = "newSecurePassword";
        String encodedPassword = "encodedPassword";

        TokenPasswordReset resetToken = new TokenPasswordReset();
        resetToken.setToken(token);
        resetToken.setEmail("test@example.com");
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));

        User user = new User();
        user.setMail("test@example.com");

        // Mock repository behavior
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(resetToken));
        when(userRepository.findByMail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        // Act
        passwordResetService.updatePassword(token, newPassword);

        // Verify password updated
        assertEquals(encodedPassword, user.getPassword());
        verify(userRepository, times(1)).save(user);
        // Verify token deleted
        verify(tokenRepository, times(1)).delete(resetToken);
    }

    @Test
    void validateToken_shouldReturnFalseForInvalidToken() {
        String token = "invalidToken";

        // Mock repository behavior
        when(tokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // Act
        boolean isValid = passwordResetService.validateToken(token);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void updatePassword_shouldThrowExceptionForExpiredToken() {
        String token = "expiredToken";
        String newPassword = "newPassword";

        TokenPasswordReset expiredToken = new TokenPasswordReset();
        expiredToken.setToken(token);
        expiredToken.setExpiryDate(LocalDateTime.now().minusMinutes(1)); // Token expired

        // Mock repository behavior
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(expiredToken));

        // Assert
        assertThrows(RuntimeException.class, () -> passwordResetService.updatePassword(token, newPassword));
    }
}
