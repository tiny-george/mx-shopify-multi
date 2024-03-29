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
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/cart")
public class CartEndpoints {

    private final ShopifyService shopifyService;

    @Inject
    public CartEndpoints(ShopifyService shopifyService) {
        this.shopifyService = shopifyService;
    }

    @GET
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    @PermitAll
    public Response getCart(@PathParam("id") String cartId, @HeaderParam("subscription-id") String subscriptionId) {
        var item = shopifyService.getCart(subscriptionId, cartId);
        if (item.isOk()) {
            return Response.ok(item.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(item.getError().getMessage()).build();
        }
    }

    @POST
    @Produces(APPLICATION_JSON)
    @PermitAll
    public Response createCart(@HeaderParam("subscription-id") String subscriptionId) {
        var item = shopifyService.createCart(subscriptionId);
        if (item.isOk()) {
            return Response.ok(item.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(item.getError().getMessage()).build();
        }
    }

    @POST
    @Path("/{cartId}/line/{productId}")
    @Produces(APPLICATION_JSON)
    @PermitAll
    public Response addLineToCart(@HeaderParam("subscription-id") String subscriptionId, @PathParam("cartId") String cartId, @PathParam("productId") String productId, @QueryParam("quantity") Integer quantity) {
        var item = shopifyService.addLineToCart(subscriptionId, cartId, productId, quantity);
        if (item.isOk()) {
            return Response.ok(item.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(item.getError().getMessage()).build();
        }
    }

    @DELETE
    @Path("/{cartId}/line/{lineId}")
    @Produces(APPLICATION_JSON)
    @PermitAll
    public Response removeLineFromCart(@HeaderParam("subscription-id") String subscriptionId, @PathParam("cartId") String cartId, @PathParam("lineId") String lineId) {
        var item = shopifyService.removeLineFromCart(subscriptionId, cartId, lineId);
        if (item.isOk()) {
            return Response.ok(item.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(item.getError().getMessage()).build();
        }
    }

    @PUT
    @Path("/{cartId}/line/{lineId}")
    @Produces(APPLICATION_JSON)
    @PermitAll
    public Response updateLineQuantity(@HeaderParam("subscription-id") String subscriptionId, @PathParam("cartId") String cartId, @PathParam("lineId") String lineId, @QueryParam("quantity") Integer quantity) {
        var item = shopifyService.updateQuantity(subscriptionId, cartId, lineId, quantity);
        if (item.isOk()) {
            return Response.ok(item.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(item.getError().getMessage()).build();
        }
    }
}
