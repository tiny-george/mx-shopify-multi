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

import info.magnolia.datasource.ecommerce.Product;
import info.magnolia.extensibility.shopify.model.Image;
import info.magnolia.extensibility.shopify.service.ShopifyService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/products")
public class EcommerceProductEndpoints {

    private final ShopifyService shopifyService;

    @Inject
    public EcommerceProductEndpoints(ShopifyService shopifyService) {
        this.shopifyService = shopifyService;
    }

    @GET
    @Produces(APPLICATION_JSON)
    @PermitAll
    public List<Product> allProducts(@HeaderParam("subscription-id") String subscriptionId) {
        var products = shopifyService.getItems(subscriptionId, Map.of());
        if (products.isOk()) {
            return products.get().items().stream()
                    .map(this::toEcommerceProduct)
                    .toList();
        } else {
            throw new RuntimeException(products.getError());
        }
    }

    @GET
    @Path("/byCategory/{categoryId}")
    @Produces(APPLICATION_JSON)
    @PermitAll
    public List<Product> byCategory(@PathParam("categoryId") String categoryId, @HeaderParam("subscription-id") String subscriptionId) {
        var products = shopifyService.getItems(subscriptionId, Map.of("categoryId", categoryId));
        if (products.isOk()) {
            return products.get().items().stream()
                    .map(this::toEcommerceProduct)
                    .toList();
        } else {
            throw new RuntimeException(products.getError());
        }
    }

    @GET
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    @PermitAll
    public Product product(@PathParam("id") String itemId, @HeaderParam("subscription-id") String subscriptionId) {
        var product = shopifyService.getItem(subscriptionId, itemId);
        if (product.isOk()) {
            return toEcommerceProduct(product.get());
        } else {
            throw new RuntimeException(product.getError());
        }
    }

    @GET
    @Path("/search/{text}")
    @Produces(APPLICATION_JSON)
    @PermitAll
    public List<Product> search(@PathParam("text") String text, @HeaderParam("subscription-id") String subscriptionId) {
        var products = shopifyService.getItems(subscriptionId, Map.of("searchTerm", text));
        if (products.isOk()) {
            return products.get().items().stream()
                    .map(this::toEcommerceProduct)
                    .toList();
        } else {
            throw new RuntimeException(products.getError());
        }
    }

    private Product toEcommerceProduct(info.magnolia.extensibility.shopify.model.Product product) {
        return new Product(
                product.getID().toString(),
                product.getHandle(),
                product.getVariants().get(0).getSku(),
                product.getTitle(),
                Float.toString(product.getVariants().get(0).getPrice()),
                product.getStatus().ordinal(),
                0,
                (int) Math.round(product.getVariants().get(0).getWeight()),
                product.getProductType() != null &&
                        !product.getProductType().isEmpty() ? Set.of(product.getProductType()): Set.of(),
                product.getImages().stream()
                        .map(Image::getSrc)
                        .toList()
        );
    }
}
