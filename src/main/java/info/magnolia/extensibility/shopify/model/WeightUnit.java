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

import java.io.IOException;
import com.fasterxml.jackson.annotation.*;

public enum WeightUnit {
    KG, LB;

    @JsonValue
    public String toValue() {
        switch (this) {
            case KG: return "kg";
            case LB: return "lb";
        }
        return null;
    }

    @JsonCreator
    public static WeightUnit forValue(String value) throws IOException {
        if (value.equals("kg")) return KG;
        if (value.equals("lb")) return LB;
        throw new IOException("Cannot deserialize WeightUnit");
    }
}
