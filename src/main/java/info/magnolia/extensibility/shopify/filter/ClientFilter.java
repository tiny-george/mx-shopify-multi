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
package info.magnolia.extensibility.shopify.filter;

import java.net.URI;

import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ClientFilter implements ClientRequestFilter {

    public static final String STORE_PLACEHOLDER = "placeholder";

    private final StoreNameHelper helper;

    @Inject
    public ClientFilter(StoreNameHelper helper) {
        this.helper = helper;
    }

    @Override
    public void filter(ClientRequestContext requestContext) {
        if (helper.getStoreName() != null && requestContext.getUri().toString().contains(STORE_PLACEHOLDER)) {
            requestContext.setUri(URI.create(requestContext.getUri().toString().replace(STORE_PLACEHOLDER, helper.getStoreName())));
        }
    }
}