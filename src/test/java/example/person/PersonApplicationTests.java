package example.person;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)  // Import the test security configuration to bypass security auth
class PersonApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldReturnACashCardWhenDataIsSaved() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/persons/c8a802a8-0017-41c0-91dc-e5dfca4aaaa4", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String id = documentContext.read("$.id");
        assertThat(id).isEqualTo("c8a802a8-0017-41c0-91dc-e5dfca4aaaa4");

        String firstName = documentContext.read("$.firstName");
        assertThat(firstName).isEqualTo("John");

        String lastName = documentContext.read("$.lastName");
        assertThat(lastName).isEqualTo("Doe");
    }
    @Test
    void shouldCreateAPerson(){
        Person newPerson = new Person(null, "John", "Doe");
        ResponseEntity<Void> createResponse = restTemplate
                .postForEntity("/persons", newPerson, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

}
