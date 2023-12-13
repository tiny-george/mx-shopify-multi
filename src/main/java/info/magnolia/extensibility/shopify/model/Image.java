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

import com.fasterxml.jackson.annotation.*;
import java.time.OffsetDateTime;
import java.util.List;

public class Image {
    private long id;
    private Object alt;
    private long position;
    private long productID;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String adminGraphqlAPIID;
    private long width;
    private long height;
    private String src;
    private List<Object> variantIDS;

    @JsonProperty("id")
    public long getID() { return id; }
    @JsonProperty("id")
    public void setID(long value) { this.id = value; }

    @JsonProperty("alt")
    public Object getAlt() { return alt; }
    @JsonProperty("alt")
    public void setAlt(Object value) { this.alt = value; }

    @JsonProperty("position")
    public long getPosition() { return position; }
    @JsonProperty("position")
    public void setPosition(long value) { this.position = value; }

    @JsonProperty("product_id")
    public long getProductID() { return productID; }
    @JsonProperty("product_id")
    public void setProductID(long value) { this.productID = value; }

    @JsonProperty("created_at")
    public OffsetDateTime getCreatedAt() { return createdAt; }
    @JsonProperty("created_at")
    public void setCreatedAt(OffsetDateTime value) { this.createdAt = value; }

    @JsonProperty("updated_at")
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    @JsonProperty("updated_at")
    public void setUpdatedAt(OffsetDateTime value) { this.updatedAt = value; }

    @JsonProperty("admin_graphql_api_id")
    public String getAdminGraphqlAPIID() { return adminGraphqlAPIID; }
    @JsonProperty("admin_graphql_api_id")
    public void setAdminGraphqlAPIID(String value) { this.adminGraphqlAPIID = value; }

    @JsonProperty("width")
    public long getWidth() { return width; }
    @JsonProperty("width")
    public void setWidth(long value) { this.width = value; }

    @JsonProperty("height")
    public long getHeight() { return height; }
    @JsonProperty("height")
    public void setHeight(long value) { this.height = value; }

    @JsonProperty("src")
    public String getSrc() { return src; }
    @JsonProperty("src")
    public void setSrc(String value) { this.src = value; }

    @JsonProperty("variant_ids")
    public List<Object> getVariantIDS() { return variantIDS; }
    @JsonProperty("variant_ids")
    public void setVariantIDS(List<Object> value) { this.variantIDS = value; }
}
