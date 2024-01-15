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

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import info.magnolia.extensibility.shopify.service.SchematicService;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/schematic.json")
public class SchematicEndpoints {

    private final SchematicService schematicService;

    @Inject
    public SchematicEndpoints(SchematicService schematicService) {
        this.schematicService = schematicService;
    }

    @GET
    @Produces(APPLICATION_JSON)
    @PermitAll
    public Response getSchematic() {
        var response = schematicService.getSchematic();
        if (response.isOk()) {
            return Response.ok(response.get()).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response.getError().getMessage()).build();
        }
    }
}
