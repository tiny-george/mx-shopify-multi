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

import java.io.Serializable;

import jakarta.ws.rs.core.Response;

public class ShopifyExtensionException extends RuntimeException implements Serializable {
    private final String title;
    private final Response.StatusType status;
    private final String detail;

    public ShopifyExtensionException(String title,  String detail, Response.StatusType status, Throwable cause) {
        super(cause);
        this.title = title;
        this.status = status;
        this.detail = detail;
    }
    public ShopifyExtensionException(String title,  String detail, Response.StatusType status) {
        super();
        this.title = title;
        this.status = status;
        this.detail = detail;
    }
    public String getTitle() {
        return title;
    }

    public Response.StatusType getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }

}