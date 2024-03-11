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

import info.magnolia.extensibility.shopify.mapper.RestClientResponseMapper;
import info.magnolia.extensibility.shopify.model.CustomCollectionResponse;
import info.magnolia.extensibility.shopify.model.CustomCollectionsResponse;
import info.magnolia.extensibility.shopify.model.ProductResponse;
import info.magnolia.extensibility.shopify.model.ProductsResponse;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

@RegisterRestClient(configKey = "shopify")
@RegisterProvider(RestClientResponseMapper.class)
@ApplicationScoped
public interface ShopifyHttpClient {

    @GET
    @Path("/products.json")
    ProductsResponse getItems(@HeaderParam("X-Shopify-Access-Token") String shopifyToken, @QueryParam("title") String title, @QueryParam("collection_id") Long collectionId);

    @GET
    @Path("/products/{item_id}.json")
    ProductResponse getItem(@HeaderParam("X-Shopify-Access-Token") String shopifyToken, @PathParam("item_id") String itemId);

    @GET
    @Path("/custom_collections.json")
    CustomCollectionsResponse getCategories(@HeaderParam("X-Shopify-Access-Token") String shopifyToken);

    @GET
    @Path("/custom_collections/{category_id}.json")
    CustomCollectionResponse getCategory(@HeaderParam("X-Shopify-Access-Token") String shopifyToken, @PathParam("category_id") String categoryId);
}
