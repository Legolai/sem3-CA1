package entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Phone {
    @Id
    @Column(name = "number", nullable = false, length = 45)
    private String number;
    @Column(name = "description", length = 45)
    private String description;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Person_id", nullable = false)
    private Person person;

    public Phone() {

    }
    public Phone(String number, String description, Person person) {
        this.number = number;
        this.description = description;
        this.person = person;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Person getPerson() {
        return person;
    }
    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "Phone{" + "number='" + number + '\'' + ", description='" + description + '\'' + ", person=" + person + '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return number.equals(phone.number);
    }
    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
