package example.person;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import example.person.dto.PersonDto;
import example.person.jpa.Person;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfig.class)  // Import the test security configuration to bypass security auth
class PersonApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldReturnAPersonWhenDataIsSaved() {
        ResponseEntity<String> response = restTemplate.getForEntity("/persons/id/05896dc7-46ae-4b72-afbe-6ac63995d1f0", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String id = documentContext.read("$.id");
        assertThat(id).isEqualTo("05896dc7-46ae-4b72-afbe-6ac63995d1f0");

        String firstName = documentContext.read("$.firstName");
        assertThat(firstName).isEqualTo("John");

        String lastName = documentContext.read("$.lastName");
        assertThat(lastName).isEqualTo("Doe");
    }

    @Test
    void shouldReturnAPageOfPersons() {
        ResponseEntity<String> response = restTemplate.getForEntity("/persons?page=0&size=4", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(4);
    }

    @Test
    void shouldReturnAPageOfPersonsWithFirstName() {
        ResponseEntity<String> response = restTemplate.getForEntity("/persons/firstname/John?page=0&size=4", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");

        assertThat(page.size()).isEqualTo(1);

        List<String> firstNames = documentContext.read("$[*].firstName");
        assertThat(firstNames).allMatch(name -> name.equals("John"));
    }

    @Test
    void shouldCreateAPerson() {
        PersonDto newPerson = new PersonDto();
        newPerson.setFirstName("Alexis");
        newPerson.setLastName("Rodriguez");
        newPerson.setSsn("123456789");
        ResponseEntity<Void> createResponse = restTemplate.postForEntity("/persons", newPerson, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldUpdateAnExistingPerson() {
        PersonDto personUpdate = new PersonDto();
        personUpdate.setFirstName("John");
        personUpdate.setLastName("Doe");
        personUpdate.setSsn("987654321");
        HttpEntity<PersonDto> request = new HttpEntity<>(personUpdate);
        ResponseEntity<Void> response = restTemplate.exchange("/persons/20d41faf-139d-4ec6-8b01-f5a0e9cf56b7", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    /*@Test
    void shouldPartiallyUpdatePerson() {

        // Prepare the partial update data (JSON format)
        Map<String, Object> updates = Map.of("firstName", "Smith");


        //Person personUpdate = new Person(null, "Alex", "Rod");
        HttpEntity<Map<String,Object>> request = new HttpEntity<>(updates);
        ResponseEntity<Void> response = restTemplate
                .exchange("/persons/61f41ea4-29aa-4307-b144-3b807ef2a828", HttpMethod.PATCH, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);


    }*/

    @Test
    void shouldDeleteAnExistingPerson() {
        ResponseEntity<Void> response = restTemplate.exchange("/persons/05686ccc-115f-417b-85ee-0763c3e68b92", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
