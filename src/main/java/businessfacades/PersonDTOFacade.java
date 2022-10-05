package businessfacades;

import datafacades.HobbyFacade;
import datafacades.PersonFacade;
import dtos.PersonDTO;
import entities.Person;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class PersonDTOFacade implements IDataDTOFacade<PersonDTO, Integer> {

    private static PersonDTOFacade instance;
    private static PersonFacade personFacade;

    private PersonDTOFacade() {}

    public static PersonDTOFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            personFacade = PersonFacade.getInstance(_emf);
            instance = new PersonDTOFacade();
        }
        return instance;
    }


    @Override
    public PersonDTO create(PersonDTO personDTO) {
        return new PersonDTO(personFacade.create(personDTO.getEntity()));
    }

    @Override
    public PersonDTO getById(Integer id) {
        return new PersonDTO(personFacade.getById(id));
    }

    public PersonDTO getByPhoneNumber(String phoneNumber) {
        return new PersonDTO(personFacade.getByPhoneNumber(phoneNumber));
    }

    @Override
    public List<PersonDTO> getAll() {
        return PersonDTO.convertEntitiesToDTOs(personFacade.getAll());
    }

    @Override
    public PersonDTO update(PersonDTO personDTO) {
        return new PersonDTO(personFacade.update(personDTO.getEntity()));
    }

    @Override
    public void delete(Integer id) {
        personFacade.delete(id);
    }

    public List<PersonDTO> getAllByZipCode(String zipCode) {
        return PersonDTO.convertEntitiesToDTOs(personFacade.getAllByCityInfo(zipCode));
    }


}
