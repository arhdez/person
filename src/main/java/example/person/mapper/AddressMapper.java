package example.person.mapper;

import example.person.dto.AddressDto;
import example.person.jpa.Address;
import org.mapstruct.*;


@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AddressMapper {

    AddressDto addressToAddressDto(Address address);

    Address addressDtoToAddress(AddressDto addressDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Address existent, AddressDto updatedAddressDto);

}
