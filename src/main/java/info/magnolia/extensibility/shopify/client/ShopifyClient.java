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
package info.magnolia.extensibility.shopify.client;

import info.magnolia.extensibility.shopify.filter.StoreNameHelper;
import info.magnolia.extensibility.shopify.model.Product;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ShopifyClient {
    private ShopifyHttpClient shopifyHttpClient;
    private StoreNameHelper storeNameHelper;

    @Inject
    public ShopifyClient(
            @RestClient
            ShopifyHttpClient shopifyHttpClient,StoreNameHelper storeNameHelper) {
        this.shopifyHttpClient = shopifyHttpClient;
        this.storeNameHelper =  storeNameHelper;
    }

    public List<Product> getItems(SecretValues secretValues) {
        storeNameHelper.setStoreName(secretValues.store());
        return shopifyHttpClient
                .getItems(secretValues.token()).getProducts();
    }

    public Product getItem(SecretValues secretValues, String itemId) {
        storeNameHelper.setStoreName(secretValues.store());
        return shopifyHttpClient
                .getItem(secretValues.token(), itemId).getProduct();
    }


}
