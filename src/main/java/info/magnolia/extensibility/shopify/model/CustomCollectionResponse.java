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

public class CustomCollectionResponse {
    private CustomCollection customCollection;
    @JsonProperty("custom_collection")
    public CustomCollection getCustomCollection() {
        return customCollection;
    }
    @JsonProperty("custom_collection")
    public void setCustomCollection(CustomCollection customCollection) {
        this.customCollection = customCollection;
    }
}
