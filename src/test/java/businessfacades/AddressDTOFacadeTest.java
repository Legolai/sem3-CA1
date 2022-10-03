package businessfacades;

import datafacades.PersonFacade;
import dtos.AddressDTO;
import entities.*;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddressDTOFacadeTest {

    private static EntityManagerFactory emf;
    private static AddressDTOFacade facade;
    private static PersonFacade personFacade;

    AddressDTO hobbyDTO1, hobbyDTO2;
    Person person1, person2;

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
        CityInfo cityInfo2 = new CityInfo("4000", "Helsingør");
        Address address1 = new Address("Møllevej 18", null, null, cityInfo1);
        Address address2 = new Address("Strandvejen 4", null, null, cityInfo2);

        person1 = new Person();
        person1.setFirstName("Jens");
        person1.setLastName("Jensen");
        person1.setEmail("Jensen@email.com");
        person1.setAddress(address1);
        person2 = new Person();
        person2.setFirstName("Mads");
        person2.setLastName("Madsen");
        person2.setEmail("Madsen@email.com");
        person2.setAddress(address2);

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
            em.close();
        }
    }
    @AfterEach
    public void tearDown() {
        //        Remove any data after each test was run
    }


    // TODO: Delete or change this method
    @Test
    void testShouldGetNumberOfAddressesExisting() throws Exception {
        assertEquals(2, facade.getAll().size(), "Expects two rows in the database");
    }

    @Test
    void testShouldCreateAddress() {
        assertDoesNotThrow(() -> facade.create(new AddressDTO("")));
        assertEquals(3, facade.getAll().size());
        assertThrows(IllegalStateException.class, () -> facade.create(new AddressDTO("")));
    }

    @Test
    void testShouldGetAddressByName() {
        assertThrows(EntityNotFoundException.class, () -> facade.getById("Amatørradio"));
        AddressDTO hobby = facade.getById("Akrobatik");
        assertEquals(hobbyDTO1, hobby);
    }

    @Test
    void testShouldDeleteAddress1() {
        assertThrows(EntityNotFoundException.class, () -> facade.delete("Amatørradio"));
        facade.delete(hobbyDTO1.getName());
        List<AddressDTO> hobbies = facade.getAll();
        assertEquals(1, hobbies.size());
        assertEquals(hobbyDTO2, hobbies.get(0));
        System.out.println("Hobby1 has succesfully been deleted");
    }

    @Test
    void testShouldUpdateAddress1() {
        AddressDTO hobby = new AddressDTO("");
        assertThrows(EntityNotFoundException.class, () -> facade.update(hobby));
        hobbyDTO1.setType("");
        facade.update(hobbyDTO1);
        AddressDTO updatedAddress1 = facade.getById(hobbyDTO1.getName());
        assertEquals(hobbyDTO1, updatedAddress1);
    }
}
