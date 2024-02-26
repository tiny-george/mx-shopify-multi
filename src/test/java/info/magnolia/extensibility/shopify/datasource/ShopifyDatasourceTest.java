package info.magnolia.extensibility.shopify.datasource;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.magnolia.datasource.api.*;
import info.magnolia.datasource.http.HttpDatasource;
import info.magnolia.datasource.tck.DatasourceTest;

import info.magnolia.datasource.tck.SearchTest;
import info.magnolia.extensibility.shopify.endpoints.WireMockTestExtension;
import info.magnolia.extensibility.shopify.model.Product;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import java.util.Set;

@QuarkusTest
@QuarkusTestResource(WireMockTestExtension.class)
public class ShopifyDatasourceTest extends DatasourceTest<Product> implements SearchTest<Product> {

    private static final String SUBSCRIPTION_ID = "aSubscriptionId";

    @Inject
    ObjectMapper objectMapper;

    @Override
    public Datasource<Product> newDatasource() {
        return new HttpDatasource<>("shopify", "http://localhost:8081", DatasourceType.EXTENSION,
                Set.of(DatasourceTrait.LIST_ITEMS, DatasourceTrait.ITEM_RESOLVER, DatasourceTrait.SCHEMATICS, DatasourceTrait.SEARCHABLE),
                Product.class, product -> Long.toString(product.getID()), objectMapper);
    }

    @Override
    public Context context() {
        return new SubsContext();
    }

    @Override
    public ObjectMapper objectMapper() {
        return objectMapper;
    }

    @Override
    public String searchTermWithOnlyOneResult() {
        return "Deuter Aircontact 45 + 10 Backpack";
    }

    static class SubsContext implements SubscriptionContext {

        @Override
        public String subscriptionId() {
            return SUBSCRIPTION_ID;
        }
    }
}
