package health.care.booking.dto;

import jakarta.validation.constraints.NotBlank;

public class SendMail {
    @NotBlank
    private String toEmail;

    private String subject;

    private String text;

    private String appointmentSummary;

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAppointmentSummary() {
        return appointmentSummary;
    }

    public void setAppointmentSummary(String appointmentSummary) {
        this.appointmentSummary = appointmentSummary;
    }
}
