package health.care.booking.dto;

import health.care.booking.models.FacilityAddress;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

public class FacilityRequest {
    @DBRef
    private String facilityId;
    private String facilityName;
    private final FacilityAddress facilityAddress; // this is not from a DTO so it is the full FacilityAdress object.
    private String hoursOpen;
    private String phoneNumber;
    private String email;

    // Reviewer: why is this a list of CoworkerRequests? could either just be Id's To coworkers or could be DbRefs
    // John: Håller inte med dig i detta tyvärr.
    // You don't have to have it here if you have it in the Model
    private List<String> coworkersId; // this is now my object list of coworkers (this is not full User Objects)


    public FacilityRequest(String facilityName, String hoursOpen, String email, FacilityAddress facilityAddress, String phoneNumber) {
        this.facilityName = facilityName;
        this.hoursOpen = hoursOpen;
        this.email = email;
        this.facilityAddress = facilityAddress;
        this.phoneNumber = phoneNumber;
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
