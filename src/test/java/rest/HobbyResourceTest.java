package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import static io.restassured.RestAssured.given;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class HobbyResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    private static HobbyDTO hobbyDTO1, hobbyDTO2;
    private static Person person1, person2;


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

        Hobby hobby1 = new Hobby("Akrobatik", "https://en.wikipedia.org/wiki/Acrobatics",
                "Generel", "Indendørs");
        Hobby hobby2 = new Hobby("Skuespil", "https://en.wikipedia.org/wiki/Acting",
                "Generel", "Indendørs");
        hobbyDTO1 = new HobbyDTO(hobby1);
        hobbyDTO2 = new HobbyDTO(hobby2);

        CityInfo cityInfo1 = new CityInfo("1000", "København");
        CityInfo cityInfo2 = new CityInfo("4000", "Helsingør");

        person1 = new Person();
        person1.setFirstName("Jens");
        person1.setLastName("Jensen");
        person1.setEmail("Jensen@email.com");
        person1.setAddress(new Address("Møllevej 18", null, null, cityInfo1));
        person1.setPhones(new LinkedHashSet<>(List.of(new Phone("20203040", "mobil", person1))));
        person1.assignHobby(hobby1);
        person2 = new Person();
        person2.setFirstName("Mads");
        person2.setLastName("Madsen");
        person2.setEmail("Madsen@email.com");
        person2.setAddress(new Address("Strandvejen 4", null, null, cityInfo2));
        person2.setPhones(new LinkedHashSet<>(List.of(new Phone("40993040", "mobil", person2))));
        person2.assignHobby(hobby2);


        try {
            em.getTransaction().begin();
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

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/hobby").then().statusCode(200);
    }
    @Test
    public void testLogRequest() {
        System.out.println("Testing logging request details");
        given().log().all()
                .when().get("/hobby")
                .then().statusCode(200);
    }
    @Test
    public void testLogResponse() {
        System.out.println("Testing logging response details");
        given()
                .when().get("/hobby")
                .then().log().body().statusCode(200);
    }

    @Test
    public void testGetById()  {
        given()
                .contentType(ContentType.JSON)
                .get("/hobby/{name}",hobbyDTO1.getName())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name", equalTo(hobbyDTO1.getName()))
                .body("description", equalTo(hobbyDTO1.getDescription()))
                .body("category", equalTo(hobbyDTO1.getCategory()))
                .body("type", equalTo(hobbyDTO1.getType()));
    }

    //TODO: This for later, when we do error handling
//    @Test
//    public void testGetByIdError() {
//        given()
//                .contentType(ContentType.JSON)
//                .get("/hobby/{name}","counting sheeps")
//                .then()
//                .assertThat()
//                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
//                .body("code", equalTo(404))
//                .body("message", equalTo("The Hobby entity with name: 'counting sheeps' was not found"));
//    }

    @Test
    public void testPrintResponse(){
        Response response = given().when().get("/hobby/"+hobbyDTO1.getName());
        ResponseBody body = response.getBody();
        System.out.println(body.prettyPrint());

        response
                .then()
                .assertThat()
                .body("name",equalTo("Akrobatik"));
    }

//    @Test
//    public void exampleJsonPathTest() {
//        Response res = given().get("/parent/"+p1.getId());
//        assertEquals(200, res.getStatusCode());
//        String json = res.asString();
//        JsonPath jsonPath = new JsonPath(json);
//        assertEquals("Henrik", jsonPath.get("name"));
//    }

    @Test
    public void getAllHobbies() throws Exception {
        List<HobbyDTO> hobbyDTOs;

        hobbyDTOs = given()
                .contentType("application/json")
                .when()
                .get("/hobby")
                .then()
                .extract().body().jsonPath().getList("", HobbyDTO.class);

//        HobbyDTO h1DTO = new HobbyDTO(ho);
//        HobbyDTO h2DTO = new HobbyDTO(p2);
        assertThat(hobbyDTOs, containsInAnyOrder(hobbyDTO1, hobbyDTO2));
    }

    @Test
    public void postTest() {
        Hobby h = new Hobby("Skiing", "link to wiki here", "General", "Udendørs");
        HobbyDTO hdto = new HobbyDTO(h);
        String requestBody = GSON.toJson(hdto);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/hobby")
                .then()
                .assertThat()
                .statusCode(200)
                .body("name", notNullValue())
                .body("name", equalTo("Skiing"))
                .body("description", equalTo("link to wiki here"))
                .body("category", equalTo("General"))
                .body("type", equalTo("Udendørs"));
    }

    @Test
    public void updateTest() {
        System.out.println("Start of update test");
        Response response = given().when().get("/hobby/"+hobbyDTO2.getName());
        ResponseBody body = response.getBody();
        System.out.println(body.prettyPrint());

        hobbyDTO2.assignPerson(person2);
        hobbyDTO2.assignPerson(person1);
        hobbyDTO2.setDescription("temporarily removed for update test");
        String requestBody = GSON.toJson(hobbyDTO2);

        given()
                .header("Content-type", ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/hobby/{name}",hobbyDTO2.getName())
                .then()
                .assertThat()
                .statusCode(200)
                .body("name", equalTo(hobbyDTO2.getName()))
                .body("description", equalTo("temporarily removed for update test"));

        System.out.println("After given");
        Response response2 = given().when().get("/hobby/"+hobbyDTO2.getName());
        ResponseBody body2 = response2.getBody();
        System.out.println(body2.prettyPrint());

        given()
                .contentType(ContentType.JSON)
                .get("/hobby/{name}",hobbyDTO2.getName())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("name", equalTo(hobbyDTO2.getName()))
                .body("description", equalTo(hobbyDTO2.getDescription()))
                .body("category", equalTo(hobbyDTO2.getCategory()))
                .body("type", equalTo(hobbyDTO2.getType()))
                .body("people", hasEntry(""+person2.getId(), person2.getFirstName()+" "+person2.getLastName()));
    }

    @Test
    public void testDeleteParent() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("name", hobbyDTO2.getName())
                .delete("/hobby/{name}")
                .then()
                .statusCode(200);
    }

    // More test tools from: https://www.baeldung.com/java-junit-hamcrest-guide

    @Test
    public void testToString(){
        System.out.println("Check if obj.toString() creates the right output");
        String str = hobbyDTO1.toString();
        assertThat(hobbyDTO1,hasToString(str));
    }


}