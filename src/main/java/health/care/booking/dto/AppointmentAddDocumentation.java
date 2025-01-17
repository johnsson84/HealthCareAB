package health.care.booking.dto;

public class AppointmentAddDocumentation {
    // VARIABLES
    private String appointmentId;
    private String documentation;

    // GETTERS SETTERS
    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }
}
