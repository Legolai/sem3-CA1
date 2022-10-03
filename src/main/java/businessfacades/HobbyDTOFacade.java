package businessfacades;

import datafacades.HobbyFacade;
import dtos.HobbyDTO;
import entities.Hobby;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class HobbyDTOFacade implements IDataDTOFacade<HobbyDTO> {
    private static HobbyDTOFacade instance;
    private static HobbyFacade hobbyFacade;

    private HobbyDTOFacade() {}

    public static HobbyDTOFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            hobbyFacade = HobbyFacade.getInstance(_emf);
            instance = new HobbyDTOFacade();
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
    public <P> HobbyDTO getById(P p) {
        return new HobbyDTO(hobbyFacade.getById(p));
    }

    @Override
    public List<HobbyDTO> getAll() {
        return HobbyDTO.toList(hobbyFacade.getAll());
    }

    @Override
    public HobbyDTO update(HobbyDTO hobbyDTO) {
        Hobby h = hobbyFacade.update(hobbyDTO.getEntity());
        return new HobbyDTO(h);
    }

    @Override
    public <P> void delete(P p) {
        hobbyFacade.delete(p);
    }
}
