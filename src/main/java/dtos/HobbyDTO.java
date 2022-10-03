package dtos;

import entities.Hobby;
import entities.Person;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

public class HobbyDTO {

    private String name;
    private String description;
    private String category;
    private String type;
    private Map<Integer, String> people = new HashMap<>();

    public HobbyDTO(String name, String description, String category, String type) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.type = type;
    }
    public HobbyDTO(String name, String description, String category, String type, Set<Person> people) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.type = type;
        people.forEach(Person -> this.people.put(Person.getId(),
                Person.getFirstName()+" "+Person.getLastName()));
    }
    public HobbyDTO(Hobby hobby) {
        this.name = hobby.getName();
        this.description = hobby.getDescription();
        this.category = hobby.getCategory();
        this.type = hobby.getType();
        hobby.getPeople().forEach(Person -> this.people.put(Person.getId(),
                Person.getFirstName()+" "+Person.getLastName()));
    }

    public static List<HobbyDTO> toList(List<Hobby> hobbyList) {
        return hobbyList.stream().map(HobbyDTO::new).collect(Collectors.toList());
    }

    public Hobby getEntity(){
        return new Hobby(this.name, this.description, this.category, this.type);
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

    public Map<Integer, String> getPeople() {
        return people;
    }
    public void setPeople(Map<Integer, String> people) {
        this.people = people;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HobbyDTO hobbyDTO = (HobbyDTO) o;
        return name.equals(hobbyDTO.name);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    @Override
    public String toString() {
        return "HobbyDTO{" + "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", people=" + people.toString() + '}';
    }
}
