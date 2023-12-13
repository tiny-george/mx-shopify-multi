
package info.magnolia.extensibility.shopify.service;

import info.magnolia.extensibility.shopify.client.ShopifyClient;
import info.magnolia.extensibility.shopify.model.Product;
import info.magnolia.response.Response;
import info.magnolia.secrets.api.Secrets;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ShopifyService {

    public static final String TOKEN_KEY = "shopify.token";
    private static final Logger LOGGER = LoggerFactory.getLogger(ShopifyService.class);
    private final ShopifyClient shopifyClient;
    private final Secrets secrets;

    @Inject
    public ShopifyService(@RestClient ShopifyClient shopifyClient, Secrets secrets) {
        this.shopifyClient =  shopifyClient;
        this.secrets = secrets;
    }

    public Response<List<Product>> getItems(String subscriptionId) {
        LOGGER.debug("Calling get all products of subscription: {}", subscriptionId);
        return secrets.subscriptionGet(subscriptionId, TOKEN_KEY)
                .map(shopifyClient::getItems)
                .map(Response::ok)
                .orElseGet(() -> Response.error(new Exception("Not found a shopify token for subscription: " + subscriptionId)));
    }
    public Response<Product> getItem(String subscriptionId, String itemId) {
        LOGGER.debug("Calling get product by id: {} for subscription: {}", itemId, subscriptionId);
        return secrets.subscriptionGet(subscriptionId, TOKEN_KEY)
                .map(token -> shopifyClient.getItem(token, itemId))
                .map(Response::ok)
                .orElseGet(() -> Response.error(new Exception("Not found a shopify token for subscription: " + subscriptionId)));
    }
}
