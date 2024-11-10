package example.person.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import example.person.validation.CreateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class PersonDto {

    private UUID id;

    @JsonProperty("first_name")
    @NotBlank(groups = CreateGroup.class)
    @NotNull(groups = CreateGroup.class)
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank(groups = CreateGroup.class)
    @NotNull(groups = CreateGroup.class)
    private String lastName;

    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{4}$", message = "SSN should be in the format XXX-XX-XXXX")
    @NotBlank(groups = CreateGroup.class)
    @NotNull(groups = CreateGroup.class)
    private String ssn;

    @JsonProperty("email")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    @NotBlank(groups = CreateGroup.class)
    @NotNull(groups = CreateGroup.class)
    private String email;

    @JsonProperty("date_of_birth")
    @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "Date of birth must be in the format yyyy-MM-dd and a valid date")
    @NotBlank(groups = CreateGroup.class)
    @NotNull(groups = CreateGroup.class)
    private String dateOfBirth;// As a String in desired format (e.g., "dd-MM-yyyy")
}
