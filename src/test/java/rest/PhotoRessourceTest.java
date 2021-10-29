package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PhotoDTO;
import entities.Photo;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import entities.Tag;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;

import java.util.List;
import javax.persistence.EntityManager;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeAll;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PhotoRessourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Photo p1, p2;
    private static Tag c1, c2;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

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
        p1 = new Photo("Henrik",76, "Some text");
        p2 = new Photo("Betty",76, "Some other thext");
        c1 = new Tag("Joseph");
        c2 = new Tag("Alberta");


        p1.addTag(c1);
        p1.addTag(c2);


        try {
            em.getTransaction().begin();
            em.createNamedQuery("Tag.deleteAllRows").executeUpdate();
            em.createNamedQuery("Photo.deleteAllRows").executeUpdate();
            em.persist(c1);
            em.persist(c2);
            em.persist(p1);
            em.persist(p2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/Photo").then().statusCode(200);
    }

    @Test
    public void testGetById()  {
        given()
                .contentType(ContentType.JSON)
//                .pathParam("id", p1.getId()).when()
                .get("/Photo/{id}",p1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(p1.getId()))
                .body("name", equalTo(p1.getFileName()))
                .body("tags", hasItems(hasEntry("name","Joseph"),hasEntry("name","Alberta")));
    }

    @Test
    public void testError() {
        given()
                .contentType(ContentType.JSON)
//                .pathParam("id", p1.getId()).when()
                .get("/Photo/{id}",999999999)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("code", equalTo(404))
                .body("message", equalTo("The Photo entity with ID: 999999999 Was not found"));
    }

    @Test
    public void testPrintResponse(){
        Response response = given().when().get("/Photo/"+p1.getId());
        ResponseBody body = response.getBody();
        System.out.println(body.prettyPrint());

        response
                .then()
                .assertThat()
                .body("name",equalTo("Henrik"));
    }

    @Test
    public void exampleJsonPathTest() {
        Response res = given().get("/Photo/"+p1.getId());
        assertEquals(200, res.getStatusCode());
        String json = res.asString();
        JsonPath jsonPath = new JsonPath(json);
        assertEquals("Henrik", jsonPath.get("name"));
    }

    @Test
    public void getAllPhotos() throws Exception {
        List<PhotoDTO> PhotoDTOs;

        PhotoDTOs = given()
                .contentType("application/json")
                .when()
                .get("/Photo")
                .then()
                .extract().body().jsonPath().getList("", PhotoDTO.class);

        PhotoDTO p1DTO = new PhotoDTO(p1);
        PhotoDTO p2DTO = new PhotoDTO(p2);
        assertThat(PhotoDTOs, containsInAnyOrder(p1DTO, p2DTO));

    }


    @Test
    public void postTest() {
        Photo p = new Photo("Helge",45,"TEXTEXT");
        p.addTag(new Tag("Josephine"));
        PhotoDTO pdto = new PhotoDTO(p);
        String requestBody = GSON.toJson(pdto);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/Photo")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo("Helge"))
                .body("tags", hasItems(hasEntry("name","Josephine")));
    }

    @Test
    public void updateTest() {
        p2.addTag(c2);
        p2.setYear(23);
        PhotoDTO pdto = new PhotoDTO(p2);
        String requestBody = GSON.toJson(pdto);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .put("/Photo/"+p2.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(p2.getId()))
                .body("name", equalTo("Betty"))
                .body("year", equalTo(23))
                .body("tags", hasItems(hasEntry("name","Alberta")));
    }

    @Test
    public void testDeletePhoto() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", p2.getId())
                .delete("/Photo/{id}")
                .then()
                .statusCode(200)
                .body("id",equalTo(p2.getId()));
    }
}

