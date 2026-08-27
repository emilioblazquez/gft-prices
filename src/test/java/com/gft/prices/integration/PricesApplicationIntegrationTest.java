package com.gft.prices.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PricesApplicationIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void shouldReturnPriceList1At10OnJune14() {
        given()
                .queryParam("applicationDate", "2020-06-14T10:00:00Z")
                .queryParam("productId", 35455)
                .queryParam("brandId", 1)

                .when()
                .get("/prices")

                .then()
                .statusCode(200)
                .body("productId", equalTo(35455))
                .body("brandId", equalTo(1))
                .body("priceList", equalTo(1))
                .body("price", equalTo(35.5F))
                .body("currency", equalTo("EUR"));
    }

    @Test
    void shouldReturnPriceList2At16OnJune14() {
        given()
                .queryParam("applicationDate", "2020-06-14T16:00:00Z")
                .queryParam("productId", 35455)
                .queryParam("brandId", 1)

                .when()
                .get("/prices")

                .then()
                .statusCode(200)
                .body("productId", equalTo(35455))
                .body("brandId", equalTo(1))
                .body("priceList", equalTo(2))
                .body("price", equalTo(25.45F))
                .body("currency", equalTo("EUR"));
    }

    @Test
    void shouldReturnPriceList1At21OnJune14() {
        given()
                .queryParam("applicationDate", "2020-06-14T21:00:00Z")
                .queryParam("productId", 35455)
                .queryParam("brandId", 1)

                .when()
                .get("/prices")

                .then()
                .statusCode(200)
                .body("productId", equalTo(35455))
                .body("brandId", equalTo(1))
                .body("priceList", equalTo(1))
                .body("price", equalTo(35.5F))
                .body("currency", equalTo("EUR"));
    }

    @Test
    void shouldReturnPriceList3At10OnJune15() {
        given()
                .queryParam("applicationDate", "2020-06-15T10:00:00Z")
                .queryParam("productId", 35455)
                .queryParam("brandId", 1)

                .when()
                .get("/prices")

                .then()
                .statusCode(200)
                .body("productId", equalTo(35455))
                .body("brandId", equalTo(1))
                .body("priceList", equalTo(3))
                .body("price", equalTo(30.5F))
                .body("currency", equalTo("EUR"));
    }

    @Test
    void shouldReturnPriceList4At21OnJune16() {
        given()
                .queryParam("applicationDate", "2020-06-16T21:00:00Z")
                .queryParam("productId", 35455)
                .queryParam("brandId", 1)

                .when()
                .get("/prices")

                .then()
                .statusCode(200)
                .body("productId", equalTo(35455))
                .body("brandId", equalTo(1))
                .body("priceList", equalTo(4))
                .body("price", equalTo(38.95F))
                .body("currency", equalTo("EUR"));
    }

    @Test
    void shouldReturn404WhenPriceDoesNotExist() {
        given()
                .queryParam("applicationDate", "2020-06-14T10:00:00Z")
                .queryParam("productId", 99999)
                .queryParam("brandId", 1)

                .when()
                .get("/prices")

                .then()
                .statusCode(404)
                .body("title", equalTo("Price not found"))
                .body("status", equalTo(404))
                .body("detail", equalTo(
                        "No applicable price found for brandId=1, productId=99999 and applicationDate=2020-06-14T10:00"))
                .body("instance", equalTo("/prices"));
    }

    @Test
    void shouldReturn400WhenApplicationDateIsMissing() {
        given()
                .queryParam("productId", 35455)
                .queryParam("brandId", 1)

                .when()
                .get("/prices")

                .then()
                .statusCode(400)
                .body("title", equalTo("Missing request parameter"))
                .body("status", equalTo(400))
                .body("detail", equalTo(
                        "Required parameter 'applicationDate' is missing"))
                .body("instance", equalTo("/prices"));
    }

    @Test
    void shouldReturn400WhenProductIdHasInvalidType() {
        given()
                .queryParam("applicationDate", "2020-06-14T10:00:00Z")
                .queryParam("productId", "abc")
                .queryParam("brandId", 1)

                .when()
                .get("/prices")

                .then()
                .statusCode(400)
                .body("title", equalTo("Invalid request parameter"))
                .body("status", equalTo(400))
                .body("detail", equalTo(
                        "Parameter 'productId' has invalid value 'abc'"))
                .body("instance", equalTo("/prices"));
    }
}
