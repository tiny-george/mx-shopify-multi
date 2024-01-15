/*
 * This file Copyright (c) 2024 Magnolia International
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
package info.magnolia.extensibility.shopify.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

class SchematicServiceTest {

    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final String[] FIRST_LEVEL_PROPERTY_NAMES_ARRAY = {
            "adminGraphqlAPIID",
            "bodyHTML",
            "createdAt",
            "handle",
            "id",
            "image",
            "images",
            "options",
            "productType",
            "publishedAt",
            "publishedScope",
            "status",
            "tags",
            "templateSuffix",
            "title",
            "updatedAt",
            "variants",
            "vendor" };

    @Test
    @DisplayName("Should return the json schema without any error")
    void returnsJsonSchemaOk() throws JsonProcessingException {
        SchematicService schematicService = new SchematicService();

        var schemaAsStringResponse = schematicService.getSchematic();


        assertTrue(schemaAsStringResponse.isOk());
        Map<String, Object> jsonSchemaMap = OBJECT_MAPPER.readValue(schemaAsStringResponse.get(), new TypeReference<>() {});
        Map<String, Object> properties = (Map<String, Object>) jsonSchemaMap.get("properties");

        assertEquals(FIRST_LEVEL_PROPERTY_NAMES_ARRAY.length, properties.size());
        assertEquals(
                Set.of(FIRST_LEVEL_PROPERTY_NAMES_ARRAY),
                properties.keySet()
        );
    }
}