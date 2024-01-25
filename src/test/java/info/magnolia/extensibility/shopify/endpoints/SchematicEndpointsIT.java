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

import org.junit.jupiter.api.Test;


import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class SchematicEndpointsIT {

    @Test
    void getSchematic() {
        given().when().get("/schematic.json")
                .then().statusCode(200).log().all()
                .body("properties.adminGraphqlAPIID.type", equalTo("string"))
                .and().body("properties.bodyHTML.type", equalTo("string"))
                .and().body("properties.createdAt.type", equalTo("string"))
                .and().body("properties.handle.type", equalTo("string"))
                .and().body("properties.id.type", equalTo("integer"))
                .and().body("properties.images.type", equalTo("array"))
                .and().body("properties.images.items.$ref", equalTo("#/definitions/Image"))
                .and().body("properties.options.type", equalTo("array"))
                .and().body("properties.options.items.type", equalTo("object"))
                .and().body("properties.productType.type", equalTo("string"))
                .and().body("properties.publishedAt.type", equalTo("string"))
                .and().body("properties.publishedAt.format", equalTo("date-time"))
                .and().body("properties.publishedScope.type", equalTo("string"))
                .and().body("properties.publishedScope.const", equalTo("WEB"))
                .and().body("properties.status.type", equalTo("string"))
                .and().body("properties.tags.type", equalTo("string"))
                .and().body("properties.templateSuffix.type", equalTo("string"))
                .and().body("properties.title.type", equalTo("string"))
                .and().body("properties.updatedAt.type", equalTo("string"))
                .and().body("properties.updatedAt.format", equalTo("date-time"))
                .and().body("properties.variants.type", equalTo("array"))
                .and().body("properties.variants.items.type", equalTo("object"))
                .and().body("properties.vendor.type", equalTo("string"));
    }
}
