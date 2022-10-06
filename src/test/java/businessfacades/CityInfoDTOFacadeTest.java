package businessfacades;

import datafacades.PersonFacade;
import dtos.CityInfoDTO;
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

public class CityInfoDTOFacadeTest {
    private static EntityManagerFactory emf;
    private static CityInfoDTOFacade facade;
    private CityInfo cityInfo1, cityInfo2;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = CityInfoDTOFacade.getInstance(emf);
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

        cityInfo1 = new CityInfo("1000", "København");
        cityInfo2 = new CityInfo("4000", "Helsingør");

        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();

            em.persist(cityInfo1);
            em.persist(cityInfo2);

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
    void testShouldCreateCityInfo() {
        facade.create(new CityInfoDTO("3550", "Slangerup"));
        assertEquals(3, facade.getAll().size());
        CityInfoDTO cityInfoDTO = new CityInfoDTO("3550", "Slangerup");
        assertThrows(IllegalStateException.class, () -> facade.create(cityInfoDTO));
    }

    @Test
    void testShouldDeleteCityInfo() {
        assertThrows(EntityNotFoundException.class, () -> facade.delete("3550"));
        assertDoesNotThrow(() -> facade.delete("4000"));
        assertEquals(1, facade.getAll().size());
    }

    @Test
    void testShouldGetCityInfoByZipCode() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> facade.getById("3550"));
        assertDoesNotThrow(() -> facade.getById("4000"));
        CityInfoDTO cityInfoDTO = facade.getById("4000");
        assertEquals(cityInfo2, cityInfoDTO.getEntity());
    }

    @Test
    void testShouldUpdateCityInfo() throws EntityNotFoundException {
        CityInfoDTO tmp = new CityInfoDTO("3550", "Slangerup");
        assertThrows(EntityNotFoundException.class, () -> facade.update(tmp));
        assertDoesNotThrow(() -> facade.update(new CityInfoDTO("4000", "København")));
        CityInfoDTO cityInfoDTO = facade.getById("4000");
        assertNotEquals(cityInfo2.getCity(), cityInfoDTO.getCity());
    }
}
