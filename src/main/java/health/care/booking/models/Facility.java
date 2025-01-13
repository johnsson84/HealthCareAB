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

  private double location;

  private int phoneNumber;

  private String email;

  private String hoursOpen;

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

  public double getLocation() {
    return location;
  }

  public void setLocation(double location) {
    this.location = location;
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

}
