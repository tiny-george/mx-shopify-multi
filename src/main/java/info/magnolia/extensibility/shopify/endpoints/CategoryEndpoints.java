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

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import info.magnolia.extensibility.shopify.service.ShopifyService;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/categories")
public class CategoryEndpoints {

    private final ShopifyService shopifyService;

    @Inject
    public CategoryEndpoints(ShopifyService shopifyService) {
        this.shopifyService = shopifyService;
    }

    @GET
    @Produces(APPLICATION_JSON)
    @PermitAll
    public Response allCategories(@HeaderParam( "subscription-id") String subscriptionId) {
        var items = shopifyService.getCategories(subscriptionId);
        if (items.isOk()) {
            return Response.ok(items.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(items.getError().getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    @PermitAll
    public Response getCategory(@PathParam("id") String categoryId, @HeaderParam( "subscription-id") String subscriptionId) {
        var category = shopifyService.getCategory(subscriptionId, categoryId);
        if (category.isOk()) {
            return Response.ok(category.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(category.getError().getMessage()).build();
        }
    }

}
