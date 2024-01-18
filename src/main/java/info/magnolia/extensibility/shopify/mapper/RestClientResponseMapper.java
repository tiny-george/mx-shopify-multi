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
package info.magnolia.extensibility.shopify.mapper;

import info.magnolia.extensibility.shopify.exception.NotAuthorizedException;
import info.magnolia.extensibility.shopify.exception.NotFoundException;
import info.magnolia.extensibility.shopify.exception.ShopifyExtensionException;

import java.util.Map;
import java.util.function.Function;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RestClientResponseMapper implements ResponseExceptionMapper<ShopifyExtensionException> {
    private static final Map<Integer, Function<String, ShopifyExtensionException>> EXCEPTION_MAP = Map.of(
            Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), desc -> new ShopifyExtensionException("There was a problem calling remote api", desc, Response.Status.INTERNAL_SERVER_ERROR),
            Response.Status.UNAUTHORIZED.getStatusCode(), desc -> new NotAuthorizedException("There was an authorization problem calling remote api", desc),
            Response.Status.NOT_FOUND.getStatusCode(), desc -> new NotFoundException("Element not found", desc)
    );

    @Override
    public ShopifyExtensionException toThrowable(Response response) {
        return EXCEPTION_MAP.getOrDefault(response.getStatus(), (desc -> new ShopifyExtensionException("There was a problem invoking the remote api", desc, Response.Status.INTERNAL_SERVER_ERROR, null))).apply(response.readEntity(String.class));
    }
}
