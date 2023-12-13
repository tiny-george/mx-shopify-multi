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

package info.magnolia.extensibility.shopify.service;

import static info.magnolia.extensibility.shopify.service.ShopifyService.TOKEN_KEY;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

import info.magnolia.extensibility.shopify.client.ShopifyClient;
import info.magnolia.extensibility.shopify.model.Product;
import info.magnolia.secrets.api.Secrets;

import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class ShopifyEndpointsTest {

    private static final String SUBSCRIPTION_ID = "aSubscriptionId";
    private static final String SHOPIFY_TOKEN = "someToken";
    private static final String ITEM_ID = "anItemId";

    @InjectMock
    Secrets secrets;

    @InjectMock
    @RestClient
    ShopifyClient shopifyClient;

    @Test
    void listOfProducts() {
        Mockito.when(secrets.subscriptionGet(SUBSCRIPTION_ID, TOKEN_KEY))
                .thenReturn(Optional.of(SHOPIFY_TOKEN));
        Product product = new Product();
        product.setID(5L);
        Mockito.when(shopifyClient.getItems(SHOPIFY_TOKEN)).thenReturn(List.of(product));

        given().when().get("/items/all/" + SUBSCRIPTION_ID)
                .then().statusCode(200).log().all()
                .body("size()", is(1),
                        "id[0]", is(5)
                );
    }

    @Test
    void oneProduct() {
        Mockito.when(secrets.subscriptionGet(SUBSCRIPTION_ID, TOKEN_KEY))
                .thenReturn(Optional.of(SHOPIFY_TOKEN));
        Product product = new Product();
        product.setID(7L);
        Mockito.when(shopifyClient.getItem(SHOPIFY_TOKEN, ITEM_ID)).thenReturn(product);

        given().when().get("/items/get/" + ITEM_ID + "/" + SUBSCRIPTION_ID)
                .then().statusCode(200).log().all()
                .body("id", is(7));
    }
}
