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
        String result = """
                <div class="column is-4">
                    <div class="card is-shady">
                        <div class="card-image has-text-centered">
                            <img src=\"""" +
                product.getImage().getSrc() +
                """
                        "/><div class="column is-4">
                                </div>
                                <div class="card-content">
                                    <div class="content">
                                        <h4>""" +
                product.getTitle() +
                "</h4><p>" +
                product.getBodyHTML().replaceAll("\n", "") +
                "</p><p>Price: " +
                (product.getVariants() != null && !product.getVariants().isEmpty() ? product.getVariants().get(0).getPrice() : "Unknown") +
                """
                        </p>
                                        <p><a href="#">Learn more</a></p>
                                    </div>
                                </div>
                            </div>
                        </div>""";
        return result;
    }
}
