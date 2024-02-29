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
import static org.hamcrest.Matchers.is;

import java.util.Map;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(WireMockTestExtension.class)
class CategoryEndpointsIT {

    private static final String SUBSCRIPTION_ID = "aSubscriptionId";

    @Test
    void listOfCategories() {
        given().when()
                .headers(Map.of("subscription-id", SUBSCRIPTION_ID))
                .get("/categories/")
                .then().statusCode(200).log().all()
                .body("size", is(3),
                        "items.size()", is(3),
                        "items[0].id", is(1)
                );
    }

    @Test
    void findCategoryById() {
        given().when()
                .headers(Map.of("subscription-id", SUBSCRIPTION_ID))
                .get("/categories/1")
                .then().statusCode(200).log().all()
                .body("id", is(1),
                        "title", is("Luggage"));
    }

    @Test
    void categoryNotFound() {
        given().when()
                .headers(Map.of("subscription-id", SUBSCRIPTION_ID))
                .get("/categories/2")
                .then().statusCode(404).log().all();
    }

}
