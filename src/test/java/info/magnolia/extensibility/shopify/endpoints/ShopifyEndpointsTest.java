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

import static info.magnolia.extensibility.shopify.service.ShopifyService.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

import info.magnolia.extensibility.shopify.client.SecretValues;
import info.magnolia.extensibility.shopify.client.ShopifyClient;
import info.magnolia.extensibility.shopify.model.Product;
import info.magnolia.secrets.api.Secrets;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.WebApplicationException;

@QuarkusTest
class ShopifyEndpointsTest {

    private static final String SUBSCRIPTION_ID = "aSubscriptionId";
    private static final String SHOPIFY_TOKEN = "someToken";
    private static final String SHOPIFY_STORE_NAME = "someStoreName";
    private static final String ITEM_ID = "anItemId";

    @InjectMock
    Secrets secrets;

    @InjectMock
    ShopifyClient shopifyClient;

    @Test
    void listOfProducts() {
        Product product = new Product();
        product.setID(5L);
        Mockito.when(shopifyClient.getItems(new SecretValues(SHOPIFY_TOKEN, SHOPIFY_STORE_NAME))).thenReturn(List.of(product));

        given().when()
                .headers(Map.of("subscription_id", SUBSCRIPTION_ID))
                .get("/items/")
                .then().statusCode(200).log().all()
                .body("size", is(1),
                        "items.size()", is(1),
                        "items[0].id", is(5)
                );
    }

    @Test
    void oneProduct() {
        Product product = new Product();
        product.setID(7L);
        Mockito.when(shopifyClient.getItem(new SecretValues(SHOPIFY_TOKEN, SHOPIFY_STORE_NAME), ITEM_ID)).thenReturn(product);

        given().when()
                .headers(Map.of("subscription_id", SUBSCRIPTION_ID))
                .get("/items/" + ITEM_ID)
                .then().statusCode(200).log().all()
                .body("id", is(7));
    }

    @Test
    void checkItemDoesNotExist() {

        Mockito.when(shopifyClient.getItem(new SecretValues(SHOPIFY_TOKEN, SHOPIFY_STORE_NAME), ITEM_ID)).thenThrow(new WebApplicationException(404));

        given().when()
                .headers(Map.of("subscription_id", SUBSCRIPTION_ID))
                .get("/items/" + ITEM_ID)
                .then().statusCode(404).log().all();
    }

    @BeforeEach
    public void setupSecrets() {
        Mockito.when(secrets.subscriptionGet(SUBSCRIPTION_ID, TOKEN_KEY))
                .thenReturn(Optional.of(SHOPIFY_TOKEN));
        Mockito.when(secrets.subscriptionGet(SUBSCRIPTION_ID, STORE_NAME))
                .thenReturn(Optional.of(SHOPIFY_STORE_NAME));
    }
}
