package health.care.booking.models;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "facility")
public class Facility {

  @Id
  private String id;

  @NotNull
  private String facilityName;
  @NotNull
  private String phoneNumber;
  @Email
  @NotNull
  private String email;
  @NotNull
  private String hoursOpen;
  private List<String> coworkersId;
  @NotNull
  private FacilityAddress facilityAddress;

  // CONSTRUCTOR
  public Facility() {}

  // GETTERS
  public String getFacilityName() {
    return facilityName;
  }
  public String getPhoneNumber() {
    return phoneNumber;
  }
  public String getHoursOpen() {
    return hoursOpen;
  }
  public String getId() {
    return id;
  }
  public List<String> getCoworkersId() {
    return coworkersId;
  }
  public FacilityAddress getAddress() {
    return facilityAddress;
  }

  // SETTERS
  public void setFacilityName(String facilityName) {
    this.facilityName = facilityName;
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
  public void setHoursOpen(String hoursOpen) {
    this.hoursOpen = hoursOpen;
  }
  public void setCoworkers(List<String> coworkersId) {
    this.coworkersId = coworkersId;
  }
  public void setAddress(FacilityAddress facilityAddress) {
    this.facilityAddress = facilityAddress;
  }
}
