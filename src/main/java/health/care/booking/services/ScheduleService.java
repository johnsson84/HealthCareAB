package health.care.booking.services;

import health.care.booking.models.Appointment;
import health.care.booking.models.User;
import health.care.booking.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    MailService mailService;
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    UserRepository userRepository;

    @Scheduled(cron = "0 00 7 * * ?") // "Sec, Min, Hour"
    public void sendDailyReminders() {
        List<Appointment> appointmentList = appointmentService.getScheduledAppointmentsThatAreToday();
        for (Appointment appointment : appointmentList) {
                   User user = userRepository.findById(appointment.getPatientId()).orElseThrow(() -> new RuntimeException("No user found with id: " + appointment.getPatientId()));
                   mailService.sendEmailReminder(user.getMail(), appointment.getDateTime().toString(), appointment.getReason());
        }
        System.out.println(appointmentList.size() + " Big list");
    }
}


