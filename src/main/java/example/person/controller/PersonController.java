package example.person.controller;

import example.person.config.SecurityConfig;
import example.person.dto.PersonDto;
import example.person.service.PersonService;
import example.person.validation.CreateGroup;
import example.person.validation.PatchGroup;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/persons")
@Import(SecurityConfig.class)//Import security bypass
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {this.personService = personService;}

    @PostMapping
    public ResponseEntity<PersonDto> createPerson(@Validated(CreateGroup.class) @RequestBody PersonDto newPersonRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.createPerson(newPersonRequest));
    }

    @GetMapping("/id/{requestedId}")
    public ResponseEntity<PersonDto> findById(@PathVariable UUID requestedId) {
        Optional<PersonDto> person = personService.findById(requestedId);
        return person.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/firstname/{requestedFirstName}")
    public ResponseEntity<List<PersonDto>> findByFirstName(@PathVariable String requestedFirstName, Pageable pageable) {
        return ResponseEntity.ok(personService.findByFirstName(requestedFirstName, pageable));
    }

    @GetMapping
    public ResponseEntity<List<PersonDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(personService.findAll(pageable));
    }

    @PutMapping("/{requestedId}")
    public ResponseEntity<PersonDto> putPerson(@PathVariable UUID requestedId, @Validated(CreateGroup.class) @RequestBody PersonDto personUpdate) {
        Optional<PersonDto> person = personService.updatePerson(personUpdate, requestedId);
        return person.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{requestedId}")
    public ResponseEntity<PersonDto> patchPerson(@PathVariable UUID requestedId, @Validated(PatchGroup.class) @RequestBody PersonDto personFieldsToUpdate) {
        Optional<PersonDto> person = personService.updatePerson(personFieldsToUpdate, requestedId);
        return person.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable UUID id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
