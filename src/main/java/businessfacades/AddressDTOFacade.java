package businessfacades;

import datafacades.AddressFacade;
import datafacades.HobbyFacade;
import dtos.AddressDTO;
import entities.Address;

import javax.persistence.EntityManagerFactory;
import java.util.List;

public class AddressDTOFacade implements IDataDTOFacade<AddressDTO> {
    private static AddressDTOFacade instance;
    private static AddressFacade addressFacade;

    private AddressDTOFacade() {}

    public static AddressDTOFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            addressFacade = AddressFacade.getInstance(_emf);
            instance = new AddressDTOFacade();
        }
        return instance;
    }

    @Override
    public AddressDTO create(AddressDTO addressDTO) {
        Address address = addressDTO.getEntity();
        address = addressFacade.create(address);
        return new AddressDTO(address);
    }

    @Override
    public <Integer> AddressDTO getById(Integer id) {
        return new AddressDTO(addressFacade.getById(id));
    }

    @Override
    public List<AddressDTO> getAll() {
        return AddressDTO.toList(addressFacade.getAll());
    }

    @Override
    public AddressDTO update(AddressDTO addressDTO) {
        Address address = addressFacade.update(addressDTO.getEntity());
        return new AddressDTO(address);
    }

    @Override
    public <Integer> void delete(Integer id) {
        addressFacade.delete(id);
    }
}
