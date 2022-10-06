package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.HobbyDTO;
import dtos.PersonDTO;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    private PersonDTO personDTO1, personDTO2;
    private HobbyDTO hobbyDTO1, hobbyDTO2;
    private Person person1, person2;


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

        CityInfo cityInfo1 = new CityInfo("1000", "København");
        CityInfo cityInfo2 = new CityInfo("4000", "Helsingør");
        CityInfo cityInfo3 = new CityInfo("2610", "Rødovre");

        person1 = new Person();
        person1.setFirstName("Jens");
        person1.setLastName("Jensen");
        person1.setEmail("Jensen@email.com");
        person1.setAddress(new Address("Møllevej 18", null, null, cityInfo1));
        person1.assignPhone(new Phone("20203040", "mobil", person1));
        person1.assignHobby(hobby1);

        person2 = new Person();
        person2.setFirstName("Mads");
        person2.setLastName("Madsen");
        person2.setEmail("Madsen@email.com");
        person2.setAddress(new Address("Strandvejen 4", null, null, cityInfo2));
        person2.assignPhone(new Phone("40993040", "mobil", person2));
        person2.assignHobby(hobby2);

        hobbyDTO1 = new HobbyDTO(hobby1);
        hobbyDTO2 = new HobbyDTO(hobby2);

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
            em.persist(cityInfo3);
            em.persist(person1);
            em.persist(person2);

            em.getTransaction().commit();
        } finally {
            hobbyDTO1.assignPerson(person1);
            hobbyDTO2.assignPerson(person2);
            personDTO1 = new PersonDTO(person1);
            personDTO2 = new PersonDTO(person2);
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/person").then().statusCode(200);
    }
    @Test
    public void testLogRequest() {
        System.out.println("Testing logging request details");
        given().log().all()
                .when().get("/person")
                .then().statusCode(200);
    }
    @Test
    public void testLogResponse() {
        System.out.println("Testing logging response details");
        given()
                .when().get("/person")
                .then().log().body().statusCode(200);
    }

    @Test
    public void testGetById()  {
        given()
                .contentType(ContentType.JSON)
                .get("/person/{id}",personDTO1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", equalTo(personDTO1.getFirstName()))
                .body("lastName", equalTo(personDTO1.getLastName()))
                .body("email", equalTo(personDTO1.getEmail()))
                .body("phones", hasItem(hasEntry("number", personDTO1.getPhones().get(0).getNumber())))
                .body("address", hasEntry("id", personDTO1.getAddress().getId()))
                .body("hobbies", hasItem(hasEntry("name", personDTO1.getHobbies().get(0).getName())));
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
        Response response = given().when().get("/person/"+personDTO1.getId());
        ResponseBody body = response.getBody();
        System.out.println(body.prettyPrint());

        response
                .then()
                .assertThat()
                .body("firstName",equalTo("Jens"));
    }

    @Test
    public void getAllPersons() throws Exception {
        List<PersonDTO> personDTOS;

        personDTOS = given()
                .contentType("application/json")
                .when()
                .get("/person")
                .then()
                .extract().body().jsonPath().getList("", PersonDTO.class);

//        HobbyDTO h1DTO = new HobbyDTO(ho);
//        HobbyDTO h2DTO = new HobbyDTO(p2);
        assertThat(personDTOS, containsInAnyOrder(personDTO1, personDTO2));
    }

    @Test
    public void postTest() {
        Person p = new Person();
        p.setFirstName("Michael");
        p.setLastName("Xu");
        p.setEmail("michael@mail.com");
        p.setAddress(new Address("Kaffevej 47", "4. sal", "tv.", new CityInfo("2610", "Rødovre")));
        p.assignPhone(new Phone("60608080", "mobil", p));
        p.assignHobby(hobbyDTO1.getEntity());

        PersonDTO pdto = new PersonDTO(p);
        String requestBody = GSON.toJson(pdto);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/person")
                .then()
                .assertThat()
                .statusCode(200)
                .body("firstName", equalTo("Michael"))
                .body("lastName", equalTo("Xu"))
                .body("email", equalTo("michael@mail.com"))
                .body("phones", contains(hasEntry("number", "60608080")))
                .body("address", hasEntry("street", "Kaffevej 47"))
                .body("hobbies", contains(hasEntry("name", hobbyDTO1.getName())));
//
    }

    @Test
    public void updateTest() {
        System.out.println("Start of update test");
        Person person = new Person();
        person.setFirstName(person2.getFirstName());
        person.setLastName(person2.getLastName());
        person.setEmail(person2.getEmail());
        person.setAddress(person2.getAddress());
//        person.setHobbies(person2.getHobbies());

        String createRequestBody = GSON.toJson(new PersonDTO(person));

        PersonDTO created = given()
                .header("Content-type", ContentType.JSON)
                .body(createRequestBody)
                .when().post("/person").andReturn().as(PersonDTO.class);

        person.setId(created.getId());
        person.setLastName("blank");
        person.assignPhone(new Phone("44204420", "fastnet", null));
        person.assignHobby(hobbyDTO1.getEntity());
        created = new PersonDTO(person);
        String requestBody = GSON.toJson(created);

        given()
                .header("Content-type", ContentType.JSON)
                .body(requestBody)
                .when()
                .pathParam("id", created.getId())
                .put("/person/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("firstName", equalTo(created.getFirstName()))
                .body("lastName", equalTo("blank"))
                .body("email", equalTo(created.getEmail()))
                .body("phones", hasItem(hasEntry("number", created.getPhones().stream().findFirst().get().getNumber())))
                .body("hobbies", hasItem(hasEntry("name", created.getHobbies().stream().findFirst().get().getName())));

    }

    @Test
    public void testDeletePerson() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", personDTO2.getId())
                .delete("/person/{id}")
                .then()
                .statusCode(200);
    }

    // More test tools from: https://www.baeldung.com/java-junit-hamcrest-guide

    @Test
    public void testToString() {
        System.out.println("Check if obj.toString() creates the right output");
        String str = personDTO1.toString();
        assertThat(personDTO1,hasToString(str));
    }

    @Test
    public void testGetByPhoneNumber()  {
        given()
                .contentType(ContentType.JSON)
                .get("/person/phone/{number}",personDTO1.getPhones().get(0).getNumber())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", equalTo(personDTO1.getFirstName()))
                .body("lastName", equalTo(personDTO1.getLastName()))
                .body("email", equalTo(personDTO1.getEmail()))
                .body("phones", hasItem(hasEntry("number", personDTO1.getPhones().get(0).getNumber())))
                .body("address", hasEntry("id", personDTO1.getAddress().getId()))
                .body("hobbies", hasItem(hasEntry("name", personDTO1.getHobbies().get(0).getName())));
    }

    @Test
    public void getAllByZipCode() throws Exception {
        List<PersonDTO> personDTOS;

        personDTOS = given()
                .contentType("application/json")
                .when()
                .get("/person/city/{zipCode}", "1000")
                .then()
                .extract().body().jsonPath().getList("", PersonDTO.class);

        //        HobbyDTO h1DTO = new HobbyDTO(ho);
        //        HobbyDTO h2DTO = new HobbyDTO(p2);
        assertThat(personDTOS, containsInAnyOrder(personDTO1));
        System.out.println(personDTOS);
    }


}