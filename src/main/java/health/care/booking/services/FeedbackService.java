package health.care.booking.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import health.care.booking.dto.FeedbackDTO;
import health.care.booking.models.Appointment;
import health.care.booking.models.Feedback;
import health.care.booking.models.User;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.FeedbackRepository;
import health.care.booking.respository.UserRepository;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, AppointmentRepository appointmentRepository,
            UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    // Create a feedback
    public Feedback addFeedback(FeedbackDTO feedbackDTO) throws Exception {
        Appointment existingAppointment = appointmentRepository.findById(feedbackDTO.getAppointmentId())
                .orElseThrow(() -> new Exception("Appointment not found!"));

        Feedback newFeedback = new Feedback();
        newFeedback.setAppointmentId(existingAppointment);
        newFeedback.setComment(Optional.ofNullable(feedbackDTO.getComment()).orElse(""));
        newFeedback.setRating(Optional.ofNullable(feedbackDTO.getRating()).orElse(3));
        feedbackRepository.save(newFeedback);
        return newFeedback;
    }

}
