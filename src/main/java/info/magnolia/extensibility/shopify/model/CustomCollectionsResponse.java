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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomCollectionsResponse {
    private List<CustomCollection> customCollections;
    @JsonProperty("custom_collections")
    public List<CustomCollection> getCustomCollections() {
        return customCollections;
    }
    @JsonProperty("custom_collections")
    public void setCustomCollections(List<CustomCollection> customCollections) {
        this.customCollections = customCollections;
    }
}
