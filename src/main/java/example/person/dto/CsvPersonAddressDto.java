package example.person.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CsvPersonAddressDto {

    @JsonProperty("person_id")
    private UUID personId;

    @JsonProperty("address_id")
    private UUID addressId;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    @JsonProperty("address")
    private String fullAddress;
}
