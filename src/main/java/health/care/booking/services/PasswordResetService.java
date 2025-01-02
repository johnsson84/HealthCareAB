package health.care.booking.services;

import health.care.booking.models.TokenPasswordReset;
import health.care.booking.models.User;
import health.care.booking.respository.TokenPasswordResetRepository;
import health.care.booking.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {
    @Autowired
    private JavaMailSender mailSender;

    private final TokenPasswordResetRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(TokenPasswordResetRepository tokenRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void sendPasswordResetLink(String email) {
        // Genererar en säker token genom UUID
        String token = UUID.randomUUID().toString();
        // spara token till DB
        TokenPasswordReset resetToken = new TokenPasswordReset();
        resetToken.setToken(token);
        resetToken.setEmail(email);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        tokenRepository.save(resetToken);
        // Skickar email
        String resetLink = "http://localhost:8080/reset-password";
        sendEmail(email, resetLink);
    }

    private void sendEmail(String toEmail, String resetLink) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("Click the link to reset your password: " + resetLink);
        mailSender.send(mailMessage);
    }

    public boolean validateToken(String token) {
        // Hämtar token från DB och kollar om den är giltig eller inte
        return tokenRepository.findByToken(token)
                .map(resetToken -> !resetToken.isExpired())
                .orElse(false);
    }

    public void updatePassword(String token, String newPassword) {
        // Hämtar token från databasen
        TokenPasswordReset resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("invalid or expired token"));
        // Kollar om tokens tid har gått ut
        if (resetToken.isExpired()) {
            throw new RuntimeException("Token has expired");
        }
        // Hämtar användaren
        String mail = resetToken.getEmail();
        User user = userRepository.findByMail(mail).orElseThrow(() -> new RuntimeException("user not found"));
        // Uppdaterar användarens lösenord
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        // Raderar token efter att användaren har skapat sitt nya lösenord
        tokenRepository.delete(resetToken);
    }
}
