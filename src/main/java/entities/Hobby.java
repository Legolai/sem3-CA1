package entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NamedQuery(name = "Hobby.deleteAllRows", query = "DELETE from Hobby")
public class Hobby {
    @Id
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "category", nullable = false, length = 45)
    private String category;
    @Column(name = "type", nullable = false, length = 45)
    private String type;
    @ManyToMany(cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "Person_has_Hobby", joinColumns = @JoinColumn(name = "Hobby_name"), inverseJoinColumns = @JoinColumn(name = "Person_id"))
    private Set<Person> people = new LinkedHashSet<>();

    public Hobby() {

    }
    public Hobby(String name, String description, String category, String type) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.type = type;
    }
    public Hobby(String name, String description, String category, String type, Set<Person> people) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.type = type;
        this.people = people;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Set<Person> getPeople() {
        return people;
    }
    public void setPeople(Set<Person> people) {
        this.people = people;
    }

    @Override
    public String toString() {
        return "Hobby{" + "name='" + name + '\'' + ", description='" + description + '\'' + ", category='" + category + '\'' + ", type='" + type + '\'' + ", people=" + people + '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hobby hobby = (Hobby) o;
        return name.equals(hobby.name);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void assignPerson(Person person) {
        if (person == null) return;
        people.add(person);
    }
}