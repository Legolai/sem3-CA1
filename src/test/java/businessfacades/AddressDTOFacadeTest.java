package businessfacades;

import datafacades.PersonFacade;
import dtos.AddressDTO;
import entities.*;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddressDTOFacadeTest {

    private static EntityManagerFactory emf;
    private static AddressDTOFacade facade;
    private static PersonFacade personFacade;

    AddressDTO addressDTO, addressDTO1 ;
    Address address, address1;
    Person person1, person2;
    CityInfo cityInfo2;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = AddressDTOFacade.getInstance(emf);
        personFacade = PersonFacade.getInstance(emf);
    }
    @AfterAll
    public static void tearDownClass() {
        //        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        CityInfo cityInfo1 = new CityInfo("1000", "København");
        cityInfo2 = new CityInfo("4000", "Helsingør");

        address = new Address("Møllevej 18", null, null, cityInfo1);
        address1 = new Address("Strandvejen 4", null, null, cityInfo2);
        addressDTO = new AddressDTO(address);
        addressDTO1 = new AddressDTO(address1);

        person1 = new Person();
        person1.setFirstName("Jens");
        person1.setLastName("Jensen");
        person1.setEmail("Jensen@email.com");
        person1.setAddress(address);
        person2 = new Person();
        person2.setFirstName("Mads");
        person2.setLastName("Madsen");
        person2.setEmail("Madsen@email.com");
        person2.setAddress(address1);

        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.persist(cityInfo1);
            em.persist(cityInfo2);
            em.persist(person1);
            em.persist(person2);

            em.getTransaction().commit();
        } finally {
            addressDTO.setId(address.getId());
            addressDTO1.setId(address1.getId());
            em.close();
        }
    }
    @AfterEach
    public void tearDown() {
        //        Remove any data after each test was run
    }


    @Test
    void testShouldGetNumberOfAddressesExisting() throws Exception {
        assertEquals(2, facade.getAll().size(), "Expects two rows in the database");
    }

    @Test
    void testShouldCreateAddress() {
        Address newAddress = new Address("Tinghøjvej 58", "", "", new CityInfo("4000", "Helsingør"));
        AddressDTO newAddressDTO = new AddressDTO(newAddress);
        assertDoesNotThrow(() -> facade.create(newAddressDTO));
        assertEquals(3, facade.getAll().size());
        assertThrows(IllegalStateException.class, () -> facade.create(new AddressDTO(new Address("Tinghøjvej 58", "", "", new CityInfo("2860", "Søborg")))));
    }

    @Test
    void testShouldGetAddressById() {
        assertThrows(EntityNotFoundException.class, () -> facade.getById(10));
        AddressDTO addressDTO = facade.getById(this.addressDTO.getId());
        assertEquals(this.addressDTO, addressDTO);
    }

    @Test
    void testShouldDeleteAddressById() {
        assertThrows(EntityNotFoundException.class, () -> facade.delete(10));
        personFacade.delete(person1.getId());
        List<AddressDTO> addresses = facade.getAll();
        assertEquals(1, addresses.size());
        assertEquals(addressDTO1, addresses.get(0));
        System.out.println("addressDTO has succesfully been deleted");
    }

    @Test
    void testShouldUpdateAddress() {
        AddressDTO addressDTO = new AddressDTO(new Address("Tinghøjvej 58", "", "", cityInfo2));
        assertThrows(IllegalArgumentException.class, () -> facade.update(addressDTO));
        addressDTO1.setStreet("Tinghøjvej 100");
        addressDTO1.setFloor("2");
        facade.update(addressDTO1);
        AddressDTO updatedAddress1 = facade.getById(addressDTO1.getId());
        assertEquals(addressDTO1, updatedAddress1);
    }
}
