package dat.controllers;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.config.Populator;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TripControllerTest {

    private Javalin app;
    private static final EntityManagerFactory emfTest = HibernateConfig.getEntityManagerFactoryForTest();
    private int port = 9090;
    private static String adminToken;
    private static String userToken;

    @BeforeAll
    void setup() {
        app = ApplicationConfig.startServer(port);
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        // Set up test users and tokens
        LoginUtil.createTestUsers(emfTest);
        adminToken = LoginUtil.getAdminToken();
        userToken = LoginUtil.getUserToken();

        // Populate initial data
        new Populator(emfTest).populateData();
    }

    @AfterAll
    void tearDown() {
        ApplicationConfig.stopServer(app);
        if (emfTest != null) {
            emfTest.close();
        }
    }

    @Test
    void testGetAllTrips() {
        Response response = given()
                .header("Authorization", userToken)
                .when()
                .get("/api/trips")
                .then()
                .statusCode(200)
                .extract()
                .response();

        assertNotNull(response.getBody());
        assertTrue(response.getBody().jsonPath().getList("$").size() >= 0);
    }

    @Test
    void testGetTripById() {
        int tripId = 1;  // ID of "Beach Adventure" in Populator data

        given()
                .header("Authorization", userToken)
                .when()
                .get("/api/trips/" + tripId)
                .then()
                .statusCode(200)
                .body("trip.id", equalTo(tripId))
                .body("trip.name", equalTo("Beach Adventure"))
                .body("trip.price", equalTo(200.0f))  // Ensure price is matched as a float
                .body("trip.category", equalTo("BEACH"))
                .body("guide.id", notNullValue())
                .body("guide.firstname", equalTo("John"))
                .body("guide.lastname", equalTo("Doe"))
                .body("guide.email", equalTo("john@example.com"));
    }

    @Test
    void testCreateTrip() {
        given()
                .header("Authorization", adminToken)
                .contentType("application/json")
                .body("{ \"name\": \"Mountain Hike\", \"starttime\": \"2024-01-01T10:00:00\", \"endtime\": \"2024-01-02T18:00:00\", \"longitude\": 34.0522, \"latitude\": -118.2437, \"price\": 120.0, \"category\": \"FOREST\" }")
                .when()
                .post("/api/trips")
                .then()
                .statusCode(201)
                .body("name", equalTo("Mountain Hike"))
                .body("category", equalTo("FOREST"));
    }

    @Test
    void testUpdateTrip() {
        String updatedTripJson = "{\"name\": \"Updated City Tour\", \"starttime\": \"2024-12-01T09:00:00\", \"endtime\": \"2024-12-01T18:00:00\", \"longitude\": -74.0060, \"latitude\": 40.7128, \"price\": 180.0, \"category\": \"CITY\"}";

        given()
                .header("Authorization", adminToken)
                .contentType("application/json")
                .body(updatedTripJson)
                .when()
                .put("/api/trips/2")
                .then()
                .statusCode(200)
                .body("name", equalTo("Updated City Tour"));
    }

    @Test
    void testDeleteTrip() {
        given()
                .header("Authorization", adminToken)
                .when()
                .delete("/api/trips/3")
                .then()
                .statusCode(204);
    }

    @Test
    void testGetTripsByCategory() {
        given()
                .header("Authorization", userToken)
                .contentType("application/json")
                .when()
                .get("/api/trips/category/BEACH")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].category", equalTo("BEACH"));
    }

    @Test
    void testGetTotalPriceByGuide() {
        Response response = given()
                .header("Authorization", userToken)
                .when()
                .get("/api/trips/guides/totalprice")
                .then()
                .statusCode(200)
                .extract()
                .response();

        assertNotNull(response.getBody());
        assertTrue(response.getBody().jsonPath().getList("$").size() >= 0);
    }

    @Test
    void testAddGuideToTrip() {
        given()
                .header("Authorization", adminToken)
                .contentType("application/json")
                .when()
                .put("/api/trips/1/guides/1")
                .then()
                .statusCode(204);
    }

    @Test
    void testPopulateTrips() {
        given()
                .header("Authorization", adminToken)
                .when()
                .post("/api/trips/populate")
                .then()
                .statusCode(201)
                .contentType("text/plain")  // Ensure content type matches response
                .body(equalTo("Database populated with trips and guides"));
    }

    @Test
    void testGetTotalPackingWeight() {
        int tripId = 4;  // Ensure this matches an actual populated trip ID

        given()
                .header("Authorization", userToken) // Add Authorization header if required
                .when()
                .get("/api/trips/" + tripId + "/packing-weight")
                .then()
                .statusCode(200)
                .body("tripId", equalTo(tripId))
                .body("totalPackingWeight", notNullValue()); // Only check for presence
    }

}
