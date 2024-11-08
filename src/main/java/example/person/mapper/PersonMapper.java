package example.person.mapper;

import example.person.dto.PersonDto;
import example.person.jpa.Person;
import example.person.service.DateFormatService;
import example.person.service.SSNEncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class PersonMapper {

    @Autowired
    private SSNEncryptionService ssnEncryptionService;

    @Autowired
    private DateFormatService dateFormatService;

    public PersonDto personToPersonDto(Person person) {
        PersonDto personDto = new PersonDto();
        if (person != null) {
            personDto.setId(person.getId());
            personDto.setFirstName(person.getFirstName());
            personDto.setLastName(person.getLastName());
            personDto.setSsn(ssnEncryptionService.decrypt(person.getSsn()));
            personDto.setEmail(person.getEmail());
            personDto.setDateOfBirth(dateFormatService.localDateToString(person.getDateOfBirth()));
        }
        return personDto;
    }

    public Person personDtoToPerson(PersonDto personDto) {
        Person person = new Person();
        if (personDto != null) {
            person.setFirstName(personDto.getFirstName());
            person.setLastName(personDto.getLastName());
            person.setSsn(ssnEncryptionService.encrypt(personDto.getSsn()));
            person.setEmail(personDto.getEmail());
            person.setDateOfBirth(dateFormatService.stringToLocalDate(personDto.getDateOfBirth()));
        }

        return person;
    }

    public void update(Person existent, PersonDto updatedPersonDto) {
        if (existent != null && updatedPersonDto != null) {
            existent.setFirstName(updatedPersonDto.getFirstName());
            existent.setLastName(updatedPersonDto.getLastName());
            existent.setSsn(ssnEncryptionService.encrypt(updatedPersonDto.getSsn()));
            existent.setEmail(updatedPersonDto.getEmail());
            existent.setDateOfBirth(dateFormatService.stringToLocalDate(updatedPersonDto.getDateOfBirth()));
        }
    }
}
