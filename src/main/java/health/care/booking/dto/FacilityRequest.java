package health.care.booking.dto;

import health.care.booking.models.FacilityAddress;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

public class FacilityRequest {
    @DBRef
    private String facilityId;
    private String facilityName;
    private String phoneNumber;
    private String email;
    private String hoursOpen;
    private FacilityAddress facilityAddress;

    private List<String> coworkersId;

    public FacilityRequest(String facilityName, String phoneNumber, String email, String hoursOpen, FacilityAddress facilityAddress) {
        this.facilityName = facilityName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.hoursOpen = hoursOpen;
        this.facilityAddress = facilityAddress;
    }


    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public FacilityAddress getFacilityAddress() {
        return (FacilityAddress) facilityAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHoursOpen() {
        return hoursOpen;
    }

    public void setHoursOpen(String hoursOpen) {
        this.hoursOpen = hoursOpen;
    }

    public List<String> getCoworkersId() {
        return coworkersId;
    }

    public void setCoworkersId(List<String> coworkersId) {
        this.coworkersId = coworkersId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }
}
