package health.care.booking.services;

import org.springframework.stereotype.Service;

import health.care.booking.dto.FeedbackDTO;
import health.care.booking.models.Feedback;
import health.care.booking.respository.FeedbackRepository;
import health.care.booking.respository.UserRepository;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
    }

    // Create a feedback
    public Feedback addFeedback(FeedbackDTO feedbackDTO) {
    }

}
