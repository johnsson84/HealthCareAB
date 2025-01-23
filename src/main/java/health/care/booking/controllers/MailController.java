package health.care.booking.controllers;

import health.care.booking.dto.SendMail;
import health.care.booking.models.Appointment;
import health.care.booking.models.TokenPasswordReset;
import health.care.booking.models.User;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.AppointmentService;
import health.care.booking.services.MailService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private MailService mailService;
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    UserRepository userRepository;

    @PostMapping
    public void SendMailFromClientSide(@Valid @RequestBody SendMail sendMail) {
        try {
            mailService.sendEmailAppointment(sendMail.getToEmail(), sendMail.getSubject(), sendMail.getText(), sendMail.getTime(), sendMail.getDate(), sendMail.getFirstName());
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/request")
    public void SendMailRequestFromClientSide(@Valid @RequestBody SendMail sendMail) {
        try {
            mailService.sendEmailRequest(sendMail.getToEmail(), sendMail.getAppointmentReason(), sendMail.getDate(), sendMail.getTime(), sendMail.getFirstName());
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @Scheduled(cron = "0 00 7 * * ?") // "Sec, Min, Hour"
    @PostMapping("/reminder")
    public void sendDailyReminders() {
        List<Appointment> appointmentList = appointmentService.getScheduledAppointmentsThatAreToday();
        for (Appointment appointment : appointmentList) {
            User user = userRepository.findById(appointment.getPatientId()).orElseThrow(() -> new RuntimeException("No user found with id: " + appointment.getPatientId()));
            mailService.sendEmailReminder(user.getMail(), appointment.getDateTime().toString(), appointment.getReason());
        }
        System.out.println(appointmentList.size() + " Big list");
    }
}
