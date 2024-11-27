package example.person.mapper;

import example.person.dto.PersonAddressDto;
import example.person.jpa.PersonAddress;
import org.mapstruct.*;


@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PersonAddressMapper {


    @Mapping(target = "personId", source = "id.personId")
    @Mapping(target = "addressId", source = "id.addressId")
    PersonAddressDto personAddressToPersonAddressDto(PersonAddress personAddress);

    @Mapping(target = "id.personId", source = "personId")
    @Mapping(target = "id.addressId", source = "addressId")
    PersonAddress personAddressDtoToPersonAddress(PersonAddressDto personAddressDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget PersonAddress existent, PersonAddressDto updatedPersonAddressDto);

}
