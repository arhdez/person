package example.person.mapper;

import example.person.dto.PersonDto;
import example.person.jpa.Person;
import org.mapstruct.BeanMapping;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDateTime;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, componentModel = "spring", imports = {LocalDateTime.class})
@DecoratedWith(PersonMapperDecorator.class)
public interface PersonMapper {

    @Mapping(target = "ssn", ignore = true)
    @Mapping(target = "dateOfBirth", ignore = true)
    PersonDto personToPersonDto(Person person);

    @Mapping(target = "ssn", ignore = true)
    @Mapping(target = "dateOfBirth", ignore = true)
    Person personDtoToPerson(PersonDto personDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "ssn", ignore = true)
    @Mapping(target = "dateOfBirth", ignore = true)
    void update(@MappingTarget Person existent, PersonDto updatedPersonDto);
}