package datafacades;

import entities.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;

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
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
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
    void shouldCreateNewPhone() {
        System.out.println("creating new phone");
        Phone newPhone = new Phone("12121212", "landline", person);
        Phone actual = facade.create(newPhone);
        Phone expected = facade.getById("12121212");
        System.out.println("comparing actual: " + actual + " with expected: " + expected);
        assertEquals(expected, actual);
    }

    @Test
    void shouldGetPhoneByNumber() {
        System.out.println("getting phone by number");
        Phone actual = facade.getById(p1.getNumber());
        Phone expected = p1;
        System.out.println("comparing actual: " + actual + " with expected: " + expected);
        assertEquals(expected, actual);
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
    void shouldUpdateDescriptionForPhone() {
        System.out.println("updating phone p1");
        Phone updatedPhone = p1;
        updatedPhone.setDescription("new type of phone");
        Phone actual = facade.update(updatedPhone);
        Phone expected = facade.getById(p1.getNumber());
        System.out.println("comparing actual: " + actual + " with expected: " + expected);
        assertEquals(expected, actual);
    }

    @Test
    void shouldDeletePhone() {
        System.out.println("deleting phone");
        Phone toBeDeleted = p1;
        facade.delete(toBeDeleted.getNumber());
        assertThrows(EntityNotFoundException.class, () -> facade.getById(toBeDeleted.getNumber()));
    }
}
