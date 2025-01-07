package health.care.booking.models;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "appointment")
public class Appointment {
    @Id
    private String id;

    @DBRef
    private User patientId;
    
    @DBRef
    private User caregiverId;

    // datum och tid, vill ni så kan ni ändra till något annat
    // tex ett fält för datum ett för tid det är upp till er
    private @NotNull Date dateTime;

    // använder Enum Status klassen
    private Status status;

    public Appointment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getPatientId() {
        return patientId;
    }

    public void setPatientId(User patientId) {
        this.patientId = patientId;
    }

    public User getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(User caregiverId) {
        this.caregiverId = caregiverId;
    }

    public @NotNull Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(@NotNull Date dateTime) {
        this.dateTime = dateTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
