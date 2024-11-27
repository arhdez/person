package example.person.service;

import example.person.dto.PersonAddressDto;
import example.person.mapper.PersonAddressMapper;
import example.person.repository.PersonAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonAddressService {
    private final PersonAddressRepository personAddressRepository;
    private final PersonAddressMapper personAddressMapper;

    public PersonAddressDto createPersonAddress(PersonAddressDto personAddressDto){
        return personAddressMapper.personAddressToPersonAddressDto(personAddressRepository.save(personAddressMapper.personAddressDtoToPersonAddress(personAddressDto)));
    }
}
