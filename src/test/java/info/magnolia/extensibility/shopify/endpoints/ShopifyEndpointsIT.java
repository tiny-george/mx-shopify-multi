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

    private static final String ITEM_ID = "anItemId";

    @Test
    void listOfProducts() {
        given().when()
                .headers(Map.of("space", SUBSCRIPTION_ID))
                .get("/items/")
                .then().statusCode(200).log().all()
                .body("size", is(1),
                        "items.size()", is(1),
                        "items[0].id", is(4494451802165L)
                );
    }

    @Test
    void oneProduct() {

        given().when()
                .headers(Map.of("space", SUBSCRIPTION_ID))
                .get("/items/" + ITEM_ID)
                .then().statusCode(200).log().all()
                .body("id", is(4494451802165L));
    }

    @Test
    void checkItemDoesNotExist() {

        given().when()
                .headers(Map.of("space", SUBSCRIPTION_ID))
                .get("/items/" + "nonExistingItemId")
                .then()
                .contentType("application/problem+json")
                .statusCode(404).log().all()
                .body("status", is(404))
                .and()
                .body("title", is("Element not found"))
                .and()
                .body("detail", is("{\"errors\":\"Not Found\"}"))
                .and()
                .body("trace_id", is(not(empty())))
                .and()
                .body("span_id", is(not(empty())));
    }

}
