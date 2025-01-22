package health.care.booking.controllers;

import health.care.booking.dto.SendMail;
import health.care.booking.models.TokenPasswordReset;
import health.care.booking.services.MailService;
import health.care.booking.services.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private MailService mailService;
    @Autowired
    private ScheduleService scheduleService;

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
}
