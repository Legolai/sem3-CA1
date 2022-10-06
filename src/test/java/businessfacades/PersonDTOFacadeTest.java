package businessfacades;

import dtos.PersonDTO;
import entities.*;
import errorhandling.EntityNotFoundException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.LinkedHashSet;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PersonDTOFacadeTest {
    private static EntityManagerFactory emf;
    private static PersonDTOFacade facade;

    PersonDTO personDTO1, personDTO2;


    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonDTOFacade.getInstance(emf);
    }
    @AfterAll
    public static void tearDownClass() {

    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        CityInfo cityInfo1 = new CityInfo("1000", "København");
        CityInfo cityInfo2 = new CityInfo("4000", "Helsingør");

        Address address = new Address("Møllevej 18", null, null, cityInfo1);
        Address address1 = new Address("Strandvejen 4", null, null, cityInfo2);
        Hobby hobby1 = new Hobby("Akrobatik", "https://en.wikipedia.org/wiki/Acrobatics",
                "Generel", "Indendørs");

        Person person1 = new Person();
        person1.setFirstName("Jens");
        person1.setLastName("Jensen");
        person1.setEmail("Jensen@email.com");
        person1.setAddress(address);
        person1.assignPhone(new Phone("40402020", "mobil", person1));
        Person person2 = new Person();
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
            em.persist(hobby1);
            em.persist(cityInfo1);
            em.persist(cityInfo2);
            em.persist(person1);
            em.persist(person2);

            em.getTransaction().commit();
        } finally {
            personDTO1 = new PersonDTO(person1);
            personDTO2 = new PersonDTO(person2);
            em.close();
        }
    }
    @AfterEach
    public void tearDown() {
        //        Remove any data after each test was run
    }

    @Test
    void ShouldGetAll() {
        List<PersonDTO> list = facade.getAll();
        assertEquals(2, list.size());
    }


    @Test
    void ShouldGetAllByZipCode() {
        List<PersonDTO> list = facade.getAllByZipCode("4000");
        assertEquals(1, list.size());
        assertEquals(personDTO2, list.get(0));
    }

    @Test
    void ShouldGetById() throws errorhandling.EntityNotFoundException {
        PersonDTO personDTO = facade.getById(personDTO1.getId());
        assertEquals(personDTO1, personDTO);
    }

    @Test
    void ShouldGetByPhoneNumber() throws errorhandling.EntityNotFoundException {
        PersonDTO personDTO = facade.getByPhoneNumber(personDTO1.getPhones().get(0).getNumber());
        assertEquals(personDTO1, personDTO);
    }

    @Test
    void ShouldCreateANewPerson() {
        PersonDTO personDTO = new PersonDTO(new Person("hansen@email.com", "hans", "hansen", new Address("peterbangsvej 12", null, null, new CityInfo("1000", "København")), new LinkedHashSet<>(), new LinkedHashSet<>()));
        assertDoesNotThrow(() -> facade.create(personDTO));
        assertEquals(3, facade.getAll().size());
    }

    @Test
    void ShouldUpdatePerson() throws errorhandling.EntityNotFoundException {
        CityInfo cityInfo1 = new CityInfo("1000", "København");

        Person person = new Person();
        person.setFirstName("Jens");
        person.setLastName("Jensen");
        person.setEmail("Jensen@email.com");
        person.setAddress(new Address("Møllevej 18", null, null, cityInfo1));
        person.assignPhone(new Phone("20203049", "mobil", person));
        person.setLastName("blank");
        PersonDTO personDTO = facade.create(new PersonDTO(person));
        Hobby hobby = new Hobby("Akrobatik", "https://en.wikipedia.org/wiki/Acrobatics", "Generel", "Indendørs");
        person = personDTO.getEntity();
        person.assignHobby(hobby);
        PersonDTO updated = facade.update(new PersonDTO(person));
        assertNotEquals(personDTO.getHobbies().size(),updated.getHobbies().size());
    }

    @Test
    void ShouldDeletePerson() {
        assertThrows(EntityNotFoundException.class, () -> facade.delete(10));
        assertDoesNotThrow(() -> facade.delete(personDTO1.getId()));
    }
}
