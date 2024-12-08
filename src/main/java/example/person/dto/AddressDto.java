package example.person.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import example.person.validation.CreateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AddressDto {

    @JsonProperty("address_id")
    private UUID addressId;

    @JsonProperty("street")
    private String street;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("zip_code")
    private String zipCode;
}
