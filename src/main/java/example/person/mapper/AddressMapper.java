package example.person.mapper;

import example.person.dto.AddressKafkaDto;
import example.person.jpa.Address;
import org.mapstruct.*;


@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AddressMapper {

    AddressKafkaDto addressToAddressDto(Address address);

    Address addressDtoToAddress(AddressKafkaDto addressKafkaDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Address existent, AddressKafkaDto updatedAddressKafkaDto);

}
