package datafacades;

import entities.CityInfo;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

class CityInfoFacadeTest {

    private static EntityManagerFactory emf;
    private static CityInfoFacade facade;
    CityInfo ci1, ci2, ci3;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = CityInfoFacade.getInstance(emf);
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
            ci1 = new CityInfo("0877", "København C");
            ci2 = new CityInfo("0800","Høje Taastrup");
            ci3 = new CityInfo("0899","Kommuneservice");
            em.persist(ci1);
            em.persist(ci2);
            em.persist(ci3);
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
        System.out.println("Testing create(CityInfo cityInfo)");
        CityInfo cityInfo = new CityInfo("4030","Tune");
        CityInfo expected = cityInfo;
        CityInfo actual = facade.create(cityInfo);
        assertEquals(expected, actual);
    }

    @Test
    void testCreateWithParameters() {
        System.out.println("Testing create(String zipCode, String city)");
        CityInfo expected = new CityInfo("4030","Tune");
        CityInfo actual = facade.create("4030","Tune");
        assertEquals(expected, actual);
    }

    @Test
    void getById() {
        System.out.println("Testing getbyid(id)");
        CityInfo expected = ci1;
        CityInfo actual = facade.getById(ci1.getZipCode());
        assertEquals(expected, actual);
    }

    @Test
    void getAll() {
        System.out.println("Testing getAll()");
        int expected = 3;
        int actual = facade.getAll().size();
        assertEquals(expected,actual);
    }

    @Test
    void update() {
        System.out.println("Testing Update(CityInfo cityInfo)");
        ci2.setCity("updated city");
        CityInfo expected = ci2;
        CityInfo actual = facade.update(ci2);
        assertEquals(expected,actual);
    }

    @Test
    void delete() {
        System.out.println("Testing delete(id)");
        facade.delete(ci3.getZipCode());
        int expected = 2;
        int actual = facade.getAll().size();
        assertEquals(expected, actual);
    }
}