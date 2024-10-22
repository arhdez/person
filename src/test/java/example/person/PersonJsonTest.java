package example.person;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class PersonJsonTest {
    @Autowired
    private JacksonTester<Person> json;
    @Autowired
    private JacksonTester<Person[]> jsonList;

    private Person[] persons;

    @BeforeEach
    void setUp() {
        persons = Arrays.array(
                new Person(99L, "John", "Doe"),
                new Person(100L, "Alex", "Rod"),
                new Person(101L, "Al", "Alx"));
    }

    @Test
    void personSerializationTest() throws IOException {
        Person person = persons[0];
        assertThat(json.write(person)).isStrictlyEqualToJson("single.json");
        assertThat(json.write(person)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(person)).extractingJsonPathNumberValue("@.id")
                .isEqualTo(99);
        assertThat(json.write(person)).hasJsonPathStringValue("@.firstName");
        assertThat(json.write(person)).extractingJsonPathStringValue("@.firstName")
                .isEqualTo("John");
        assertThat(json.write(person)).hasJsonPathStringValue("@.lastName");
        assertThat(json.write(person)).extractingJsonPathStringValue("@.lastName")
                .isEqualTo("Doe");
    }

    @Test
    void personDeserializationTest() throws IOException {
        String expected = """
                {
                    "id": 99,
                    "firstName": "John",
                    "lastName": "Doe"
                }
                """;
        assertThat(json.parse(expected))
                .isEqualTo(new Person(99L, "John", "Doe"));
        assertThat(json.parseObject(expected).id()).isEqualTo(99L);
        assertThat(json.parseObject(expected).firstName()).isEqualTo("John");
        assertThat(json.parseObject(expected).lastName()).isEqualTo("Doe");
    }

    @Test
    void personListSerializationTest() throws IOException {
        assertThat(jsonList.write(persons)).isStrictlyEqualToJson("list.json");
    }

    @Test
    void
    personListDeserializationTest() throws IOException {
        String expected = """
                [
                    {"id": 99, "firstName": "John" , "lastName": "Doe"},
                    {"id": 100, "firstName": "Alex" , "lastName": "Rod"},
                    {"id": 101, "firstName": "Al" , "lastName": "Alx"}
                ]
                """;
        assertThat(jsonList.parse(expected)).isEqualTo(persons);
    }

}
