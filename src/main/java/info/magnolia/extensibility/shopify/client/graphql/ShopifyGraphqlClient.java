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
package info.magnolia.extensibility.shopify.client.graphql;

import info.magnolia.extensibility.exception.ExtensionException;
import info.magnolia.extensibility.exception.NotFoundException;
import info.magnolia.extensibility.shopify.client.SecretValues;
import info.magnolia.extensibility.shopify.client.graphql.dto.Cart;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.smallrye.graphql.client.GraphQLError;
import io.smallrye.graphql.client.Response;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClientBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ShopifyGraphqlClient {
    private static final CharSequence STORE_PLACEHOLDER = "placeholder";
    private static final String GET_CART_GRAPHQL_QUERY_FILE = "/graphql/getCart-query.graphql";
    private String shopifyGraphqlUrl;
    private static final String CART_ID_STRING_TEMPLATE = "gid://shopify/Cart/%s";
    private final String getCartQuery;

    @Inject
    public ShopifyGraphqlClient(
            @ConfigProperty(name = "shopify.graphql.url") String shopifyGraphqlUrl) {
        this.shopifyGraphqlUrl = shopifyGraphqlUrl;
        this.getCartQuery = readQueryFromFile(GET_CART_GRAPHQL_QUERY_FILE);
    }

    public Cart getCart(SecretValues secretValues, String cartId) {
        Response response;
        response = doCall(secretValues, getCartQuery, Map.of("id", String.format(CART_ID_STRING_TEMPLATE, cartId)));
        if (response.getErrors() != null && !response.getErrors().isEmpty()) {
            throw new ExtensionException(
                    String.format("Remote api returned some errors: %s", response.getErrors().stream().map(GraphQLError::getMessage).collect(Collectors.joining(System.lineSeparator())))
                    , null, Map.of("cartId", cartId), null);
        } else {
            var cart = response.getObject(Cart.class, "cart");
            if (cart == null) {
                throw new NotFoundException("Cart not found", String.format("The cart with id -%s- was not found on the store: %s", cartId, secretValues.store()));
            } else {
                return cart;
            }
        }
    }

    private Response doCall(SecretValues secretValues, String query, Map<String, Object> variables) {
        Response response;
        try {
            var dynamicGraphQLClient = DynamicGraphQLClientBuilder.newBuilder().url(getShopifyGraphqlUrl(secretValues.store())).header("Shopify-Storefront-Private-Token", secretValues.token()).build();
            response = dynamicGraphQLClient.executeSync(query, variables);
        } catch (Exception e) {
            throw new ExtensionException("There was a problem calling storefront api", e.getMessage(), variables, e);
        }
        return response;
    }

    private String readQueryFromFile(String fileName) {
        try (var in = ShopifyGraphqlClient.class.getResourceAsStream(fileName)) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException(String.format("There was a problem reading query file: %s", fileName));
        }
    }

    private String getShopifyGraphqlUrl(String storeName) {
        if (shopifyGraphqlUrl.contains(STORE_PLACEHOLDER)) {
            return shopifyGraphqlUrl.replace(STORE_PLACEHOLDER, storeName);
        } else {
            return shopifyGraphqlUrl;
        }
    }
}
