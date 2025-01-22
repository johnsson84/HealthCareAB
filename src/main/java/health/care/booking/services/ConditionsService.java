package health.care.booking.services;

import health.care.booking.dto.CreateCondition;
import health.care.booking.models.Conditions;
import health.care.booking.models.ConditionsCategory;
import health.care.booking.respository.ConditionsRepository;
import org.springframework.stereotype.Service;

@Service
public class ConditionsService {

    private final ConditionsRepository conditionsRepository;

    public ConditionsService(ConditionsRepository conditionsRepository) {
        this.conditionsRepository = conditionsRepository;
    }

    // Create condition
    public Conditions createCondition(CreateCondition createCondition) {
        Conditions condition = new Conditions();
        condition.setName(createCondition.getName());
        condition.setDescription(createCondition.getDescription());
        switch (createCondition.getCategory().toUpperCase()) {
            case "DISEASES":
                condition.setCategory(ConditionsCategory.DISEASES);
                break;
            case "DISORDERS":
                condition.setCategory(ConditionsCategory.DISORDERS);
                break;
            case "INJURIES":
                condition.setCategory(ConditionsCategory.INJURIES);
                break;
            case "ALLERGIES":
                condition.setCategory(ConditionsCategory.ALLERGIES);
                break;
            default:
                throw new IllegalArgumentException("Invalid category: " + createCondition.getCategory());
        }
        conditionsRepository.save(condition);
        return condition;
    }

    // Delete a condition
    public String deleteCondition(String id) throws Exception {
        Conditions condition = conditionsRepository.findById(id)
                .orElseThrow(() -> new Exception("Condition not found"));
        conditionsRepository.delete(condition);
        return "Condition deleted!";
    }
}
