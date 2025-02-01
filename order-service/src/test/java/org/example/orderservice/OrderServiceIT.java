package org.example.orderservice;

import io.restassured.RestAssured;
import org.example.orderservice.dto.OrderRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceIT {

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
    @DisplayName("Should submit order successfully")
    void shouldSubmitOrder() {
        OrderRequest orderRequest = new OrderRequest(
                "Iphone 16",
                BigDecimal.valueOf(1500),
                12);

        given()
                .contentType("application/json")
                .body(orderRequest)
                .when()
                .post("/api/order")
                .then()
                .log().all()
                .statusCode(201)
                .body("skuCode", Matchers.equalTo(orderRequest.skuCode()))
                .body("price", Matchers.equalTo(orderRequest.price().intValue()))
                .body("quantity", Matchers.equalTo(orderRequest.quantity()))
                .body("orderNumber", Matchers.notNullValue());

    }
}
