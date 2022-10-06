package businessfacades;

import datafacades.HobbyFacade;
import datafacades.PersonFacade;
import dtos.HobbyDTO;
import entities.Hobby;
import errorhandling.EntityNotFoundException;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HobbyDTOFacade implements IDataDTOFacade<HobbyDTO, String> {
    private static HobbyDTOFacade instance;
    private static HobbyFacade hobbyFacade;

    private static PersonFacade personFacade;

    private HobbyDTOFacade() {}

    public static HobbyDTOFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            hobbyFacade = HobbyFacade.getInstance(_emf);
            instance = new HobbyDTOFacade();

            personFacade = PersonFacade.getInstance(_emf);
        }
        return instance;
    }


    @Override
    public HobbyDTO create(HobbyDTO hobbyDTO) {
        Hobby h = hobbyDTO.getEntity();
        h = hobbyFacade.create(h);
        return new HobbyDTO(h);
    }

    @Override
    public HobbyDTO getById(String name) throws EntityNotFoundException {
        return new HobbyDTO(hobbyFacade.getById(name));
    }

    @Override
    public List<HobbyDTO> getAll() {
        return HobbyDTO.toList(hobbyFacade.getAll());
    }

    @Override
    public HobbyDTO update(HobbyDTO hobbyDTO) throws EntityNotFoundException {
        Hobby h;
        if (hobbyDTO.getPeople().isEmpty()) {
            h = hobbyFacade.update(hobbyDTO.getEntity());
        } else {
            Hobby toBeUpdated = hobbyDTO.getEntity();
            for (Integer id : hobbyDTO.getPeople().keySet()) {
                toBeUpdated.assignPerson(personFacade.getById(id));
            }
            h = hobbyFacade.update(toBeUpdated);
        }
        return new HobbyDTO(h);
    }

    @Override
    public void delete(String name) throws EntityNotFoundException {
        hobbyFacade.delete(name);
    }

    public Map<String, Integer> getCountOfAllMembers() {
        return hobbyFacade.getCountOfAllMembers();
    }
    public Integer getCountOfMembersForHobby(String name) {
        return hobbyFacade.getCountOfMembersForHobby(name);
    }
}
