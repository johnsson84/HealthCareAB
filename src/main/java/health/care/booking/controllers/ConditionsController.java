package health.care.booking.controllers;
import health.care.booking.dto.CreateCondition;
import health.care.booking.models.Conditions;
import health.care.booking.respository.ConditionsRepository;
import health.care.booking.services.ConditionsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conditions")
public class ConditionsController {

    private final ConditionsService conditionsService;
    private final ConditionsRepository conditionsRepository;

    public ConditionsController(ConditionsService conditionsService, ConditionsRepository conditionsRepository) {
        this.conditionsService = conditionsService;
        this.conditionsRepository = conditionsRepository;
    }

    // Get all feedback
    @GetMapping("/all")
    public ResponseEntity<?> getAllConditions() {
        return ResponseEntity.ok(conditionsRepository.findAll());
    }

    // Create a condition
    @PostMapping("/add")
    public ResponseEntity<?> createCondition(@RequestBody CreateCondition createCondition) {
        try {
            return ResponseEntity.ok(conditionsService.createCondition(createCondition));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Delete a condition
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCondition(@PathVariable String id) {
        try {
            return ResponseEntity.ok(conditionsService.deleteCondition(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
