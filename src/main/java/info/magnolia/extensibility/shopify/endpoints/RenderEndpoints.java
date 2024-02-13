package info.magnolia.extensibility.shopify.endpoints;

import info.magnolia.extensibility.shopify.model.Product;
import info.magnolia.extensibility.shopify.service.ShopifyService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.MediaType.TEXT_HTML;

@Path("/render")
public class RenderEndpoints {

    @Inject
    ShopifyService shopifyService;

    @GET
    @Produces(TEXT_HTML)
    @PermitAll
    public Response item(@QueryParam("subscription-id") String subscriptionId,
                         @QueryParam("id") String id) {
        var item = shopifyService.getItem(subscriptionId, id);
        if (item.isOk()) {
            return Response.ok(html(item.get())).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Item not found.").build();
        }
    }

    private String html(Product product) {
        var result = "<html><body>";
        result += "<p>Title:" + product.getTitle() + "</p>";
        result += "<image src=' "+ product.getImage().getSrc() + "'>";
        return result + "</body></html>";
    }
}
