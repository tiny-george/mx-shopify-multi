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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/items")
public class ItemEndpoints {

    private final ShopifyService shopifyService;
    @Context
    private UriInfo uriInfo;

    @Inject
    public ItemEndpoints(ShopifyService shopifyService) {
        this.shopifyService = shopifyService;
    }

    @GET
    @Produces(APPLICATION_JSON)
    @PermitAll
    public Response allItems(@HeaderParam("subscription-id") String subscriptionId) {
        var items = shopifyService.getItems(subscriptionId, getFilterConditions());
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
    public Response oneItem(@PathParam("id") String itemId, @HeaderParam("subscription-id") String subscriptionId) {
        var item = shopifyService.getItem(subscriptionId, itemId);
        if (item.isOk()) {
            return Response.ok(item.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(item.getError().getMessage()).build();
        }
    }

    private Map<String, Object> getFilterConditions() {
        if (uriInfo != null && uriInfo.getQueryParameters() != null) {
            return uriInfo.getQueryParameters().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.join(",", entry.getValue())));
        } else {
            return new HashMap<>();
        }
    }
}
