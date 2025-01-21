package health.care.booking.dto;

import health.care.booking.models.FacilityAddress;
import jakarta.validation.constraints.Email;

public class UserUpdateRequest {
    @Email
    private String mail;
    private String birthDate;
    private String phoneNmr;
    private FacilityAddress address;

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setPhoneNmr(String phoneNmr) {
        this.phoneNmr = phoneNmr;
    }

    public void setAddress(FacilityAddress address) {
        this.address = address;
    }

    public UserUpdateRequest() {
    }

    public String getMail() {
        return mail;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPhoneNmr() {
        return phoneNmr;
    }

    public FacilityAddress getAddress() {
        return address;
    }
}
