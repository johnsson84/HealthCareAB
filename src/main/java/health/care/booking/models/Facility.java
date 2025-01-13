package health.care.booking.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;


@Document(collection = "facility")
public class Facility {

  @Id
  private int id;

  private String facilityName;

  private List<Address> addresses;

  private int phoneNumber;

  private String email;

  private String hoursOpen;

  private List<User> coworkers;

  public String getFacilityName() {
    return facilityName;
  }

  public void setFacilityName(String facilityName) {
    this.facilityName = facilityName;
  }

  public List<Address> getAddresses() {
    return addresses;
  }

  public void setAddresses(List<Address> addresses) {
    this.addresses = addresses;
  }

  public int getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(int phoneNumber) {
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



  public int getId() {
    return id;
  }

  public List<User> getCoworkers() {
    return coworkers;
  }

  public void setCoworkers(List<User> coworkers) {
    this.coworkers = coworkers;
  }
}
