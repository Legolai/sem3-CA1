package dtos;

import entities.Person;
import entities.Phone;
import entities.RenameMe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PersonDTO {


    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private List<PhoneDTO> phones;
    private AddressDTO address;
    private List<HobbyDTO> hobbies;


    public PersonDTO(int id, String firstName, String lastName, String email, List<PhoneDTO> phones, AddressDTO address, List<HobbyDTO> hobbies) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phones = phones;
        this.address = address;
        this.hobbies = hobbies;
    }

    public PersonDTO(Person person) {
        if (person.getId() != null && person.getId() != 0) {
            this.id = person.getId();
        }

        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
        this.phones = PhoneDTO.toList(new ArrayList<>(person.getPhones()));
        this.address = new AddressDTO(person.getAddress());
        this.hobbies = HobbyDTO.toList(new ArrayList<>(person.getHobbies()));
    }

    public static List<PersonDTO> getDtos(List<Person> people){
        return people.stream().map(PersonDTO::new).collect(Collectors.toList());
    }

    public Person getEntity(){
        Person person = new Person();
        if(this.id != 0) {
            person.setId(this.id);
        }

        person.setFirstName(this.firstName);
        person.setLastName(this.lastName);
        person.setEmail(this.email);
        person.setAddress(this.address.getEntity());
        person.setPhones(this.phones.stream().map(PhoneDTO::getEntity).collect(Collectors.toSet()));
        person.setHobbies(this.hobbies.stream().map(HobbyDTO::getEntity).collect(Collectors.toSet()));

        return person;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<PhoneDTO> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDTO> phones) {
        this.phones = phones;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public List<HobbyDTO> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<HobbyDTO> hobbies) {
        this.hobbies = hobbies;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonDTO)) return false;
        PersonDTO personDTO = (PersonDTO) o;
        return id == personDTO.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PersonDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phones=" + phones +
                ", address=" + address +
                ", hobbies=" + hobbies +
                '}';
    }



}
