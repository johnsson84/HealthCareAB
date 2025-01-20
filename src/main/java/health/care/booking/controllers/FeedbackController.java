package health.care.booking.controllers;

import health.care.booking.dto.FeedbackAverageGradeAllResponse;
import health.care.booking.dto.FeedbackDTO;
import health.care.booking.models.Feedback;
import health.care.booking.respository.FeedbackRepository;
import health.care.booking.services.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    }

    // Get all feedback
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllFeedback() {
        return ResponseEntity.ok(feedbackRepository.findAll());
    }

    // Get all feedbacks for a doctor
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    @GetMapping("/doctor/{username}")
    public ResponseEntity<?> getDoctorFeedback(@Valid @PathVariable String username) throws Exception {
        List<Feedback> allFeedback = feedbackService.getFeedbackForDoctor(username);

        if (!allFeedback.isEmpty()) {
            return ResponseEntity.ok(allFeedback);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No feedback found...");
        }
    }

    // Get all feedbacks given from a patient
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/patient/{username}")
    public ResponseEntity<?> getPatientFeedbackGiven(@Valid @PathVariable String username) throws Exception {
        List<Feedback> allFeedback = feedbackService.getPatientFeedbackGiven(username);
        if (!allFeedback.isEmpty()) {
            return ResponseEntity.ok(allFeedback);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No feedback given...");
        }
    }

    // Add a feedback to a caregiver
    @PreAuthorize("hasRole('USER')")
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
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{feedbackId}")
    public ResponseEntity<?> deleteAnFeedback(@PathVariable String feedbackId) {
        try {
            return feedbackService.deleteFeedback(feedbackId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/find/average-grade/{username}")
    public double feedbackAverageResponseMethod(@Valid @PathVariable String username) throws Exception {
        return feedbackService.getAverageFeedbackGrade(username);
    }
    @GetMapping("/find/average-feedback/all")
    public List<FeedbackAverageGradeAllResponse> getFeedbackAverageAll() throws Exception {
        return feedbackService.getAverageFeedbackGradeAll();
    }

    // A list with all doctors feedback that is high rate (4-5)
    @GetMapping("/find/doctors-highrating")
    public ResponseEntity<?> getAllDoctorsHighRating() throws Exception {
        try {
            return ResponseEntity.ok(feedbackService.getAllFeedbackHighRating());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}
