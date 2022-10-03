package datafacades;

import entities.Address;
import entities.CityInfo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.util.List;

public class AddressFacade implements IDataFacade<Address, Integer>{

    private static AddressFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private AddressFacade() {}

    public static AddressFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AddressFacade();
        }
        return instance;
    }
    @Override
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public Address create(String street, String floor, String door, CityInfo cityinfoZipcode) {
        Address address = new Address(street, floor, door, cityinfoZipcode);
        create(address);
        return address;
    }
    @Override
    public Address create(Address address) {
        executeInsideTransaction((em) -> {
            em.persist(address);
        });
        return address;    //TODO: errorhandling, in general all methods needs
    }

    @Override
    public Address getById(Integer id) {
        Address a =  executeWithClose((em) -> em.find(Address.class, id));
        if (a == null)
            throw new EntityNotFoundException("The Address entity with id: "+id+" Was not found");
        return a;
    }

    @Override
    public List<Address> getAll() {
        return executeWithClose((em) -> {
            TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a", Address.class);
            List<Address> addresses = query.getResultList();
            return addresses;
        });
    }

    @Override
    public Address update(Address address) {
        if (address.getId() == null)
            throw new IllegalArgumentException("No Address can be updated when id is missing");
        Address check = executeWithClose((em) -> em.find(Address.class, address.getId()));
        if (check == null)
            throw new IllegalArgumentException("No Address with the zipCode: "+address.getId());

        executeInsideTransaction((em) -> {
            em.merge(address);
        });
        return address;
    }

    @Override
    public void delete(Integer id) {
        executeInsideTransaction((em) -> {
            Address a =  em.find(Address.class, id);
            if (a == null)
                throw new EntityNotFoundException("The Address entity with id: "+id+" Was not found");
            em.remove(a);
        });
    }
}
