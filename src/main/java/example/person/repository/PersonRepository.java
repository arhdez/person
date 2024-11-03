package example.person.repository;

import example.person.jpa.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonRepository extends CrudRepository<Person, UUID>, PagingAndSortingRepository<Person, UUID> {
    Page<Person> findByFirstName(String firstName, Pageable pageable);
    //Page<Person> findAll(Pageable pageable);
    boolean existsById(UUID id);
}
