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

package info.magnolia.extensibility.shopify.exception;

import org.jboss.resteasy.reactive.RestResponse;

public class NotFoundException extends ShopifyExtensionException{

    public NotFoundException(String title, String detail) {
        super(title, detail, RestResponse.Status.NOT_FOUND);
    }
}