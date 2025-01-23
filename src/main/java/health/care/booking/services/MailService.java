package health.care.booking.services;

import health.care.booking.models.Appointment;
import health.care.booking.models.Status;
import health.care.booking.respository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailService {
    @Autowired
    private final JavaMailSender mailSender;
    @Autowired
    private AppointmentRepository appointmentRepository;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String toEmail, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        mailSender.send(mailMessage);
    }

    public void sendEmailAppointment(String toEmail, String subject, String text, String time, String date, String appointmentReason) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText("Your appointment on: " + date + " at the time of: " + time + ". concerning: " + appointmentReason + " has a pending message: " + text);
        mailSender.send(mailMessage);
    }

    public void sendEmailReminder(String toEmail, String dateTime, String appointmentReason){
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setTo(toEmail);
    mailMessage.setSubject("Appointment reminder");
    mailMessage.setText("This is a reminder about your appointment on today: " + dateTime + ". Concerning: " + appointmentReason + ". Make sure to be on time and have a good day.");
    mailSender.send(mailMessage);
    }

    public void sendEmailRequest(String toEmail, String appointmentReason, String time, String date, String firstName) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Mail request from: " + firstName);
        mailMessage.setText("You have requested an email concerning: " + appointmentReason + ". your booked appointment is on " + date + " time: " + time + ". Please write below why you requested this mail:");
        mailSender.send(mailMessage);
    }
}
