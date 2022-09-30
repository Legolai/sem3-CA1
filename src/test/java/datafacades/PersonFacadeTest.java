package datafacades;

import entities.*;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;

    Person person1, person2;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getInstance(emf);
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

        Hobby hobby1 = new Hobby();
        hobby1.setName("Akrobatik");
        hobby1.setDescription("https://en.wikipedia.org/wiki/Acrobatics");
        hobby1.setCategory("Generel");
        hobby1.setType("Indendørs");
        Hobby hobby2 = new Hobby();
        hobby2.setName("Skuespil");
        hobby2.setDescription("https://en.wikipedia.org/wiki/Acting");
        hobby2.setCategory("Generel");
        hobby2.setType("Indendørs");

        CityInfo cityInfo1 = new CityInfo("1000", "København");
        CityInfo cityInfo2 = new CityInfo("4000", "Helsingør");

        person1 = new Person();
        person1.setFirstName("Jens");
        person1.setLastName("Jensen");
        person1.setEmail("Jensen@email.com");
        person1.setAddress(new Address("Møllevej 18", null, null, cityInfo1));
        person1.setPhones(new LinkedHashSet<>(List.of(new Phone("20203040", "mobil", person1))));
        person1.assignHobby(hobby1);
        person1.setHobbies(new LinkedHashSet<>(List.of(hobby1)));
        person2 = new Person();
        person2.setFirstName("Mads");
        person2.setLastName("Madsen");
        person2.setEmail("Madsen@email.com");
        person2.setAddress(new Address("Strandvejen 4", null, null, cityInfo2));
        person2.setPhones(new LinkedHashSet<>(List.of(new Phone("40993040", "mobil", person1))));
        person2.assignHobby(hobby2);

        try {
            em.getTransaction().begin();
//            em.createQuery("DELETE from Person_has_Hobby").executeUpdate();
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


    @Test
    void testShouldGetNumberOfPersonsExisting() throws Exception {
        assertEquals(2, facade.getAll().size(), "Expects two rows in the database");
    }

    @Test
    void testShouldGetPersonByPhoneNumber() {
        Person person = facade.getByPhoneNumber(person1.getPhones().stream().findFirst().get().getNumber());
        assertEquals(person1, person);
    }

    @Test
    void testShouldGetPersonsByCityInfo() {
        List<Person> persons = facade.getAllByCityInfo(new CityInfo("4000", "Helsingør"));
        assertEquals(1, persons.size());
        assertEquals(person2, persons.get(0));
    }

    @Test
    void testShouldCreatePersonAndHobbiesByNames() {
        Phone phone = new Phone();
        phone.setNumber("40993550");
        phone.setDescription("mobil");
        Person person = facade.create("Hansen@email.com", "Hans", "Hansen", new Address("Strandvejen 42", null, null, new CityInfo("4000", "Helsingør")), Set.of(phone), List.of("Skuespil", "Akrobatik"));
    }
}