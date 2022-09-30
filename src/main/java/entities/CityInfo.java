package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.util.Objects;

@Entity
@NamedQuery(name = "CityInfo.deleteAllRows", query = "DELETE from CityInfo")
public class CityInfo {
    @Id
    @Column(name = "zipCode", nullable = false)
    private String zipCode;
    @Column(name = "city", nullable = false, length = 45)
    private String city;

    public CityInfo() {

    }
    public CityInfo(String zipCode, String city) {
        this.zipCode = zipCode;
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "CityInfo{" + "zipCode=" + zipCode + ", city='" + city + '\'' + '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityInfo cityInfo = (CityInfo) o;
        return zipCode.equals(cityInfo.zipCode);
    }
    @Override
    public int hashCode() {
        return Objects.hash(zipCode);
    }
}
