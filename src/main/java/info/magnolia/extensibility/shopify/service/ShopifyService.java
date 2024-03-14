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

import info.magnolia.extensibility.exception.NotFoundException;
import info.magnolia.extensibility.shopify.client.SecretValues;
import info.magnolia.extensibility.shopify.client.graphql.ShopifyGraphqlClient;
import info.magnolia.extensibility.shopify.client.graphql.dto.Cart;
import info.magnolia.extensibility.shopify.client.rest.ShopifyRestClient;
import info.magnolia.extensibility.shopify.dto.GenericListResponse;
import info.magnolia.extensibility.shopify.model.CustomCollection;
import info.magnolia.extensibility.shopify.model.Product;
import info.magnolia.response.Response;
import info.magnolia.secrets.api.Secrets;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ShopifyService {

    private static final String SEARCH_TERM = "searchTerm";
    private static final String CATEGORY_ID = "categoryId";
    private static final String TOKEN_KEY = "shopify.token";
    private static final String STORE_NAME = "shopify.store.name";
    private static final Logger LOGGER = LoggerFactory.getLogger(ShopifyService.class);
    private static final String SHOPIFY_CONFIG_NOT_FOUND_FOR_SUBSCRIPTION = "Shopify config not found for subscription: ";
    private static final String SHOPIFY_CONFIG_NOT_FOUND = "Shopify config not found";
    private final ShopifyRestClient shopifyRestClient;
    private final ShopifyGraphqlClient shopifyGraphqlClient;
    private final Secrets secrets;

    @Inject
    public ShopifyService(ShopifyRestClient shopifyRestClient, ShopifyGraphqlClient shopifyGraphqlClient, Secrets secrets) {
        this.shopifyRestClient = shopifyRestClient;
        this.shopifyGraphqlClient = shopifyGraphqlClient;
        this.secrets = secrets;
    }

    public Response<GenericListResponse> getItems(String subscriptionId, Map<String, Object> filters) {
        LOGGER.debug("Calling get all products of subscription: {}", subscriptionId);

        return secretValues(subscriptionId)
                .map(retrievedSecrets -> shopifyRestClient.getItems(retrievedSecrets,  (String) filters.get(SEARCH_TERM), (Long) filters.computeIfPresent(CATEGORY_ID, (key, value) -> Long.valueOf((String) value))))
                .map(products -> new GenericListResponse(products.size(), products))
                .map(Response::ok)
                .orElseThrow(() -> new NotFoundException(SHOPIFY_CONFIG_NOT_FOUND, SHOPIFY_CONFIG_NOT_FOUND_FOR_SUBSCRIPTION + subscriptionId));

    }


    public Response<Product> getItem(String subscriptionId, String itemId) {
        LOGGER.debug("Calling get product by id: {} for subscription: {}", itemId, subscriptionId);

        return secretValues(subscriptionId)
                .map(secretValues -> shopifyRestClient.getItem(secretValues, itemId))
                .map(Response::ok)
                .orElseThrow(() -> new NotFoundException(SHOPIFY_CONFIG_NOT_FOUND, SHOPIFY_CONFIG_NOT_FOUND_FOR_SUBSCRIPTION + subscriptionId));

    }

    public Response<GenericListResponse> getCategories(String subscriptionId) {
        LOGGER.debug("Calling get all categories (customcollections) of subscription: {}", subscriptionId);

        return secretValues(subscriptionId)
                .map(shopifyRestClient::getCustomCollections)
                .map(products -> new GenericListResponse(products.size(), products))
                .map(Response::ok)
                .orElseThrow(() -> new NotFoundException(SHOPIFY_CONFIG_NOT_FOUND, SHOPIFY_CONFIG_NOT_FOUND_FOR_SUBSCRIPTION + subscriptionId));
    }

    public Response<CustomCollection> getCategory(String subscriptionId, String categoryId) {
        LOGGER.debug("Calling get category by id: {} for subscription: {}", categoryId, subscriptionId);

        return secretValues(subscriptionId)
                .map(secretValues -> shopifyRestClient.getCustomCollection(secretValues, categoryId))
                .map(Response::ok)
                .orElseThrow(() -> new NotFoundException(SHOPIFY_CONFIG_NOT_FOUND, SHOPIFY_CONFIG_NOT_FOUND_FOR_SUBSCRIPTION + subscriptionId));

    }

    public Response<Cart> getCart(String subscriptionId, String cartId) {
        LOGGER.debug("Calling get cart by id: {} for subscription: {}", cartId, subscriptionId);

        return secretValues(subscriptionId)
                .map(secretValues -> shopifyGraphqlClient.getCart(secretValues, cartId))
                .map(Response::ok)
                .orElseThrow(() -> new NotFoundException(SHOPIFY_CONFIG_NOT_FOUND, SHOPIFY_CONFIG_NOT_FOUND_FOR_SUBSCRIPTION + subscriptionId));

    }

    private Optional<SecretValues> secretValues(String subscriptionId) {
        LOGGER.debug("Requesting token and secret for subscription: {}", subscriptionId);
        var token = secrets.subscriptionGet(subscriptionId, TOKEN_KEY);
        var store = secrets.subscriptionGet(subscriptionId, STORE_NAME);
        if (token.isEmpty() || store.isEmpty()) {
            if (token.isEmpty()) {
                LOGGER.error("Not found shopify token for subscription: {}", subscriptionId);
            }
            if (store.isEmpty()) {
                LOGGER.error("Not found shopify store name for subscription: {}", subscriptionId);
            }
            return Optional.empty();
        } else {
            return Optional.of(new SecretValues(token.get(), store.get()));
        }
    }

    public Response<Cart> createCart(String subscriptionId) {
        LOGGER.debug("Calling create cart for subscription: {}", subscriptionId);

        return secretValues(subscriptionId)
                .map(shopifyGraphqlClient::createCart)
                .map(Response::ok)
                .orElseThrow(() -> new NotFoundException(SHOPIFY_CONFIG_NOT_FOUND, SHOPIFY_CONFIG_NOT_FOUND_FOR_SUBSCRIPTION + subscriptionId));
    }

    public Response<Cart> addLineToCart(String subscriptionId, String cartId, String productId, Integer quantity) {
        LOGGER.debug("Calling add line to cart: {} for subscription: {}, product: {} and quantity: {}", cartId, subscriptionId, productId, quantity );
        return secretValues(subscriptionId)
                .map( secret -> shopifyGraphqlClient.addLineToCart (secret, cartId, productId, getSanitizedQuantity(quantity)))
                .map(Response::ok)
                .orElseThrow(() -> new NotFoundException(SHOPIFY_CONFIG_NOT_FOUND, SHOPIFY_CONFIG_NOT_FOUND_FOR_SUBSCRIPTION + subscriptionId));
    }

    public Response<Cart> removeLineFromCart(String subscriptionId, String cartId, String lineId) {
        LOGGER.debug("Calling remove line from cart: {} for subscription: {} and line: {}", cartId, subscriptionId, lineId);
        return secretValues(subscriptionId)
                .map( secret -> shopifyGraphqlClient.removeLineFromCart(secret, cartId, lineId))
                .map(Response::ok)
                .orElseThrow(() -> new NotFoundException(SHOPIFY_CONFIG_NOT_FOUND, SHOPIFY_CONFIG_NOT_FOUND_FOR_SUBSCRIPTION + subscriptionId));
    }

    public Response<Cart> updateQuantity(String subscriptionId, String cartId, String lineId, Integer quantity) {
        LOGGER.debug("Calling remove line from cart: {} for subscription: {} and line: {} with quantity: {}", cartId, subscriptionId, lineId, quantity);
        return secretValues(subscriptionId)
                .map( secret -> shopifyGraphqlClient.updateQuantity(secret, cartId, lineId, getSanitizedQuantity(quantity)))
                .map(Response::ok)
                .orElseThrow(() -> new NotFoundException(SHOPIFY_CONFIG_NOT_FOUND, SHOPIFY_CONFIG_NOT_FOUND_FOR_SUBSCRIPTION + subscriptionId));
    }

    private int getSanitizedQuantity(Integer quantity) {
        int sanitizedQuantity;
        if (Objects.isNull(quantity) || quantity <= 0){
            sanitizedQuantity = 1;
        }
        else {
            sanitizedQuantity = quantity;
        }
        return sanitizedQuantity;
    }
}
