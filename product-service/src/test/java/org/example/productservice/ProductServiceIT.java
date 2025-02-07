//package org.example.productservice;
//
//import io.restassured.RestAssured;
//import io.restassured.http.ContentType;
//import org.example.productservice.dto.product.ProductDto;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.*;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
//import org.springframework.test.annotation.DirtiesContext;
//import org.testcontainers.containers.MongoDBContainer;
//
//import java.math.BigDecimal;
//
//import static io.restassured.RestAssured.given;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
//class ProductServiceIT {
//
//    @ServiceConnection
//    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5")
//            .withReuse(true);
//
//    @LocalServerPort
//    private Integer port;
//
//    @BeforeEach
//    void setUp() {
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = port;
//    }
//
//    @BeforeAll
//    static void beforeAll() {
//        mongoDBContainer.start();
//    }
//
//    @AfterAll
//    static void afterAll() {
//        mongoDBContainer.stop();
//    }
//
//
//    @Test
//    @DisplayName("Should create product successfully")
//    void shouldCreateProduct() {
//        ProductDto productDto = new ProductDto(
//                "Laptop",
//                "High end laptop",
//                BigDecimal.valueOf(6299));
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(productDto)
//                .when()
//                .post()
//                .then()
//                .statusCode(201)
//                .body("id", Matchers.notNullValue())
//                .body("name", Matchers.equalTo(productDto.name()))
//                .body("description", Matchers.equalTo(productDto.description()));
//    }
//}
