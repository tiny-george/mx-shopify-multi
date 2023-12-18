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
package info.magnolia.extensibility.shopify.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductResponse {
    private Product product;

    @JsonProperty("product")
    public Product getProduct() { return product; }
    @JsonProperty("product")
    public void setProduct(Product value) { this.product = value; }
}
