package health.care.booking.controllers;

import health.care.booking.dto.FeedbackDTO;
import health.care.booking.models.Feedback;
import health.care.booking.respository.FeedbackRepository;
import health.care.booking.services.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Get all feedbacks from a caregiver
    @GetMapping("/caregiver/{caregiverId}")
    public ResponseEntity<?> getCaregiverFeedback(@PathVariable("caregiverId") String caregiverId) {
        List<Feedback> allFeedback = feedbackRepository.findAllFeedbackByAppointmentId_CaregiverId(caregiverId);
        return ResponseEntity.ok(allFeedback);
    }

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
