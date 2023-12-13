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
import java.util.List;

public class Option {
    private long id;
    private long productID;
    private Name name;
    private long position;
    private List<Option1> values;

    @JsonProperty("id")
    public long getID() { return id; }
    @JsonProperty("id")
    public void setID(long value) { this.id = value; }

    @JsonProperty("product_id")
    public long getProductID() { return productID; }
    @JsonProperty("product_id")
    public void setProductID(long value) { this.productID = value; }

    @JsonProperty("name")
    public Name getName() { return name; }
    @JsonProperty("name")
    public void setName(Name value) { this.name = value; }

    @JsonProperty("position")
    public long getPosition() { return position; }
    @JsonProperty("position")
    public void setPosition(long value) { this.position = value; }

    @JsonProperty("values")
    public List<Option1> getValues() { return values; }
    @JsonProperty("values")
    public void setValues(List<Option1> value) { this.values = value; }
}
