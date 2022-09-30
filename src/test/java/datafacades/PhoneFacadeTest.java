package datafacades;

import entities.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PhoneFacadeTest {

    private static EntityManagerFactory emf;
    private static PhoneFacade facade;
    Person person;
    Phone p1, p2, p3;


    @BeforeAll
    public static void setUpBeforeAll() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PhoneFacade.getInstance(emf);
    }

    @BeforeEach
    void setUp() {
        EntityManager em = emf.createEntityManager();
        CityInfo cityInfo = new CityInfo("1234", "foo");
        Address address = new Address("foovej 1", "0", "mf", cityInfo);
        person = new Person(
                "foo@bar.com",
                "foo",
                "bar",
                address,
                new LinkedHashSet<Phone>(),
                new LinkedHashSet<Hobby>()
                );
        p1 = new Phone("12345678", "mobile", person);
        p2 = new Phone("87654321", "landline phone", person);
        p3 = new Phone("32423423", "Telefon", person);
        person.assignPhone(p1);
        person.assignPhone(p2);
        person.assignPhone(p3);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.persist(cityInfo);
            em.persist(person);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {

    }

    @Test
    void getById() {
    }

    @Test
    void shouldGetAllPhones() {
        System.out.println("getting all phones");
        int actual =  facade.getAll().size();
        int expected = 3;
        System.out.println("comparing actual: " + actual + " with expected: " +expected);
        assertEquals(expected, actual);
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}
