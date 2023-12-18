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

import info.magnolia.extensibility.shopify.model.Product;

import java.net.URI;
import java.util.List;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ShopifyClient {

    public List<Product> getItems(SecretValues secretValues) {
        return getHttpClient(secretValues.store())
                .getItems(secretValues.token()).getProducts();
    }

    public Product getItem(SecretValues secretValues, String itemId) {
        return getHttpClient(secretValues.store())
                .getItem(secretValues.token(), itemId).getProduct();
    }

    ShopifyHttpClient getHttpClient(String storeName) {
        return RestClientBuilder.newBuilder()
                .baseUri(URI.create("https://" + storeName + ".myshopify.com/admin/api/2023-10/"))
                .followRedirects(true)
                .build(ShopifyHttpClient.class);
    }
}
