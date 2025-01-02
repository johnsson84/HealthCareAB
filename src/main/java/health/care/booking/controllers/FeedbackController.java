package health.care.booking.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import health.care.booking.dto.FeedbackDTO;
import health.care.booking.models.Feedback;
import health.care.booking.respository.FeedbackRepository;
import health.care.booking.services.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final FeedbackRepository feedbackRepository;

    public FeedbackController(FeedbackService feedbackService, FeedbackRepository feedbackRepository) {
        this.feedbackService = feedbackService;
        this.feedbackRepository = feedbackRepository;
    };

    // Get all feedback
    @GetMapping("/all")
    public ResponseEntity<?> getAllFeedback() {
        return ResponseEntity.ok(feedbackRepository.findAll());
    }

    // Get one feedback from a caregiver

    // Add a feedback to a caregiver
    @PostMapping("/add")
    public ResponseEntity<?> addFeedback(@Valid @RequestBody FeedbackDTO feedbackDTO) {
        try {
            Feedback newFeedback = feedbackService.addFeedback(feedbackDTO);
            return ResponseEntity.ok(newFeedback);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Delete a feedback

    // Delete all feedbacks - only for DEBUG:

}
