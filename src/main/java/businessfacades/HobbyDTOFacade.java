package businessfacades;

import datafacades.HobbyFacade;
import datafacades.PersonFacade;
import dtos.HobbyDTO;
import entities.Hobby;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Map;

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
    public HobbyDTO getById(String name) {
        return new HobbyDTO(hobbyFacade.getById(name));
    }

    @Override
    public List<HobbyDTO> getAll() {
        return HobbyDTO.toList(hobbyFacade.getAll());
    }

    @Override
    public HobbyDTO update(HobbyDTO hobbyDTO) {
        Hobby h;
        if (hobbyDTO.getPeople().isEmpty()) {
            h = hobbyFacade.update(hobbyDTO.getEntity());
        } else {
            Hobby toBeUpdated = hobbyDTO.getEntity();
            Hobby inDB = hobbyFacade.getById(hobbyDTO.getName());
            if (hobbyDTO.getPeople().equals(inDB.getPeople())) {
                toBeUpdated.setPeople(inDB.getPeople());
            } else {
                for (Map.Entry<Integer,String> entry : hobbyDTO.getPeople().entrySet()) {
                    toBeUpdated.assignPerson(personFacade.getById(entry.getKey()));
                }
            }
            h = hobbyFacade.update(toBeUpdated);
        }
        return new HobbyDTO(h);
    }

    @Override
    public void delete(String name) {
        hobbyFacade.delete(name);
    }
}
