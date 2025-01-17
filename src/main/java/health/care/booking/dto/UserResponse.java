package health.care.booking.dto;

public class UserResponse {

    private String coworkerUsername;
    private String coworkerEmail;
    private String coworkerFirstName;
    private String coworkerLastName;
    private String coworkerRole;

    // GETTERS
    public String getCoworkerUsername() {
        return coworkerUsername;
    }
    public String getCoworkerEmail() {
        return coworkerEmail;
    }
    public String getCoworkerFirstName() {
        return coworkerFirstName;
    }
    public String getCoworkerLastName() {
        return coworkerLastName;
    }
    public String getCoworkerRole() {
        return coworkerRole;
    }

    // SETTERS
    public void setCoworkerUsername(String coworkerUsername) {
        this.coworkerUsername = coworkerUsername;
    }
    public void setCoworkerEmail(String coworkerEmail) {
        this.coworkerEmail = coworkerEmail;
    }
    public void setCoworkerFirstName(String coworkerFirstName) {
        this.coworkerFirstName = coworkerFirstName;
    }
    public void setCoworkerLastName(String coworkerLastName) {
        this.coworkerLastName = coworkerLastName;
    }
    public void setCoworkerRole(String coworkerRole) {
        this.coworkerRole = coworkerRole;
    }
}
