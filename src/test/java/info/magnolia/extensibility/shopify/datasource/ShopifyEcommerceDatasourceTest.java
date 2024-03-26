package info.magnolia.extensibility.shopify.datasource;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.magnolia.datasource.api.*;
import info.magnolia.datasource.ecommerce.Ecommerce;
import info.magnolia.datasource.http.HttpEcommerceDatasource;
import info.magnolia.datasource.tck.DatasourceEcommerceTest;
import info.magnolia.extensibility.shopify.endpoints.WireMockTestExtension;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import java.util.Set;

@QuarkusTest
@QuarkusTestResource(WireMockTestExtension.class)
public class ShopifyEcommerceDatasourceTest extends DatasourceEcommerceTest {

    private static final String SUBSCRIPTION_ID = "aSubscriptionId";

    @Inject
    ObjectMapper objectMapper;

    @Override
    public Datasource newDatasource() {
        return new HttpEcommerceDatasource("shopify-ecommerce", "http://localhost:8081", DatasourceType.ECOMMERCE,
                Set.of(Ecommerce.NAME), objectMapper);
    }

    @Override
    public Context context() {
        return new SubsContext();
    }

    @Override
    public ObjectMapper objectMapper() {
        return objectMapper;
    }

    static class SubsContext implements SubscriptionContext {

        @Override
        public String subscriptionId() {
            return SUBSCRIPTION_ID;
        }
    }
}
