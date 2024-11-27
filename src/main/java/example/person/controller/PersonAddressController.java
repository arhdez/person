package example.person.controller;

import example.person.config.SecurityConfig;
import example.person.dto.PersonAddressDto;
import example.person.service.PersonAddressService;
import example.person.validation.CreateGroup;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/personaddresses")
@Import(SecurityConfig.class)
public class PersonAddressController {
    private final PersonAddressService personAddressService;

    public PersonAddressController(PersonAddressService personAddressService) {
        this.personAddressService = personAddressService;
    }

    @PostMapping
    public ResponseEntity<PersonAddressDto> createPersonAddress(@Validated(CreateGroup.class) @RequestBody PersonAddressDto newPersonAddress) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personAddressService.createPersonAddress(newPersonAddress));
    }
}
