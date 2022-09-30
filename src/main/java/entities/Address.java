package entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NamedQuery(name = "Address.deleteAllRows", query = "DELETE from Address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "street", nullable = false, length = 45)
    private String street;
    @Column(name = "floor", length = 45)
    private String floor;
    @Column(name = "door", length = 45)
    private String door;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CityInfo_zipCode", nullable = false)
    private CityInfo cityinfoZipcode;

    public Address() {

    }
    public Address(String street, String floor, String door, CityInfo cityinfoZipcode) {
        this.street = street;
        this.floor = floor;
        this.door = door;
        this.cityinfoZipcode = cityinfoZipcode;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
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

    public CityInfo getCityinfoZipcode() {
        return cityinfoZipcode;
    }
    public void setCityinfoZipcode(CityInfo cityinfoZipcode) {
        this.cityinfoZipcode = cityinfoZipcode;
    }

    @Override
    public String toString() {
        return "Address{" + "id=" + id + ", street='" + street + '\'' + ", floor='" + floor + '\'' + ", door='" + door + '\'' + ", cityinfoZipcode=" + cityinfoZipcode + '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return id.equals(address.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}