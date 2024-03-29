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
package info.magnolia.extensibility.shopify.client.rest;

import info.magnolia.extensibility.shopify.client.SecretValues;
import info.magnolia.extensibility.shopify.filter.StoreNameHelper;
import info.magnolia.extensibility.shopify.model.CustomCollection;
import info.magnolia.extensibility.shopify.model.Product;

import java.util.List;

import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ShopifyRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShopifyRestClient.class);

    private ShopifyHttpClient shopifyHttpClient;
    private StoreNameHelper storeNameHelper;

    @Inject
    public ShopifyRestClient(
            @RestClient
            ShopifyHttpClient shopifyHttpClient, StoreNameHelper storeNameHelper) {
        this.shopifyHttpClient = shopifyHttpClient;
        this.storeNameHelper = storeNameHelper;
    }

    public List<Product> getItems(SecretValues secretValues, String title, Long collectionId) {
        storeNameHelper.setStoreName(secretValues.store());
        try {
            return shopifyHttpClient
                    .getItems(secretValues.token(), title, collectionId).getProducts();
        } catch (WebApplicationException t) {
            LOGGER.error("Error getting shopify items", t);
            throw t;
        }
    }

    public Product getItem(SecretValues secretValues, String itemId) {
        storeNameHelper.setStoreName(secretValues.store());
        return shopifyHttpClient
                .getItem(secretValues.token(), itemId).getProduct();
    }


    public List<CustomCollection> getCustomCollections(SecretValues secretValues) {
        storeNameHelper.setStoreName(secretValues.store());
        return shopifyHttpClient
                .getCategories(secretValues.token()).getCustomCollections();
    }

    public CustomCollection getCustomCollection(SecretValues secretValues, String categoryId) {
        storeNameHelper.setStoreName(secretValues.store());
        return shopifyHttpClient
                .getCategory(secretValues.token(), categoryId).getCustomCollection();
    }
}
