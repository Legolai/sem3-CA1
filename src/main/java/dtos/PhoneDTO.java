package dtos;

import entities.Phone;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PhoneDTO {

    private String number;
    private String description;

    public PhoneDTO(String number, String description) {
        this.number = number;
        this.description = description;
    }

    public PhoneDTO(Phone phone) {
        this.number = phone.getNumber();
        this.description = phone.getDescription();
    }

    public static List<PhoneDTO> toList(List<Phone> phones) {
        return phones.stream().map(PhoneDTO::new).collect(Collectors.toList());
    }

    public Phone getEntity(){
        Phone phone = new Phone();
        phone.setNumber(this.number);
        phone.setDescription(this.description);
        return phone;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneDTO)) return false;
        PhoneDTO phoneDTO = (PhoneDTO) o;
        return Objects.equals(getNumber(), phoneDTO.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber());
    }

    @Override
    public String toString() {
        return "PhoneDTO{" +
                "number='" + number + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
