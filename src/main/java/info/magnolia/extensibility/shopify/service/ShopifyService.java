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
import info.magnolia.extensibility.shopify.client.ShopifyClient;
import info.magnolia.extensibility.shopify.dto.GenericListResponse;
import info.magnolia.extensibility.shopify.model.CustomCollection;
import info.magnolia.extensibility.shopify.model.Product;
import info.magnolia.response.Response;
import info.magnolia.secrets.api.Secrets;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ShopifyService {

    private static final String SEARCH_TERM = "searchTerm";
    private static final String CATEGORY_ID = "categoryId";
    public static final String TOKEN_KEY = "shopify.token";
    public static final String STORE_NAME = "shopify.store.name";
    private static final Logger LOGGER = LoggerFactory.getLogger(ShopifyService.class);
    private final ShopifyClient shopifyClient;
    private final Secrets secrets;

    @Inject
    public ShopifyService(ShopifyClient shopifyClient, Secrets secrets) {
        this.shopifyClient = shopifyClient;
        this.secrets = secrets;
    }

    public Response<GenericListResponse> getItems(String subscriptionId, Map<String, Object> filters) {
        LOGGER.debug("Calling get all products of subscription: {}", subscriptionId);

        return secretValues(subscriptionId)
                .map(retrievedSecrets -> shopifyClient.getItems(retrievedSecrets,  (String) filters.get(SEARCH_TERM), (Long) filters.computeIfPresent(CATEGORY_ID, (key, value) -> Long.valueOf((String) value))))
                .map(products -> new GenericListResponse(products.size(), products))
                .map(Response::ok)
                .orElseThrow(() -> new NotFoundException("Shopify config not found", "Shopify config not found for subscription: " + subscriptionId));

    }


    public Response<Product> getItem(String subscriptionId, String itemId) {
        LOGGER.debug("Calling get product by id: {} for subscription: {}", itemId, subscriptionId);

        return secretValues(subscriptionId)
                .map(secretValues -> shopifyClient.getItem(secretValues, itemId))
                .map(Response::ok)
                .orElseThrow(() -> new NotFoundException("Shopify config not found", "Shopify config not found for subscription: " + subscriptionId));

    }

    public Response<GenericListResponse> getCategories(String subscriptionId) {
        LOGGER.debug("Calling get all categories (customcollections) of subscription: {}", subscriptionId);

        return secretValues(subscriptionId)
                .map(shopifyClient::getCustomCollections)
                .map(products -> new GenericListResponse(products.size(), products))
                .map(Response::ok)
                .orElseThrow(() -> new NotFoundException("Shopify config not found", "Shopify config not found for subscription: " + subscriptionId));
    }

    public Response<CustomCollection> getCategory(String subscriptionId, String categoryId) {
        LOGGER.debug("Calling get category by id: {} for subscription: {}", categoryId, subscriptionId);

        return secretValues(subscriptionId)
                .map(secretValues -> shopifyClient.getCustomCollection(secretValues, categoryId))
                .map(Response::ok)
                .orElseThrow(() -> new NotFoundException("Shopify config not found", "Shopify config not found for subscription: " + subscriptionId));

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
}
