package health.care.booking.dto;

public class UserResponse {

    private String usernameDto;
    private String emailDto;
    private String firstNameDto;
    private String lastNameDto;

    public String getUsernameDto() {
        return usernameDto;
    }

    public void setUsernameDto(String usernameDto) {
        this.usernameDto = usernameDto;
    }

    public String getLastNameDto() {
        return lastNameDto;
    }

    public void setLastNameDto(String lastNameDto) {
        this.lastNameDto = lastNameDto;
    }

    public String getFirstNameDto() {
        return firstNameDto;
    }

    public void setFirstNameDto(String firstNameDto) {
        this.firstNameDto = firstNameDto;
    }

    public String getEmailDto() {
        return emailDto;
    }

    public void setEmailDto(String emailDto) {
        this.emailDto = emailDto;
    }
}
