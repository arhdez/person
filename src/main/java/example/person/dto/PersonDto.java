package example.person.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;


import java.util.UUID;

public class PersonDto {

    private UUID id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @Pattern(
            regexp = "^\\d{3}-\\d{2}-\\d{4}$",
            message =  "SSN should be in the format XXX-XX-XXXX"
    )
    private String ssn;

    @JsonProperty("email")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Invalid email format"
    )
    private String email;

    @JsonProperty("date_of_birth")
    @Pattern(
            regexp = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$",
            message = "Date of birth must be in the format yyyy-MM-dd and a valid date"
    )
    private String dateOfBirth;// As a String in desired format (e.g., "dd-MM-yyyy")

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
