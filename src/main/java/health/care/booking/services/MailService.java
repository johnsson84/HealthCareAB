package health.care.booking.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        System.out.println(mailMessage);
        mailSender.send(mailMessage);
    }

    public void sendEmailRequest(String toEmail, String appointmentSummary) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Mail Request: " + appointmentSummary);
        mailMessage.setText("You have requested an email, answer to this email!");
        System.out.println(mailMessage);
        mailSender.send(mailMessage);
    }
}
