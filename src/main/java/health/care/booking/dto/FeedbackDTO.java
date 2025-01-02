package health.care.booking.dto;

import jakarta.validation.constraints.*;

public class FeedbackDTO {

    // Variables
    @NotBlank
    private String appointmentId;

    @NotBlank
    private String patientId;

    private String comment; // This is optional

    @Min(1)
    @Max(5)
    private int rating;

    // Constructors
    public FeedbackDTO(String appointmentId, String patientId, String comment, int rating) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.comment = comment;
        this.rating = rating;
    }

    public FeedbackDTO(String appointmentId, String patientId, int rating) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.rating = rating;
    }

    // Setters
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    // Getters
    public String getAppointmentId() {
        return appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }
}
