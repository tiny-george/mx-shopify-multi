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

public enum PublishedScope {
    WEB;

    @JsonValue
    public String toValue() {
        switch (this) {
            case WEB: return "web";
        }
        return null;
    }

    @JsonCreator
    public static PublishedScope forValue(String value) throws IOException {
        if (value.equals("web")) return WEB;
        throw new IOException("Cannot deserialize PublishedScope");
    }
}
