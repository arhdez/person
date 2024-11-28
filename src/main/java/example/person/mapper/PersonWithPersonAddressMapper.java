package example.person.mapper;

import example.person.dto.PersonAddressDto;
import example.person.dto.PersonDto;
import example.person.dto.PersonWithPersonAddressDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, componentModel = "spring", imports = {LocalDateTime.class})
//@DecoratedWith(PersonMapperDecorator.class)
public interface PersonWithPersonAddressMapper {

    @Mapping(target = "person", source = "personDto")
    @Mapping(target = "personAddress", source = "personAddressDto")
    PersonWithPersonAddressDto toPersonWithPersonAddressDto(PersonDto personDto, PersonAddressDto personAddressDto);

    /*@Mapping(target = "")
    PersonWithPersonAddressDto toCustomPersonAddressDto(PersonDto personDto, PersonAddressDto personAddressDto);*/
}
