package health.care.booking.models;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "facilityAddress")
public class FacilityAddress {

  @NotNull
  private String street;
  @NotNull
  private String city;

  private int postcode;
  @NotNull
  private String region;
  @NotNull
  private String country;

  public FacilityAddress() {
  }

  public FacilityAddress(String street, String city, int postcode, String region, String country) {
    this.street = street;
    this.city = city;
    this.postcode = postcode;
    this.region = region;
    this.country = country;
  }


  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public int getPostcode() {
    return postcode;
  }

  public void setPostcode(int postcode) {
    this.postcode = postcode;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}
