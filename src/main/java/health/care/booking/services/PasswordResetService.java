package health.care.booking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordResetService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetLink(String email) {
        // Generate a secure token (e.g., UUID)
        String token = UUID.randomUUID().toString();

        // Store token (e.g., in a database)
        saveTokenToDatabase(email, token);

        // Send email
        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        sendEmail(email, resetLink);
    }

    private void saveTokenToDatabase(String email, String token) {
        // TODO: Save the token with an expiration time linked to the email in the database
    }

    private void sendEmail(String toEmail, String resetLink) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("Click the link to reset your password: " + resetLink);
        mailSender.send(mailMessage);
    }
}
