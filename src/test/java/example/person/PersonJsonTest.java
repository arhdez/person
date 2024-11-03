package example.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.person.jpa.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class PersonJsonTest {
    @Autowired
    private JacksonTester<Person> json;
   /* @Autowired
    private JacksonTester<Person[]> jsonList;

    private Person[] persons;*/

    /*@BeforeEach
    void setUp() {
        persons = Arrays.array(
                new Person("99", "John", "Doe"),
                new Person("100", "Alex", "Rod"),
                new Person("101", "Al", "Alx"));
    }*/
    @BeforeEach
    void setUp(){
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    /*@Test
    void personSerializationTest() throws IOException {
        Person person = persons[0];
        assertThat(json.write(person)).isStrictlyEqualToJson("single.json");
        assertThat(json.write(person)).hasJsonPathStringValue("@.id");
        assertThat(json.write(person)).extractingJsonPathStringValue("@.id")
                .isEqualTo("99");
        assertThat(json.write(person)).hasJsonPathStringValue("@.firstName");
        assertThat(json.write(person)).extractingJsonPathStringValue("@.firstName")
                .isEqualTo("John");
        assertThat(json.write(person)).hasJsonPathStringValue("@.lastName");
        assertThat(json.write(person)).extractingJsonPathStringValue("@.lastName")
                .isEqualTo("Doe");
    }*/
    @Test
    public void testSerializePersonToJson() throws IOException {
        // Arrange: Create a UUID and a sample Person object
        byte[] ssn = {1,2,3,4,5,6,7,8,9};
        UUID personId = UUID.randomUUID();
        Person person = new Person(personId, "John", "Doe", ssn);

        // Act: Serialize the Person object to JSON
        String expectedJson = "{\"id\":\"" + personId.toString() + "\", \"firstName\":\"John\", \"lastName\":\"Doe\", \"ssn\":\"123456789\"}";

        // Assert: Check if the serialized JSON matches the expected JSON
        assertThat(json.write(person)).isEqualToJson(expectedJson);
    }

    @Test
    public void testDeserializeJsonToPerson() throws IOException {
        // Arrange: Create a sample JSON string for a Person
        UUID personId = UUID.randomUUID();
        String personJson = "{\"id\":\"" + personId + "\", \"firstName\":\"John\", \"lastName\":\"Doe\", \"ssn\":\"123456789\"}";
        // Act: Deserialize the JSON string to a Person object
        Person person = json.parseObject(personJson);
        // Assert: Verify that the fields match
        assertThat(person.getId()).isEqualTo(personId);
        assertThat(person.getFirstName()).isEqualTo("John");
        assertThat(person.getLastName()).isEqualTo("Doe");
        assertThat(person.getSsn()).isEqualTo("123456789");
    }

   /* @Test
    void personDeserializationTest() throws IOException {
        String expected = """
                {
                    "id": 99,
                    "firstName": "John",
                    "lastName": "Doe"
                }
                """;
        assertThat(json.parse(expected))
                .isEqualTo(new Person("99", "John", "Doe"));
        assertThat(json.parseObject(expected).getId()).isEqualTo("99");
        assertThat(json.parseObject(expected).getFirstName()).isEqualTo("John");
        assertThat(json.parseObject(expected).getLastName()).isEqualTo("Doe");
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
                    {"id": "99", "firstName": "John" , "lastName": "Doe"},
                    {"id": "100", "firstName": "Alex" , "lastName": "Rod"},
                    {"id": "101", "firstName": "Al" , "lastName": "Alx"}
                ]
                """;
        assertThat(jsonList.parse(expected)).isEqualTo(persons);
    }*/

}
