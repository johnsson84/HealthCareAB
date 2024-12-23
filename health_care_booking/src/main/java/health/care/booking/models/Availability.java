package health.care.booking.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "availability")
public class Availability {
    @Id
    private String id;

    // en doktor/sjuksköterska sätter sig available
    @DBRef
    private User caregiverId;

    // en lista med tider som är tillgängliga
    // ni kan ändra implementaionen om ni hittar ett enklare sätt
    private List<LocalDateTime> availableSlots;

    public Availability() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(User caregiverId) {
        this.caregiverId = caregiverId;
    }

    public List<LocalDateTime> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(List<LocalDateTime> availableSlots) {
        this.availableSlots = availableSlots;
    }
}
