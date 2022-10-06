package businessfacades;

import datafacades.CityInfoFacade;
import datafacades.HobbyFacade;
import dtos.CityInfoDTO;
import errorhandling.EntityNotFoundException;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class CityInfoDTOFacade implements IDataDTOFacade<CityInfoDTO, String> {
    private static CityInfoDTOFacade instance;
    private static CityInfoFacade cityInfoFacade;

    private CityInfoDTOFacade() {}

    public static CityInfoDTOFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            cityInfoFacade = CityInfoFacade.getInstance(_emf);
            instance = new CityInfoDTOFacade();
        }
        return instance;
    }

    @Override
    public CityInfoDTO create(CityInfoDTO cityInfoDTO) {
        return new CityInfoDTO(cityInfoFacade.create(cityInfoDTO.getEntity()));
    }

    @Override
    public CityInfoDTO getById(String zipCode) throws EntityNotFoundException {
        return new CityInfoDTO(cityInfoFacade.getById(zipCode));
    }

    @Override
    public List<CityInfoDTO> getAll() {
        return CityInfoDTO.toList(cityInfoFacade.getAll());
    }

    @Override
    public CityInfoDTO update(CityInfoDTO cityInfoDTO) throws EntityNotFoundException {
        return new CityInfoDTO(cityInfoFacade.update(cityInfoDTO.getEntity()));
    }

    @Override
    public void delete(String zipCode) throws EntityNotFoundException {
        cityInfoFacade.delete(zipCode);
    }

}
