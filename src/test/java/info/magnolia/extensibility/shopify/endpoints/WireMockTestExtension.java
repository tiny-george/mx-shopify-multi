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
package info.magnolia.extensibility.shopify.endpoints;

import java.util.Map;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class WireMockTestExtension implements QuarkusTestResourceLifecycleManager {
    private WireMockServer wireMockServer;
    @Override
    public Map<String, String> start() {
        wireMockServer = new WireMockServer(0);
        wireMockServer.start();

        return Map.of(
                "quarkus.rest-client.secrets.url", wireMockServer.baseUrl(),
                "quarkus.rest-client.shopify.url", wireMockServer.baseUrl(),
                "shopify.graphql.url", wireMockServer.baseUrl()+"/graphql"

        );
    }

    @Override
    public void stop() {
        if (null != wireMockServer) {
            wireMockServer.stop();
        }
    }
}
