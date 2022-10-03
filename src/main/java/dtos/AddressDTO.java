package dtos;

import entities.Address;
import entities.Person;

import java.util.*;
import java.util.stream.Collectors;

public class AddressDTO  {

    private int id;
    private String street;
    private String floor;
    private String door;
    private CityInfoDTO cityInfo;
    private Map<Integer, String> persons;

    public AddressDTO(int id, String street, String floor, String door, CityInfoDTO cityInfo,  List<Person> persons) {
        this.id = id;
        this.street = street;
        this.floor = floor;
        this.door = door;
        this.persons = persons.stream().collect(Collectors.toMap(Person::getId, Person::getFullName));
        this.cityInfo = cityInfo;
    }

    public AddressDTO(Address address) {
        if (address.getId() != null || address.getId() != 0) {
            this.id = address.getId();
        }
        this.street = address.getStreet();
        this.floor = address.getFloor();
        this.door = address.getDoor();
        this.cityInfo = new CityInfoDTO(address.getCityInfo());
        this.persons = address.getPeople().stream().collect(Collectors.toMap(Person::getId, Person::getFullName));
    }

    public static List<AddressDTO> toList(List<Address> addresses) {
        return addresses.stream().map(AddressDTO::new).collect(Collectors.toList());
    }

    public Address getEntity(){
        Address address = new Address();
        if (this.id != 0) {
            address.setId(id);
        }
        address.setStreet(this.street);
        address.setDoor(this.door);
        address.setFloor(this.floor);
        address.setCityInfo(this.cityInfo.getEntity());
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressDTO)) return false;
        AddressDTO that = (AddressDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDoor() {
        return door;
    }

    public void setDoor(String door) {
        this.door = door;
    }

    public CityInfoDTO getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfoDTO cityInfo) {
        this.cityInfo = cityInfo;
    }

    public Map<Integer, String> getPersons() {
        return persons;
    }

    public void setPersons(Map<Integer, String> persons) {
        this.persons = persons;
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", floor='" + floor + '\'' +
                ", door='" + door + '\'' +
                ", cityInfo=" + cityInfo +
                ", persons=" + persons +
                '}';
    }
}