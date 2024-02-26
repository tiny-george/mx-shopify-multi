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

import info.magnolia.extensibility.shopify.dto.ItemsFilter;
import info.magnolia.extensibility.shopify.service.ShopifyService;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/items")
public class ShopifyEndpoints {

    private final ShopifyService shopifyService;

    @Inject
    public ShopifyEndpoints(ShopifyService shopifyService) {
        this.shopifyService = shopifyService;
    }

    @GET
    @Produces(APPLICATION_JSON)
    @PermitAll
    public Response allItems(@HeaderParam( "subscription-id") String subscriptionId, @BeanParam ItemsFilter itemsFilter) {
        var items = shopifyService.getItems(subscriptionId, itemsFilter);
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
    public Response oneItem(@PathParam("id") String itemId, @HeaderParam( "subscription-id") String subscriptionId) {
        var item = shopifyService.getItem(subscriptionId, itemId);
        if (item.isOk()) {
            return Response.ok(item.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(item.getError().getMessage()).build();
        }
    }
}
