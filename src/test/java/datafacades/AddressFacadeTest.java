package datafacades;

import entities.Address;
import entities.CityInfo;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

class AddressFacadeTest {

    private static EntityManagerFactory emf;
    private static AddressFacade facade;
    Address a1, a2;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = AddressFacade.getInstance(emf);
    }
    @AfterAll
    public static void tearDownClass() {
        // Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    @BeforeEach
    void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            CityInfo ci1 = new CityInfo("2860","Soeborg");
            CityInfo ci2 = new CityInfo("2610","Roedovre");
            CityInfo ci3 = new CityInfo("2900","Hellerup");
            em.persist(ci1);
            em.persist(ci2);
            em.persist(ci3);
            a1 = new Address("Mørkhøj Parkalle 12d", null, null, ci1);
            a2 = new Address("Kaffevej 47", "4", "tv", ci2);
            em.persist(a1);
            em.persist(a2);
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
        System.out.println("Testing create(Address address)");
        Address address = new Address("Tuborgvej 122", null, null, new CityInfo("2900","Hellerup"));
        Address expected = address;
        Address actual = facade.create(address);
        assertEquals(expected, actual);
    }

    @Test
    void testCreate() {
        System.out.println("Testing create(String street, String floor, String door, CityInfo cityinfoZipcode)");
        Address expected = new Address("Tuborgvej 122", null, null, new CityInfo("2900","Hellerup"));
        Address actual = facade.create("Tuborgvej 122", null, null, new CityInfo("2900","Hellerup"));
        assertEquals(expected.getStreet(), actual.getStreet());
        assertEquals(expected.getFloor(), actual.getFloor());
        assertEquals(expected.getDoor(), actual.getDoor());
        assertEquals(expected.getCityInfo(), actual.getCityInfo());
    }

    @Test
    void getById() {
        System.out.println("Testing getbyid(id)");
        Address expected = a1;
        Address actual = facade.getById(a1.getId());
        assertEquals(expected, actual);
    }

    @Test
    void getAll() {
        System.out.println("Testing getAll()");
        int expected = 2;
        int actual = facade.getAll().size();
        assertEquals(expected,actual);
    }

    @Test
    void update() {
        System.out.println("Testing Update(Address address)");
        a2.setDoor("updated city");
        Address expected = a2;
        Address actual = facade.update(a2);
        assertEquals(expected,actual);
    }

    @Test
    void delete() {
        System.out.println("Testing delete(id)");
        facade.delete(a2.getId());
        int expected = 1;
        int actual = facade.getAll().size();
        assertEquals(expected, actual);
    }
}