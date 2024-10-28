package example.person;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface PersonRepository extends CrudRepository<Person, UUID>, PagingAndSortingRepository<Person, UUID> {
    Page<Person> findByFirstName(String firstName, Pageable pageable);
    //Page<Person> findAll(Pageable pageable);
    boolean existsById(UUID id);
}
