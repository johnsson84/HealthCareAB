package health.care.booking.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "feedback")
public class Feedback {
    @Id
    private String id;

    // Feedback kan man endast göra på ett specifikt Appointment
    @DBRef
    private Appointment appointmentId;

    private String caregiverId; // Lättare att filtrera ut vilka feedbacks som tillhör vilken doktor.

    private String comment;

    // väldigt osäker på om det här fungerar..
    // men har lovat att hjälpa er om det inte gör det
    @Min(1)
    @Max(5)
    private int rating;

    public Feedback() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Appointment getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Appointment appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(String caregiverId) {
        this.caregiverId = caregiverId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Min(1)
    @Max(5)
    public int getRating() {
        return rating;
    }

    public void setRating(@Min(1) @Max(5) int rating) {
        this.rating = rating;
    }
}
