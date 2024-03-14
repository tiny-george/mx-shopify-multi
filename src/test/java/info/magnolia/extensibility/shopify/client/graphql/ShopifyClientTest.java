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

package info.magnolia.extensibility.shopify.client.graphql;

import static org.junit.jupiter.api.Assertions.*;

import info.magnolia.extensibility.exception.ExtensionException;
import info.magnolia.extensibility.shopify.client.SecretValues;
import info.magnolia.extensibility.shopify.endpoints.WireMockTestExtension;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(WireMockTestExtension.class)
class ShopifyClientTest {
    private static final String EXISTING_CART_ID = "Z2NwLXVzLWNlbnRyYWwxOjAxSFJDTUI1SEJYWlZQRUoxUUJHM0hFUjhL";
    private static final String EXISTING_CART_GID = "gid://shopify/Cart/"+EXISTING_CART_ID;
    private static final String VARIANT_ID = "31775513411637";
    private static final String NON_EXISTING_VARIANT_ID = "nonExistingVariantId";
    private static final String EXISTING_VARIANT_GID = "gid://shopify/ProductVariant/"+VARIANT_ID;
    private static final String NON_EXISTING_CART_ID = "nonExistingCartId";
    private static final String NON_EXISTING_CART_GID = EXISTING_CART_GID+NON_EXISTING_CART_ID;
    private static final String A_STORE = "aStore";
    private static final String GET_CART_BY_ID = "getCartById";
    private static final String CREATE_CART = "createCart";
    private static final String ADD_LINE_TO_CART = "addLineToCart";
    private static final String REMOVE_LINE_FROM_CART = "removeLineFromCart";
    private static final String MAPPING_NAME_TEMPLATE = "?mapping-name=%s";
    private static final String EMPTY_TOKEN = "";
    private static final String INVALID_ID = "invalidId";
    private static final String CRASH_ID = "crashId";
    private static final SecretValues SECRET_VALUES = new SecretValues(EMPTY_TOKEN, A_STORE);
    private static final String CART_LINE_ID = "9b630dd5-9d42-4c61-ae7b-27c8cab8b471";
    private static final String NON_EXISTING_LINE_ID = "nonExistingLineId";

    @ConfigProperty(name = "shopify.graphql.url")
    String shopifyGraphqlUrl;

    @Test
    @DisplayName("Should return a valid cart when an existing id is provided")
    void shouldReturnValidCartWhenExistingIdIsProvided() {
        ShopifyGraphqlClient shopifyGraphqlClient = new ShopifyGraphqlClient(shopifyGraphqlUrl + String.format (MAPPING_NAME_TEMPLATE, GET_CART_BY_ID));
        var result = shopifyGraphqlClient.getCart(SECRET_VALUES, EXISTING_CART_ID);
        assertNotNull(result);
        assertEquals(EXISTING_CART_GID, result.id());
    }

    @Test
    @DisplayName("Should return a null cart when a non-existing id is provided")
    void shouldReturnNullCartWhenNonExistingIdIsProvided() {
        ShopifyGraphqlClient shopifyGraphqlClient = new ShopifyGraphqlClient(shopifyGraphqlUrl + String.format (MAPPING_NAME_TEMPLATE, GET_CART_BY_ID));
        ExtensionException notFoundException = Assertions.assertThrows(ExtensionException.class, () -> shopifyGraphqlClient.getCart(SECRET_VALUES, NON_EXISTING_CART_ID));

        assertEquals("Cart not found", notFoundException.getTitle());
        assertEquals(String.format("The cart with id -%s- was not found on the store: %s", NON_EXISTING_CART_ID, A_STORE), notFoundException.getDetail());
    }

    @Test
    @DisplayName("Should return errors when query is not valid")
    void shouldReturnErrorsWhenQueryIsNotValid() {

        ShopifyGraphqlClient shopifyGraphqlClient = new ShopifyGraphqlClient(shopifyGraphqlUrl + String.format (MAPPING_NAME_TEMPLATE, GET_CART_BY_ID));

        ExtensionException extensionException = Assertions.assertThrows(ExtensionException.class, () -> shopifyGraphqlClient.getCart(SECRET_VALUES, INVALID_ID));

        assertEquals("Remote api returned some errors: Field must have selections (field 'cart' returns Cart but has no selections. Did you mean 'cart { ... }'?)", extensionException.getTitle());
    }

    @Test
    @DisplayName("Should throw exception when communication error occurs")
    void shouldThrowExceptionWhenCommunicationErrorOccurs() {
        ShopifyGraphqlClient shopifyGraphqlClient = new ShopifyGraphqlClient(shopifyGraphqlUrl + String.format (MAPPING_NAME_TEMPLATE, GET_CART_BY_ID));

        SecretValues secretValues = new SecretValues("aToken", "aStore");

        ExtensionException extensionException = Assertions.assertThrows(ExtensionException.class, () -> shopifyGraphqlClient.getCart(secretValues, CRASH_ID));

        assertEquals("There was a problem calling storefront api", extensionException.getTitle());
    }

    @Test
    @DisplayName("Should create a cart without any problems")
    void shouldCreateCartWithoutAnyProblems() {
        ShopifyGraphqlClient shopifyGraphqlClient = new ShopifyGraphqlClient(shopifyGraphqlUrl + String.format (MAPPING_NAME_TEMPLATE, CREATE_CART));
        var result = shopifyGraphqlClient.createCart(SECRET_VALUES);
        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(EXISTING_CART_GID, result.id());
        assertNotNull(result.checkoutUrl());
    }

    @Test
    @DisplayName("Should add a line to a cart without any problems")
    void shouldAddLineToCartWithoutAnyProblems() {
        ShopifyGraphqlClient shopifyGraphqlClient = new ShopifyGraphqlClient(shopifyGraphqlUrl + String.format (MAPPING_NAME_TEMPLATE, ADD_LINE_TO_CART));
        var result = shopifyGraphqlClient.addLineToCart(SECRET_VALUES, EXISTING_CART_ID, VARIANT_ID, 1);
        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(EXISTING_CART_GID, result.id());
        assertTrue(result.lines().edges().stream().
                anyMatch( line -> line.node().merchandise().id().equals(EXISTING_VARIANT_GID) && line.node().quantity() == 1));
    }

    @Test
    @DisplayName("Should create a new cart when adding a line is requested, providing a wrong cartId")
    void shouldCreateNewCartWhenAddLineIsRequestedProvidingWrongCartId() {
        /**
         * I promise: this is how the real shopify api works
         */
        ShopifyGraphqlClient shopifyGraphqlClient = new ShopifyGraphqlClient(shopifyGraphqlUrl + String.format (MAPPING_NAME_TEMPLATE, ADD_LINE_TO_CART));
        var result = shopifyGraphqlClient.addLineToCart(SECRET_VALUES, NON_EXISTING_CART_ID, VARIANT_ID, 1);
        assertNotNull(result);
        assertNotNull(result.id());
        assertNotEquals(NON_EXISTING_CART_GID, result.id());
        assertTrue(result.lines().edges().isEmpty());
    }

    @Test
    @DisplayName("Should return null cart when adding a line is requested, providing a wrong variantId")
    void shouldReturnNullCartWhenNonExistingVariantIdIsProvided() {
        /**
         * I promise: this is how the real shopify api works
         */
        ShopifyGraphqlClient shopifyGraphqlClient = new ShopifyGraphqlClient(shopifyGraphqlUrl + String.format (MAPPING_NAME_TEMPLATE, ADD_LINE_TO_CART));
        var result = shopifyGraphqlClient.addLineToCart(SECRET_VALUES, NON_EXISTING_CART_ID, NON_EXISTING_VARIANT_ID, 1);
        assertNull(result);
    }

    @Test
    @DisplayName("Should remove a line from a cart without any problems")
    void shouldRemoveLineFromCartWithoutAnyProblems() {
        ShopifyGraphqlClient shopifyGraphqlClient = new ShopifyGraphqlClient(shopifyGraphqlUrl + String.format (MAPPING_NAME_TEMPLATE, REMOVE_LINE_FROM_CART));
        var result = shopifyGraphqlClient.removeLineFromCart(SECRET_VALUES, EXISTING_CART_ID, CART_LINE_ID);
        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(EXISTING_CART_GID, result.id());
        assertTrue(result.lines().edges().isEmpty());
    }

    @Test
    @DisplayName("Should create a new cart when removing a line is requested providing a non existing cartId")
    void shouldCreateNewCartWhenRemovingLineIsRequestedWithNonExistingCartId() {
        /**
         * I promise: this is how the real shopify api works
         */
        ShopifyGraphqlClient shopifyGraphqlClient = new ShopifyGraphqlClient(shopifyGraphqlUrl + String.format (MAPPING_NAME_TEMPLATE, REMOVE_LINE_FROM_CART));
        var result = shopifyGraphqlClient.removeLineFromCart(SECRET_VALUES, NON_EXISTING_CART_ID, CART_LINE_ID);
        assertNotNull(result);
        assertNotNull(result.id());
        assertNotEquals(EXISTING_CART_GID, result.id());
        assertTrue(result.lines().edges().isEmpty());
    }

    @Test
    @DisplayName("Should return existing cart in its current status when removing a line is requested with non existing line id")
    void shouldReturnExistingCartInItsCurrentStatusWhenRemovingLineIsRequestedWithNonExistingLineId() {
        /**
         * I promise: this is how the real shopify api works
         */
        ShopifyGraphqlClient shopifyGraphqlClient = new ShopifyGraphqlClient(shopifyGraphqlUrl + String.format (MAPPING_NAME_TEMPLATE, REMOVE_LINE_FROM_CART));
        var result = shopifyGraphqlClient.removeLineFromCart(SECRET_VALUES, EXISTING_CART_ID, NON_EXISTING_LINE_ID);
        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(EXISTING_CART_GID, result.id());
        assertTrue(result.lines().edges().isEmpty());
    }

}