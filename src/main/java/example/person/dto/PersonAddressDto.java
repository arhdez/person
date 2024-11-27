package example.person.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import example.person.validation.CreateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PersonAddressDto {

    @JsonProperty("person_id")
    @NotNull(groups = CreateGroup.class)
    private UUID personId;

    @JsonProperty("address_id")
    @NotNull(groups = CreateGroup.class)
    private UUID addressId;

    @JsonProperty("street")
    @NotBlank(groups = CreateGroup.class)
    @NotNull(groups = CreateGroup.class)
    private String street;

    @JsonProperty("city")
    @NotBlank(groups = CreateGroup.class)
    @NotNull(groups = CreateGroup.class)
    private String city;

    @JsonProperty("state")
    @NotBlank(groups = CreateGroup.class)
    @NotNull(groups = CreateGroup.class)
    private String state;

    @JsonProperty("zip_code")
    @NotNull(groups = CreateGroup.class)
    private Integer zipCode;

}
