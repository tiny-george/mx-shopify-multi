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

public class Product {
    private Long id;
    private String title;
    private String bodyHTML;
    private String vendor;
    private String productType;
    private OffsetDateTime createdAt;
    private String handle;
    private OffsetDateTime updatedAt;
    private OffsetDateTime publishedAt;
    private String templateSuffix;
    private PublishedScope publishedScope;
    private String tags;
    private Status status;
    private String adminGraphqlAPIID;
    private List<Variant> variants;
    private List<Option> options;
    private List<Image> images;
    private Image image;

    @JsonProperty("id")
    public Long getID() { return id; }
    @JsonProperty("id")
    public void setID(Long value) { this.id = value; }

    @JsonProperty("title")
    public String getTitle() { return title; }
    @JsonProperty("title")
    public void setTitle(String value) { this.title = value; }

    @JsonProperty("body_html")
    public String getBodyHTML() { return bodyHTML; }
    @JsonProperty("body_html")
    public void setBodyHTML(String value) { this.bodyHTML = value; }

    @JsonProperty("vendor")
    public String getVendor() { return vendor; }
    @JsonProperty("vendor")
    public void setVendor(String value) { this.vendor = value; }

    @JsonProperty("product_type")
    public String getProductType() { return productType; }
    @JsonProperty("product_type")
    public void setProductType(String value) { this.productType = value; }

    @JsonProperty("created_at")
    public OffsetDateTime getCreatedAt() { return createdAt; }
    @JsonProperty("created_at")
    public void setCreatedAt(OffsetDateTime value) { this.createdAt = value; }

    @JsonProperty("handle")
    public String getHandle() { return handle; }
    @JsonProperty("handle")
    public void setHandle(String value) { this.handle = value; }

    @JsonProperty("updated_at")
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    @JsonProperty("updated_at")
    public void setUpdatedAt(OffsetDateTime value) { this.updatedAt = value; }

    @JsonProperty("published_at")
    public OffsetDateTime getPublishedAt() { return publishedAt; }
    @JsonProperty("published_at")
    public void setPublishedAt(OffsetDateTime value) { this.publishedAt = value; }

    @JsonProperty("template_suffix")
    public String getTemplateSuffix() { return templateSuffix; }
    @JsonProperty("template_suffix")
    public void setTemplateSuffix(String value) { this.templateSuffix = value; }

    @JsonProperty("published_scope")
    public PublishedScope getPublishedScope() { return publishedScope; }
    @JsonProperty("published_scope")
    public void setPublishedScope(PublishedScope value) { this.publishedScope = value; }

    @JsonProperty("tags")
    public String getTags() { return tags; }
    @JsonProperty("tags")
    public void setTags(String value) { this.tags = value; }

    @JsonProperty("status")
    public Status getStatus() { return status; }
    @JsonProperty("status")
    public void setStatus(Status value) { this.status = value; }

    @JsonProperty("admin_graphql_api_id")
    public String getAdminGraphqlAPIID() { return adminGraphqlAPIID; }
    @JsonProperty("admin_graphql_api_id")
    public void setAdminGraphqlAPIID(String value) { this.adminGraphqlAPIID = value; }

    @JsonProperty("variants")
    public List<Variant> getVariants() { return variants; }
    @JsonProperty("variants")
    public void setVariants(List<Variant> value) { this.variants = value; }

    @JsonProperty("options")
    public List<Option> getOptions() { return options; }
    @JsonProperty("options")
    public void setOptions(List<Option> value) { this.options = value; }

    @JsonProperty("images")
    public List<Image> getImages() { return images; }
    @JsonProperty("images")
    public void setImages(List<Image> value) { this.images = value; }

    @JsonProperty("image")
    public Image getImage() { return image; }
    @JsonProperty("image")
    public void setImage(Image value) { this.image = value; }
}
