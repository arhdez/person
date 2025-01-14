package example.person.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonAddressKafkaDto {

    @JsonProperty("person_id")
    private UUID personId;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    @JsonProperty("email")
    private String email;

    @JsonProperty("addresses")
    private List<String> addresses;

}
