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

public class Variant {
    private long id;
    private long productID;
    private Option1 title;
    private Float price;
    private String sku;
    private long position;
    private InventoryPolicy inventoryPolicy;
    private String compareAtPrice;
    private FulfillmentService fulfillmentService;
    private InventoryManagement inventoryManagement;
    private Option1 option1;
    private Object option2;
    private Object option3;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private boolean taxable;
    private String barcode;
    private long grams;
    private Object imageID;
    private double weight;
    private WeightUnit weightUnit;
    private long inventoryItemID;
    private long inventoryQuantity;
    private long oldInventoryQuantity;
    private boolean requiresShipping;
    private String adminGraphqlAPIID;

    @JsonProperty("id")
    public long getID() { return id; }
    @JsonProperty("id")
    public void setID(long value) { this.id = value; }

    @JsonProperty("product_id")
    public long getProductID() { return productID; }
    @JsonProperty("product_id")
    public void setProductID(long value) { this.productID = value; }

    @JsonProperty("title")
    public Option1 getTitle() { return title; }
    @JsonProperty("title")
    public void setTitle(Option1 value) { this.title = value; }

    @JsonProperty("price")
    public Float getPrice() { return price; }
    @JsonProperty("price")
    public void setPrice(Float value) { this.price = value; }

    @JsonProperty("sku")
    public String getSku() { return sku; }
    @JsonProperty("sku")
    public void setSku(String value) { this.sku = value; }

    @JsonProperty("position")
    public long getPosition() { return position; }
    @JsonProperty("position")
    public void setPosition(long value) { this.position = value; }

    @JsonProperty("inventory_policy")
    public InventoryPolicy getInventoryPolicy() { return inventoryPolicy; }
    @JsonProperty("inventory_policy")
    public void setInventoryPolicy(InventoryPolicy value) { this.inventoryPolicy = value; }

    @JsonProperty("compare_at_price")
    public String getCompareAtPrice() { return compareAtPrice; }
    @JsonProperty("compare_at_price")
    public void setCompareAtPrice(String value) { this.compareAtPrice = value; }

    @JsonProperty("fulfillment_service")
    public FulfillmentService getFulfillmentService() { return fulfillmentService; }
    @JsonProperty("fulfillment_service")
    public void setFulfillmentService(FulfillmentService value) { this.fulfillmentService = value; }

    @JsonProperty("inventory_management")
    public InventoryManagement getInventoryManagement() { return inventoryManagement; }
    @JsonProperty("inventory_management")
    public void setInventoryManagement(InventoryManagement value) { this.inventoryManagement = value; }

    @JsonProperty("option1")
    public Option1 getOption1() { return option1; }
    @JsonProperty("option1")
    public void setOption1(Option1 value) { this.option1 = value; }

    @JsonProperty("option2")
    public Object getOption2() { return option2; }
    @JsonProperty("option2")
    public void setOption2(Object value) { this.option2 = value; }

    @JsonProperty("option3")
    public Object getOption3() { return option3; }
    @JsonProperty("option3")
    public void setOption3(Object value) { this.option3 = value; }

    @JsonProperty("created_at")
    public OffsetDateTime getCreatedAt() { return createdAt; }
    @JsonProperty("created_at")
    public void setCreatedAt(OffsetDateTime value) { this.createdAt = value; }

    @JsonProperty("updated_at")
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    @JsonProperty("updated_at")
    public void setUpdatedAt(OffsetDateTime value) { this.updatedAt = value; }

    @JsonProperty("taxable")
    public boolean getTaxable() { return taxable; }
    @JsonProperty("taxable")
    public void setTaxable(boolean value) { this.taxable = value; }

    @JsonProperty("barcode")
    public String getBarcode() { return barcode; }
    @JsonProperty("barcode")
    public void setBarcode(String value) { this.barcode = value; }

    @JsonProperty("grams")
    public long getGrams() { return grams; }
    @JsonProperty("grams")
    public void setGrams(long value) { this.grams = value; }

    @JsonProperty("image_id")
    public Object getImageID() { return imageID; }
    @JsonProperty("image_id")
    public void setImageID(Object value) { this.imageID = value; }

    @JsonProperty("weight")
    public double getWeight() { return weight; }
    @JsonProperty("weight")
    public void setWeight(double value) { this.weight = value; }

    @JsonProperty("weight_unit")
    public WeightUnit getWeightUnit() { return weightUnit; }
    @JsonProperty("weight_unit")
    public void setWeightUnit(WeightUnit value) { this.weightUnit = value; }

    @JsonProperty("inventory_item_id")
    public long getInventoryItemID() { return inventoryItemID; }
    @JsonProperty("inventory_item_id")
    public void setInventoryItemID(long value) { this.inventoryItemID = value; }

    @JsonProperty("inventory_quantity")
    public long getInventoryQuantity() { return inventoryQuantity; }
    @JsonProperty("inventory_quantity")
    public void setInventoryQuantity(long value) { this.inventoryQuantity = value; }

    @JsonProperty("old_inventory_quantity")
    public long getOldInventoryQuantity() { return oldInventoryQuantity; }
    @JsonProperty("old_inventory_quantity")
    public void setOldInventoryQuantity(long value) { this.oldInventoryQuantity = value; }

    @JsonProperty("requires_shipping")
    public boolean getRequiresShipping() { return requiresShipping; }
    @JsonProperty("requires_shipping")
    public void setRequiresShipping(boolean value) { this.requiresShipping = value; }

    @JsonProperty("admin_graphql_api_id")
    public String getAdminGraphqlAPIID() { return adminGraphqlAPIID; }
    @JsonProperty("admin_graphql_api_id")
    public void setAdminGraphqlAPIID(String value) { this.adminGraphqlAPIID = value; }
}
