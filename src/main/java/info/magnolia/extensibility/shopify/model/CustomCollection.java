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

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomCollection {
    private long id;
    private String handle;
    private String title;
    private OffsetDateTime updatedAt;
    private String bodyHTML;
    private OffsetDateTime publishedAt;
    private String sortOrder;
    private Object templateSuffix;
    private String publishedScope;
    private String adminGraphqlAPIID;
    private Image image;

    @JsonProperty("id")
    public long getId() { return id; }
    @JsonProperty("id")
    public void setId(long value) { this.id = value; }

    @JsonProperty("handle")
    public String getHandle() { return handle; }
    @JsonProperty("handle")
    public void setHandle(String value) { this.handle = value; }

    @JsonProperty("title")
    public String getTitle() { return title; }
    @JsonProperty("title")
    public void setTitle(String value) { this.title = value; }

    @JsonProperty("updated_at")
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    @JsonProperty("updated_at")
    public void setUpdatedAt(OffsetDateTime value) { this.updatedAt = value; }

    @JsonProperty("body_html")
    public String getBodyHTML() { return bodyHTML; }
    @JsonProperty("body_html")
    public void setBodyHTML(String value) { this.bodyHTML = value; }

    @JsonProperty("published_at")
    public OffsetDateTime getPublishedAt() { return publishedAt; }
    @JsonProperty("published_at")
    public void setPublishedAt(OffsetDateTime value) { this.publishedAt = value; }

    @JsonProperty("sort_order")
    public String getSortOrder() { return sortOrder; }
    @JsonProperty("sort_order")
    public void setSortOrder(String value) { this.sortOrder = value; }

    @JsonProperty("template_suffix")
    public Object getTemplateSuffix() { return templateSuffix; }
    @JsonProperty("template_suffix")
    public void setTemplateSuffix(Object value) { this.templateSuffix = value; }

    @JsonProperty("published_scope")
    public String getPublishedScope() { return publishedScope; }
    @JsonProperty("published_scope")
    public void setPublishedScope(String value) { this.publishedScope = value; }

    @JsonProperty("admin_graphql_api_id")
    public String getAdminGraphqlAPIID() { return adminGraphqlAPIID; }
    @JsonProperty("admin_graphql_api_id")
    public void setAdminGraphqlAPIID(String value) { this.adminGraphqlAPIID = value; }

    @JsonProperty("image")
    public Image getImage() { return image; }
    @JsonProperty("image")
    public void setImage(Image value) { this.image = value; }
}