package example.person;

import org.springframework.data.annotation.Id;

public record Person(@Id Long id, String firstName, String lastName) {
}
