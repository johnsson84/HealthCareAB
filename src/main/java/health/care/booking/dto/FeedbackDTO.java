package health.care.booking.dto;

import jakarta.validation.constraints.*;

public class FeedbackDTO {

    // Variables
    @NotBlank
    private String appointmentId;

    private String comment; // This is optional

    @Min(1)
    @Max(5)
    private int rating;

    // Constructors
    public FeedbackDTO(String appointmentId, String comment, int rating) {
        this.appointmentId = appointmentId;
        this.comment = comment;
        this.rating = rating;
    }

    public FeedbackDTO(String appointmentId, int rating) {
        this.appointmentId = appointmentId;
        this.rating = rating;
    }

    public FeedbackDTO() {
    }

    // Setters
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
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

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }
}
