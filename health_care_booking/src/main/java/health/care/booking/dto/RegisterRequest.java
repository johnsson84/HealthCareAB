package health.care.booking.dto;

import health.care.booking.models.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class RegisterRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
    @NotBlank
    private String mail;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    private Set<Role> roles;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password, Set<Role> roles, String mail, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.mail = mail;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public @NotBlank String getUsername() {
        return username;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public @NotBlank String getMail() {return mail;}
    public @NotBlank String getFirstName() {return firstName;}
    public @NotBlank String getLastName() {return lastName;}

    public Set<Role> getRoles() {
        return roles;
    }

}
