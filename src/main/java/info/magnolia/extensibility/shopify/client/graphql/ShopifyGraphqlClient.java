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
import info.magnolia.extensibility.shopify.client.graphql.dto.CartCreateResponse;
import info.magnolia.extensibility.shopify.client.graphql.dto.CartAddLineResponse;
import info.magnolia.extensibility.shopify.client.graphql.dto.Line;
import info.magnolia.extensibility.shopify.client.graphql.dto.Node;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
    private static final String CREATE_CART_GRAPHQL_QUERY_FILE = "/graphql/createCart-query.graphql";
    private static final String ADD_LINE_CART_GRAPHQL_QUERY_FILE = "/graphql/addLineCart-query.graphql";
    private static final String REMOVE_LINE_CART_GRAPHQL_QUERY_FILE = "/graphql/removeLineCart-query.graphql";
    private static final Map<String, Object> EMPTY_MAP = Map.of();
    private static final String GID_SHOPIFY_PRODUCT_VARIANT = "gid://shopify/ProductVariant/%s";
    private static final String GID_SHOPIFY_LINE = "gid://shopify/CartLine/%s?cart=%s";
    private final Map<String, String> queryMap;
    private final String shopifyGraphqlUrl;
    private static final String CART_ID_STRING_TEMPLATE = "gid://shopify/Cart/%s";

    @Inject
    public ShopifyGraphqlClient(
            @ConfigProperty(name = "shopify.graphql.url") String shopifyGraphqlUrl) {
        this.shopifyGraphqlUrl = shopifyGraphqlUrl;
        this.queryMap = Map.of(
                GET_CART_GRAPHQL_QUERY_FILE, readQueryFromFile(GET_CART_GRAPHQL_QUERY_FILE),
                CREATE_CART_GRAPHQL_QUERY_FILE, readQueryFromFile(CREATE_CART_GRAPHQL_QUERY_FILE),
                ADD_LINE_CART_GRAPHQL_QUERY_FILE, readQueryFromFile(ADD_LINE_CART_GRAPHQL_QUERY_FILE),
                REMOVE_LINE_CART_GRAPHQL_QUERY_FILE, readQueryFromFile(REMOVE_LINE_CART_GRAPHQL_QUERY_FILE)
        );
    }

    public Cart getCart(SecretValues secretValues, String cartId) {
        Response response;
        response = doCall(secretValues, queryMap.get(GET_CART_GRAPHQL_QUERY_FILE), Map.of("id", String.format(CART_ID_STRING_TEMPLATE, cartId)));
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

    public Cart createCart(SecretValues secretValues) {
        Response response;
        response = doCall(secretValues, queryMap.get(CREATE_CART_GRAPHQL_QUERY_FILE), EMPTY_MAP);
        if (response.getErrors() != null && !response.getErrors().isEmpty()) {
            throw new ExtensionException(
                    String.format("Remote api returned some errors: %s", response.getErrors().stream().map(GraphQLError::getMessage).collect(Collectors.joining(System.lineSeparator())))
                    , null, EMPTY_MAP, null);
        } else {
            var cartCreateResponse = response.getObject(CartCreateResponse.class, "cartCreate");
            return cartCreateResponse.cart();
        }
    }

    public Cart addLineToCart(SecretValues secretValues, String cartId, String productId, int quantity) {
        Response response;
        response = doCall(secretValues, queryMap.get(ADD_LINE_CART_GRAPHQL_QUERY_FILE), Map.of("cartId", String.format(CART_ID_STRING_TEMPLATE, cartId), "lines", List.of (new Line(String.format(GID_SHOPIFY_PRODUCT_VARIANT, productId), quantity))));
        if (response.getErrors() != null && !response.getErrors().isEmpty()) {
            throw new ExtensionException(
                    String.format("Remote api returned some errors: %s", response.getErrors().stream().map(GraphQLError::getMessage).collect(Collectors.joining(System.lineSeparator())))
                    , null, EMPTY_MAP, null);
        } else {
            var cartCreateResponse = response.getObject(CartAddLineResponse.class, "cartLinesAdd");
            return cartCreateResponse.cart();
        }
    }

    public Cart removeLineFromCart(SecretValues secretValues, String cartId, String lineId) {
        Response response;
        response = doCall(secretValues, queryMap.get(REMOVE_LINE_CART_GRAPHQL_QUERY_FILE), Map.of("cartId", String.format(CART_ID_STRING_TEMPLATE, cartId), "lineIds", List.of(String.format(GID_SHOPIFY_LINE, lineId, cartId))));
        if (response.getErrors() != null && !response.getErrors().isEmpty()) {
            throw new ExtensionException(
                    String.format("Remote api returned some errors: %s", response.getErrors().stream().map(GraphQLError::getMessage).collect(Collectors.joining(System.lineSeparator())))
                    , null, EMPTY_MAP, null);
        } else {
            var cartCreateResponse = response.getObject(CartAddLineResponse.class, "cartLinesRemove");
            return cartCreateResponse.cart();
        }
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
}
