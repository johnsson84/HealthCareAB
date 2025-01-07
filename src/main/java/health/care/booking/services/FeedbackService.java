package health.care.booking.services;

import health.care.booking.dto.FeedbackDTO;
import health.care.booking.models.Feedback;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.FeedbackRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final AppointmentRepository appointmentRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, AppointmentRepository appointmentRepository) {
        this.feedbackRepository = feedbackRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // Create a feedback
    public Feedback addFeedback(FeedbackDTO feedbackDTO) throws Exception {
        appointmentRepository.findById(feedbackDTO.getAppointmentId())
                .orElseThrow(() -> new Exception("Appointment not found!"));

        Feedback newFeedback = new Feedback();
        newFeedback.setAppointmentId(feedbackDTO.getAppointmentId());
        newFeedback.setCaregiverId(Optional.ofNullable(feedbackDTO.getCaregiverId()).orElse(""));
        newFeedback.setComment(Optional.ofNullable(feedbackDTO.getComment()).orElse(""));
        newFeedback.setRating(Optional.of(feedbackDTO.getRating()).orElse(3));
        feedbackRepository.save(newFeedback);
        return newFeedback;
    }

    // Delete a feedback
    public ResponseEntity<?> deleteFeedback(String feedbackId) throws Exception {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new Exception("Feedback does not exist, can't delete..."));
        feedbackRepository.delete(feedback);
        return ResponseEntity.ok("Feedback deleted successfully");
    }

}
