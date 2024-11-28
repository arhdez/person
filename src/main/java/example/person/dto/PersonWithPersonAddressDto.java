package example.person.dto;

import lombok.Data;

@Data
public class PersonWithPersonAddressDto {
    private PersonDto person;
    private PersonAddressDto personAddress;
}
