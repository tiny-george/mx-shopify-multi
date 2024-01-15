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
package info.magnolia.extensibility.shopify.endpoints;

import static org.junit.jupiter.api.Assertions.assertEquals;

import info.magnolia.extensibility.shopify.service.SchematicService;
import info.magnolia.response.Response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


class SchematicEndpointsTest {

    private static final String THIS_ERROR_HAS_BEEN_FORCED = "This error has been forced";
    private static final String BASIC_JSON_RESPONSE = "{\"key\":\"value\"}";

    @Test
    @DisplayName("Should return a map")
    void getMapWithoutAnyProblem() {
        var schematicService = Mockito.mock(SchematicService.class);
        Mockito.when(schematicService.getSchematic()).thenReturn(Response.ok(BASIC_JSON_RESPONSE));
        SchematicEndpoints schematicEndpoints = new SchematicEndpoints(schematicService);

        var schematicResponse = schematicEndpoints.getSchematic();

        assertEquals(jakarta.ws.rs.core.Response.Status.OK.getStatusCode(), schematicResponse.getStatus());
    }

    @Test
    @DisplayName("Should return an internal server error if any problem")
    void shouldReturnInternalServerError() {
        var schematicService = Mockito.mock(SchematicService.class);
        Mockito.when(schematicService.getSchematic()).thenReturn(Response.error(new Exception(THIS_ERROR_HAS_BEEN_FORCED)));
        SchematicEndpoints schematicEndpoints = new SchematicEndpoints(schematicService);

        var schematicResponse = schematicEndpoints.getSchematic();

        assertEquals(jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), schematicResponse.getStatus());

        assertEquals(THIS_ERROR_HAS_BEEN_FORCED, schematicResponse.getEntity().toString());
    }
}