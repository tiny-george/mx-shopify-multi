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

import info.magnolia.extensibility.shopify.client.SecretValues;
import info.magnolia.extensibility.shopify.client.ShopifyClient;
import info.magnolia.extensibility.shopify.dto.GenericListResponse;
import info.magnolia.extensibility.shopify.model.Product;
import info.magnolia.response.Response;
import info.magnolia.secrets.api.Secrets;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ShopifyService {

    public static final String TOKEN_KEY = "shopify.token";
    public static final String STORE_NAME = "shopify.store.name";
    private static final Logger LOGGER = LoggerFactory.getLogger(ShopifyService.class);
    private final ShopifyClient shopifyClient;
    private final Secrets secrets;

    @Inject
    public ShopifyService(ShopifyClient shopifyClient, Secrets secrets) {
        this.shopifyClient =  shopifyClient;
        this.secrets = secrets;
    }

    public Response<GenericListResponse> getItems(String subscriptionId) {
        LOGGER.debug("Calling get all products of subscription: {}", subscriptionId);
        return secretValues(subscriptionId)
                .map(shopifyClient::getItems)
                .map(products -> new GenericListResponse(products.size(), products))
                .map(Response::ok)
                .orElseGet(() -> Response.error(new Exception("Not found a shopify config for subscription: " + subscriptionId)));
    }


    public Response<Product> getItem(String subscriptionId, String itemId) {
        LOGGER.debug("Calling get product by id: {} for subscription: {}", itemId, subscriptionId);
        return secretValues(subscriptionId)
                .map(secrets -> shopifyClient.getItem(secrets, itemId))
                .map(Response::ok)
                .orElseGet(() -> Response.error(new Exception("Not found shopify config for subscription: " + subscriptionId)));
    }

    private Optional<SecretValues> secretValues(String subscriptionId) {
        var token = secrets.subscriptionGet(subscriptionId, TOKEN_KEY);
        var store = secrets.subscriptionGet(subscriptionId, STORE_NAME);
        if (token.isEmpty() || store.isEmpty()) {
            if (token.isEmpty()) {
                LOGGER.error("Not found shopify token for subscription: " + subscriptionId);
            }
            if (store.isEmpty()) {
                LOGGER.error("Not found shopify store name for subscription: " + subscriptionId);
            }
            return Optional.empty();
        } else {
            return Optional.of(new SecretValues(token.get(), store.get()));
        }
    }
}
