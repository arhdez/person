package example.person;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
public class PersonController {
    @GetMapping("/{requestedId}")
    private ResponseEntity<Person> findById(@PathVariable Long requestedId){
        if (requestedId.toString().equals("99")) {
            Person person = new Person(99L, "John", "Doe");
            return ResponseEntity.ok(person);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
