package example.person.service;

import example.person.dto.AddressKafkaDto;
import example.person.jpa.Address;
import example.person.mapper.AddressMapper;
import example.person.repository.AddressRepository;
import example.person.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public void processAddress(AddressKafkaDto addressKafkaDto) {
        if (addressRepository.existsById(addressKafkaDto.getAddressId())) {
            updateAddress(addressKafkaDto, addressKafkaDto.getAddressId());
        } else {
            createAddress(addressKafkaDto);
        }
    }

    public AddressKafkaDto createAddress(AddressKafkaDto addressKafkaDto){
        return addressMapper.addressToAddressDto(addressRepository.save(addressMapper.addressDtoToAddress(addressKafkaDto)));
    }

    public List<AddressKafkaDto> findAllAddress(Pageable pageable){
        PageRequest pageRequest =
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSortOr(Sort.by(Sort.Direction.ASC, "street")));
        return addressRepository.findAll(pageRequest).stream().map(addressMapper::addressToAddressDto).collect(Collectors.toList());
    }

    public Optional<AddressKafkaDto> updateAddress(AddressKafkaDto addressToUpdateDto, UUID requestedId){
        Optional<Address> existentAddressOptional = addressRepository.findById(requestedId);
        if (existentAddressOptional.isPresent()){
            Address existentAddress = existentAddressOptional.get();
            addressMapper.update(existentAddress, addressToUpdateDto);
            return Optional.of(addressMapper.addressToAddressDto(addressRepository.save(existentAddress)));
        }
        return Optional.empty();
    }
    /*public List<AddressDto> findAllAddress(Pageable pageable){
        PageRequest pageRequest =
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSortOr(Sort.by(Sort.Direction.ASC, "firstName")));
        return addressRepository.findAll(pageRequest).stream().map(addressMapper::addressToAddressDto).collect(Collectors.toList())
    }*/
}
