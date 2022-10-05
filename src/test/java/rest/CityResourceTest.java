package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.AddressDTO;
import dtos.CityInfoDTO;
import dtos.HobbyDTO;
import entities.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class CityResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    private CityInfoDTO cityDTO1, cityDTO2, cityDTO3;
    private AddressDTO addressDTO1, addressDTO2;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }
    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }
    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        CityInfo cityInfo1 = new CityInfo("1000", "København");
        CityInfo cityInfo2 = new CityInfo("4000", "Helsingør");
        CityInfo cityInfo3 = new CityInfo("2860", "Søborg");
        Address address1 = new Address("bargade", "1", "2", cityInfo1);
        Address address2 = new Address("foovej", "2", "1", cityInfo2);

        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.persist(address1);
            em.persist(address2);
            em.persist(cityInfo1);
            em.persist(cityInfo2);
            em.persist(cityInfo3);

            em.getTransaction().commit();
        } finally {
            cityDTO1 = new CityInfoDTO(cityInfo1);
            cityDTO2 = new CityInfoDTO(cityInfo2);
            cityDTO3 = new CityInfoDTO(cityInfo3);
            addressDTO1 = new AddressDTO(address1);
            addressDTO2 = new AddressDTO(address2);
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/city").then().statusCode(200);
    }
    @Test
    public void testLogRequest() {
        System.out.println("Testing logging request details");
        given().log().all()
                .when().get("/city")
                .then().statusCode(200);
    }
    @Test
    public void testLogResponse() {
        System.out.println("Testing logging response details");
        given()
                .when().get("/city")
                .then().log().body().statusCode(200);
    }

    @Test
    public void shouldGetAllCities() throws Exception {
        List<CityInfoDTO> cityDTOs;

        cityDTOs = given()
                .contentType("application/json")
                .when()
                .get("/city")
                .then()
                .extract().body().jsonPath().getList("", CityInfoDTO.class);

//        HobbyDTO h1DTO = new HobbyDTO(ho);
//        HobbyDTO h2DTO = new HobbyDTO(p2);
        assertThat(cityDTOs, containsInAnyOrder(cityDTO1, cityDTO2, cityDTO3));
    }

    @Test
    public void shouldGetAllAddressesInZipcode() throws Exception {
        List<AddressDTO> addressDTOs;
        String zipCode = cityDTO1.getZipCode();

        addressDTOs = given()
                .contentType("application/json")
                .when()
                .get("/city/"+ zipCode+"/addresses")
                .then()
                .extract().body().jsonPath().getList("", AddressDTO.class);

        assertThat(addressDTOs, containsInAnyOrder(addressDTO1));
        assertThat(addressDTOs, not(containsInAnyOrder(addressDTO2)));
    }


    @Test
    void shouldCreateNewCity() {
        CityInfo cityInfo = new CityInfo("2000", "Frederiksberg");
        CityInfoDTO cityInfoDTO = new CityInfoDTO(cityInfo);
        String requestBody = GSON.toJson(cityInfoDTO);
        given()
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/city")
                .then()
                .assertThat()
                .statusCode(200)
                .body("zipCode", equalTo(cityInfo.getZipCode()))
                .body("city", equalTo(cityInfo.getCity()));
    }

    @Test
    void shouldUpdateCity() {
        CityInfo cityInfo = new CityInfo("1000", "Frederiksberg");
        CityInfoDTO cityInfoDTO = new CityInfoDTO(cityInfo);
        String requestBody = GSON.toJson(cityInfoDTO);
        given()
                .contentType(ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .put("/city/"+ cityDTO1.getZipCode())
                .then()
                .assertThat()
                .statusCode(200)
                .body("zipCode", equalTo(cityInfo.getZipCode()))
                .body("city", equalTo(cityInfo.getCity()));
    }

    @Test
    void shouldDeleteCity() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/city/2860") // zipcode of cityInfo3
                .then()
                .assertThat()
                .statusCode(200);
    }
}
