package datafacades;

import entities.Hobby;
import errorhandling.EntityNotFoundException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HobbyFacadeTest {
    private static EntityManagerFactory emf;
    private static HobbyFacade facade;

    Hobby hobby1, hobby2;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = HobbyFacade.getInstance(emf);
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
        hobby1 = new Hobby();
        hobby1.setName("Akrobatik");
        hobby1.setDescription("https://en.wikipedia.org/wiki/Acrobatics");
        hobby1.setCategory("Generel");
        hobby1.setType("Indendørs");
        hobby2 = new Hobby();
        hobby2.setName("Skuespil");
        hobby2.setDescription("https://en.wikipedia.org/wiki/Acting");
        hobby2.setCategory("Generel");
        hobby2.setType("Indendørs");

        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.persist(hobby1);
            em.persist(hobby2);

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
        assertDoesNotThrow(() -> facade.create("Amatørradio","https://en.wikipedia.org/wiki/Amateur_radio","Generel","Indendørs"));
        assertEquals(3, facade.getAll().size());
        assertThrows(IllegalStateException.class, () -> facade.create("Amatørradio","https://en.wikipedia.org/wiki/Amateur_radio","Generel","Indendørs"));
    }

    @Test
    void testShouldGetHobbyByName() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> facade.getById("Amatørradio"));
        Hobby hobby = facade.getById("Akrobatik");
        assertEquals(hobby1, hobby);
    }

    @Test
    void testShouldDeleteHobby1() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> facade.delete("Amatørradio"));
//        facade.delete(hobby1.getName());
//        List<Hobby> hobbies = facade.getAll();
//        assertEquals(1, hobbies.size());
//        assertEquals(hobby2, hobbies.get(0));
    }

    @Test
    void testShouldUpdateHobby1() throws EntityNotFoundException {
        Hobby hobby = new Hobby();
        hobby.setName("Amatørradio");
        hobby.setDescription("https://en.wikipedia.org/wiki/Amateur_radio");
        hobby.setCategory("Generel");
        hobby.setType("Indendørs");
        assertThrows(EntityNotFoundException.class, () -> facade.update(hobby));
        hobby1.setType("Udendørs");
        facade.update(hobby1);
        Hobby updatedHobby1 = facade.getById(hobby1.getName());
        assertEquals(hobby1, updatedHobby1);
    }
}