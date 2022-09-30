package datafacades;

import entities.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
        person = new Person(1, "foo@bar.com", "foo", "bar", new Address("foovej 1", "0", "mf", new CityInfo("0231", "foocity")))
        p1 = new Phone("12345678", "mobile", 1);
        p2 = new Phone("87654321", "landline phone", 1);
        p3 = new Phone("32423423", "Telefone", 1);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.persist("12345678", "mobile", 1);
            em.persist("87654321", "landline phone", 1);
            em.persist("32423423", "Telefone", 1);

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
    void getAll() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}
