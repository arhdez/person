package example.person.mapper;

import example.person.dto.PersonDto;
import example.person.jpa.Person;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.nio.charset.StandardCharsets;

@Mapper(componentModel = "spring")
public interface PersonMapperPatch {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePersonFromDto(PersonDto dto, @MappingTarget Person person);

    // Custom mapping method for String -> byte[]
    default byte[] mapStringToByteArray(String value) {
        //return value != null ? value.getBytes(StandardCharsets.UTF_8) : null;
        return value != null ? value.getBytes(StandardCharsets.UTF_8) : null;
    }

    // Custom mapping method for byte[] -> String (if needed for reverse mapping)
    default String mapByteArrayToString(byte[] value) {
        return value != null ? new String(value, StandardCharsets.UTF_8) : null;
    }

    /*PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    @Mapping(target = "dateOfBirth", source = "dateOfBirth", dateFormat = "dd-MM-yyyy")
    PersonDto personToPersonDTO(Person person);

    @Mapping(target = "dateOfBirth", source = "dateOfBirth", dateFormat = "dd-MM-yyyy")
    Person personDTOToPerson(PersonDto personDTO);*/
}
