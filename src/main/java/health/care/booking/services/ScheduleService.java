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


}


