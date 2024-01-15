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

import info.magnolia.response.Response;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SchematicService {
    public static final String JSON_SCHEMA_PATH = "/schema/product.json";
    private static final Logger LOGGER = LoggerFactory.getLogger(SchematicService.class);

    public Response<String> getSchematic (){
        try (InputStream resourceStream = this.getClass().getResourceAsStream(JSON_SCHEMA_PATH)) {
            return Response.ok(new String(resourceStream.readAllBytes(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error("There was an error retrieving json schema from {}.", JSON_SCHEMA_PATH, e);
            return Response.error(e);
        }
    }
}
