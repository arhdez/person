package example.person;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;
    private String lastName;
    @Lob// This annotation indicates a large object, suitable for BYTEA
    private byte[] ssn;

    public Person() {}
    public Person(UUID id, String firstName, String lastName, byte[] ssn) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public byte[] getSsn() {
        return ssn;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSsn(byte[] ssn) {
        this.ssn = ssn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.deepEquals(ssn, person.ssn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, Arrays.hashCode(ssn));
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", ssn=" + Arrays.toString(ssn) +
                '}';
    }
}
