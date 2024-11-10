package example.person.mapper;

import example.person.dto.PersonDto;
import example.person.jpa.Person;
import example.person.service.DateFormatService;
import example.person.service.SSNEncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class PersonMapperDecorator implements PersonMapper {

    @Autowired
    @Qualifier("delegate")
    PersonMapper delegate;

    @Autowired
    SSNEncryptionService ssnEncryptionService;

    @Autowired
    DateFormatService dateFormatService;

    @Override
    public PersonDto personToPersonDto(Person person) {
        PersonDto personDto = delegate.personToPersonDto(person);
        personDto.setSsn(ssnEncryptionService.decrypt(person.getSsn()));
        personDto.setDateOfBirth(dateFormatService.localDateToString(person.getDateOfBirth()));
        return personDto;
    }

    @Override
    public Person personDtoToPerson(PersonDto personDto) {
        Person person = delegate.personDtoToPerson(personDto);
        person.setSsn(ssnEncryptionService.encrypt(personDto.getSsn()));
        person.setDateOfBirth(dateFormatService.stringToLocalDate(personDto.getDateOfBirth()));
        return person;
    }

    @Override
    public void update(Person existent, PersonDto updatedPersonDto) {
        delegate.update(existent, updatedPersonDto);

        if (updatedPersonDto.getSsn() != null) {
            existent.setSsn(ssnEncryptionService.encrypt(updatedPersonDto.getSsn()));
        }

        if (updatedPersonDto.getDateOfBirth() != null) {
            existent.setDateOfBirth(dateFormatService.stringToLocalDate(updatedPersonDto.getDateOfBirth()));
        }
    }
}
