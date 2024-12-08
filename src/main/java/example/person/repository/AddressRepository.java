package example.person.repository;

import example.person.jpa.Address;
import example.person.jpa.PersonAddress;
import example.person.jpa.PersonAddressId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AddressRepository extends CrudRepository<Address, UUID>, PagingAndSortingRepository<Address, UUID> {
    //List<PersonAddress> findByAddressId(UUID uuid);
    boolean existsByAddressId(UUID addressId);
}
