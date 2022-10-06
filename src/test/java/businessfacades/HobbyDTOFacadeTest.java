package businessfacades;

import datafacades.PersonFacade;
import dtos.HobbyDTO;
import entities.*;
import errorhandling.EntityNotFoundException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HobbyDTOFacadeTest {

    private static EntityManagerFactory emf;
    private static HobbyDTOFacade facade;
    private static PersonFacade personFacade;

    HobbyDTO hobbyDTO1, hobbyDTO2;
    Person person1, person2;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = HobbyDTOFacade.getInstance(emf);
        personFacade = PersonFacade.getInstance(emf);
    }
    @AfterAll
    public static void tearDownClass() {
        //        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        Hobby hobby1 = new Hobby("Akrobatik", "https://en.wikipedia.org/wiki/Acrobatics",
                "Generel", "Indendørs");
        Hobby hobby2 = new Hobby("Skuespil", "https://en.wikipedia.org/wiki/Acting",
                "Generel", "Indendørs");
        hobbyDTO1 = new HobbyDTO(hobby1);
        hobbyDTO2 = new HobbyDTO(hobby2);

        CityInfo cityInfo1 = new CityInfo("1000", "København");
        CityInfo cityInfo2 = new CityInfo("4000", "Helsingør");

        person1 = new Person();
        person1.setFirstName("Jens");
        person1.setLastName("Jensen");
        person1.setEmail("Jensen@email.com");
        person1.setAddress(new Address("Møllevej 18", null, null, cityInfo1));
        person1.assignPhone(new Phone("20203040", "mobil", person1));
        person1.assignHobby(hobby1);
        person2 = new Person();
        person2.setFirstName("Mads");
        person2.setLastName("Madsen");
        person2.setEmail("Madsen@email.com");
        person2.setAddress(new Address("Strandvejen 4", null, null, cityInfo2));
        person2.assignPhone(new Phone("40993040", "mobil", person2));
        person2.assignHobby(hobby2);


        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.persist(hobby1);
            em.persist(hobby2);
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
    void testShouldGetNumberOfHobbiesExisting() throws Exception {
        assertEquals(2, facade.getAll().size(), "Expects two rows in the database");
    }

    @Test
    void testShouldCreateHobby() {
        assertDoesNotThrow(() -> facade.create(new HobbyDTO("Amatørradio","https://en.wikipedia.org/wiki/Amateur_radio","Generel","Indendørs")));
        assertEquals(3, facade.getAll().size());
        assertThrows(IllegalStateException.class, () -> facade.create(new HobbyDTO("Amatørradio","https://en.wikipedia.org/wiki/Amateur_radio","Generel","Indendørs")));
    }

    @Test
    void testShouldGetHobbyByName() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> facade.getById("Amatørradio"));
        HobbyDTO hobby = facade.getById("Akrobatik");
        assertEquals(hobbyDTO1, hobby);
    }

    @Test
    void testShouldDeleteHobby1() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> facade.delete("Amatørradio"));
        facade.delete(hobbyDTO1.getName());
        List<HobbyDTO> hobbies = facade.getAll();
        assertEquals(1, hobbies.size());
        assertEquals(hobbyDTO2, hobbies.get(0));
        System.out.println("Hobby1 has succesfully been deleted");
    }

    @Test
    void testShouldUpdateHobby1() throws EntityNotFoundException {
        HobbyDTO hobby = new HobbyDTO("Amatørradio", "https://en.wikipedia.org/wiki/Amateur_radio",
                "Generel", "Indendørs");
        assertThrows(EntityNotFoundException.class, () -> facade.update(hobby));
        hobbyDTO1.setType("Udendørs");
        facade.update(hobbyDTO1);
        HobbyDTO updatedHobby1 = facade.getById(hobbyDTO1.getName());
        assertEquals(hobbyDTO1, updatedHobby1);
    }
}