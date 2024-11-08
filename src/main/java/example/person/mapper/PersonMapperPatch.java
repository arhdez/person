package example.person.mapper;

import example.person.dto.PersonDto;
import example.person.jpa.Person;
import example.person.service.SSNEncryptionService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring")
public abstract class PersonMapperPatch {

    @Autowired
    private SSNEncryptionService ssnEncryptionService;

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updatePersonFromDto(PersonDto dto, @MappingTarget Person person);

    // Encrypts a String to byte[]
    public byte[] mapStringToByteArray(String value) {
        return value != null ? ssnEncryptionService.encrypt(value) : null;
    }

    // Decrypts byte[] to String
    public String mapByteArrayToString(byte[] value) {
        return value != null ? ssnEncryptionService.decrypt(value) : null;
    }
}

