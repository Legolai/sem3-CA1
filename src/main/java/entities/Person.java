package entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NamedQuery(name = "Person.deleteAllRows", query = "DELETE from Person ")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "email", nullable = false, length = 45)
    private String email;
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;
    @ManyToOne( fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "Address_id", nullable = false)
    private Address address;
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private Set<Phone> phones = new LinkedHashSet<>();
    @ManyToMany
    @JoinTable(name = "Person_has_Hobby", joinColumns = @JoinColumn(name = "Person_id"), inverseJoinColumns = @JoinColumn(name = "Hobby_name"))
    private Set<Hobby> hobbies = new LinkedHashSet<>();

    public Person() {

    }
    public Person(String email, String firstName, String lastName, Address address, Set<Phone> phones, Set<Hobby> hobbies) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phones = phones;
        this.hobbies = hobbies;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return String.format("%s %s", this.firstName, this.lastName);
    }

    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public void assignPhone(Phone phone) {
        if (phone == null) return;
        this.phones.add(phone);
    }
    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }

    public Set<Hobby> getHobbies() {
        return hobbies;
    }
    public void setHobbies(Set<Hobby> hobbies) {
        this.hobbies = hobbies;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", email='" + email + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", address=" + address + ", phones=" + phones + ", hobbies=" + hobbies.stream().reduce("", (acc, curr) -> acc + ", " + curr.getName(), String::concat) + '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id.equals(person.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void assignHobby(Hobby hobby) {
        if (hobby == null) return;
        hobbies.add(hobby);
    }
}
