package example.person.repository;

import example.person.jpa.PersonAddress;
import example.person.jpa.PersonAddressId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonAddressRepository extends CrudRepository<PersonAddress, PersonAddressId>, PagingAndSortingRepository<PersonAddress, PersonAddressId> {
}
