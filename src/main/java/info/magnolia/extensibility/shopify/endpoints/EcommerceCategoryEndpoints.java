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

import info.magnolia.datasource.ecommerce.Category;
import info.magnolia.extensibility.shopify.model.CustomCollection;
import info.magnolia.extensibility.shopify.service.ShopifyService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/categories")
public class EcommerceCategoryEndpoints {

    private final ShopifyService shopifyService;

    @Inject
    public EcommerceCategoryEndpoints(ShopifyService shopifyService) {
        this.shopifyService = shopifyService;
    }

    @GET
    @Produces(APPLICATION_JSON)
    @PermitAll
    public List<Category> allCategories(@HeaderParam("subscription-id") String subscriptionId) {
        var categories = shopifyService.getCategories(subscriptionId);
        if (categories.isOk()) {
            return categories.get().items().stream()
                    .map(this::toEcommerceCategory)
                    .toList();
        } else {
            throw new RuntimeException(categories.getError());
        }
    }

    @GET
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    @PermitAll
    public Category category(@PathParam("id") String categoryId, @HeaderParam("subscription-id") String subscriptionId) {
        var category = shopifyService.getCategory(subscriptionId, categoryId);
        if (category.isOk()) {
            return toEcommerceCategory(category.get());
        } else {
            throw new RuntimeException(category.getError());
        }
    }

    @GET
    @Path("/byParent/{categoryId}")
    @Produces(APPLICATION_JSON)
    @PermitAll
    public List<Category> byParent(@PathParam("categoryId") String categoryId, @HeaderParam("subscription-id") String subscriptionId) {
        var categories = shopifyService.getCategories(subscriptionId);
        if (categories.isOk()) {
            return categories.get().items().stream()
                    //TODO
                    .filter(cat -> cat.getHandle().equals(categoryId))
                    .map(this::toEcommerceCategory)
                    .toList();
        } else {
            throw new RuntimeException(categories.getError());
        }
    }

    @GET
    @Path("/byProduct/{productId}")
    @Produces(APPLICATION_JSON)
    @PermitAll
    public List<Category> byProduct(@PathParam("productId") String productId, @HeaderParam("subscription-id") String subscriptionId) {
        var product = shopifyService.getItem(subscriptionId, productId);
        if (product.isOk()) {
            var categories = shopifyService.getCategories(subscriptionId);
            if (categories.isOk()) {
                return categories.get().items().stream()
                        //TODO
                        .filter(cat -> product.get().getProductType().equals(cat.getHandle()))
                        .map(this::toEcommerceCategory)
                        .toList();
            } else {
                throw new RuntimeException(categories.getError());
            }
        } else {
            throw new RuntimeException(product.getError());
        }
    }

    private Category toEcommerceCategory(CustomCollection collection) {
        return new Category(
                Float.toString(collection.getId()),
                collection.getHandle(),
                collection.getTitle(),
                0,
                0,
                null,
                "/" + collection.getHandle()
        );
    }
}
