/*
 * This file Copyright (c) 2023 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Magnolia Network Agreement
 * which accompanies this distribution, and is available at
 * http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.extensibility.shopify.endpoints;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(WireMockTestExtension.class)
class ShopifyEndpointsIT {

    private static final String SUBSCRIPTION_ID = "aSubscriptionId";
    private static final String NOT_AUTHORIZED_SUB_ID = "nonAuthorizedSubId";

    private static final String ITEM_ID = "4494451802165";

    @Test
    void listOfProducts() {
        given().when()
                .headers(Map.of("subscription-id", SUBSCRIPTION_ID))

                .get("/items/")
                .then().statusCode(200).log().all()
                .body("size", is(1),
                        "items.size()", is(1),
                        "items[0].id", is(4494451802165L)
                );
    }

    @Test
    void getProductsByTitle() {
        given().when()
                .headers(Map.of("subscription-id", SUBSCRIPTION_ID))
                .queryParam("searchTerm","Especial Medio Cycling Backpack")
                .get("/items")
                .then().statusCode(200).log().all()
                .body("size", is(1),
                        "items.size()", is(1),
                        "items[0].id", is(4494451802166L)
                );
    }

    @Test
    void oneProduct() {

        given().when()
                .headers(Map.of("subscription-id", SUBSCRIPTION_ID))
                .get("/items/" + ITEM_ID)
                .then().statusCode(200).log().all()
                .body("id", is(4494451802165L));
    }

    @Test
    void checkItemDoesNotExist() {

        given().when()
                .headers(Map.of("subscription-id", SUBSCRIPTION_ID))
                .get("/items/" + "nonExistingItemId")
                .then()
                .contentType("application/problem+json")
                .statusCode(404).log().all()
                .body("status", is(404),
                        "title", is("Element not found"),
                        "detail", is("{\"errors\":\"Not Found\"}"),
                        "trace_id", is(not(empty())),
                        "span_id", is(not(empty()))
                );
    }

    @Test
    void checkNotAuthorized() {

        given().when()
                .headers(Map.of("subscription-id", NOT_AUTHORIZED_SUB_ID))
                .get("/items/")
                .then()
                .contentType("application/problem+json")
                .statusCode(401).log().all()
                .body("status", is(401),
                        "title", is("There was an authorization problem calling remote api"),
                        "detail", is("{\"errors\":\"Not Authorized\"}"),
                        "trace_id", is(not(empty())),
                        "span_id", is(not(empty()))
                );
    }

    @Test
    void checkInternalServerError() {

        given().when()
                .headers(Map.of("subscription-id", "genericExceptionSubId"))
                .get("/items/")
                .then()
                .contentType("application/problem+json")
                .statusCode(500).log().all()
                .body("status", is(500),
                        "title", is("There was a problem calling remote api"),
                        "detail", is("{\"errors\":\"Unknown error\"}"),
                        "trace_id", is(not(empty())),
                        "span_id", is(not(empty()))
                );
    }
}
