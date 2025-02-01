package org.example.inventoryservice;

import io.restassured.RestAssured;
import net.bytebuddy.asm.Advice;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceIT {

    @ServiceConnection
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.3.0");
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @BeforeAll
    static void beforeAll() {
        mySQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        mySQLContainer.stop();
    }

    @Test
    @DisplayName("Should check if product is in stock correctly")
    void shouldCheckIfInStock() {
        String skuCode = "Iphone 16";
        Integer quantity = 12;

        given()
                .contentType("application/json")
                .queryParam("skuCode", skuCode)
                .queryParam("quantity", quantity)
                .when()
                .get("/api/inventory")
                .then()
                .statusCode(200)
                .body(Matchers.equalToIgnoringCase("false"));
    }

}
