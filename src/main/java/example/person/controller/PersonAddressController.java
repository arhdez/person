package example.person.controller;

import example.person.config.SecurityConfig;
import example.person.dto.PersonAddressDto;
import example.person.service.PersonAddressService;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personaddresses")
@Import(SecurityConfig.class)
public class PersonAddressController {
    private final PersonAddressService personAddressService;

    public PersonAddressController(PersonAddressService personAddressService) {
        this.personAddressService = personAddressService;
    }

    @PostMapping
    public ResponseEntity<PersonAddressDto> createPersonAddress(@Validated @RequestBody PersonAddressDto newPersonAddress) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personAddressService.createPersonAddress(newPersonAddress));
    }

    @GetMapping
    public ResponseEntity<List<PersonAddressDto>> findAllPersonAddress(Pageable pageable) {
        return ResponseEntity.ok(personAddressService.findAll(pageable));
    }
}
