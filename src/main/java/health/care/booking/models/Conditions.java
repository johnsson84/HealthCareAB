package health.care.booking.models;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "conditions")
public class Conditions {
    // VARIABLES
    @Id
    private String id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private ConditionsCategory category;

    // CONSTRUCTOR
    public Conditions() {}

    // GETTERS & SETTERS

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ConditionsCategory getCategory() {
        return category;
    }

    public void setCategory(ConditionsCategory category) {
        this.category = category;
    }
}
