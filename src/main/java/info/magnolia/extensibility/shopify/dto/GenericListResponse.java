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
package info.magnolia.extensibility.shopify.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("GenericListResponse")
public record GenericListResponse<T> (Integer size, List<T> items){
}

