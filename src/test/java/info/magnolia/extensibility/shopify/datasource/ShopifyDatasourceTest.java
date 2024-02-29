package info.magnolia.extensibility.shopify.datasource;

import info.magnolia.datasource.api.Context;
import info.magnolia.datasource.api.Datasource;
import info.magnolia.datasource.api.DatasourceTrait;
import info.magnolia.datasource.api.DatasourceType;
import info.magnolia.datasource.api.SubscriptionContext;
import info.magnolia.datasource.filter.Filter;
import info.magnolia.datasource.filter.Operator;
import info.magnolia.datasource.http.HttpDatasource;
import info.magnolia.datasource.tck.DatasourceTest;
import info.magnolia.datasource.tck.FilterTest;
import info.magnolia.extensibility.shopify.endpoints.WireMockTestExtension;
import info.magnolia.extensibility.shopify.model.Product;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
@QuarkusTestResource(WireMockTestExtension.class)
public class ShopifyDatasourceTest extends DatasourceTest<Product> implements FilterTest<Product> {

    private static final String SUBSCRIPTION_ID = "aSubscriptionId";
    private static final Map<List<Filter>, Integer> FILTERS_AND_EXPECTED_MATCHING_ELEMENTS = Map.of(
            List.of(new Filter("searchTerm", Operator.EQ, "Especial Medio Cycling Backpack")), 1,
            List.of(new Filter("searchTerm", Operator.EQ, "non-existing")), 0);
    @Inject
    ObjectMapper objectMapper;

    @Override
    public Datasource<Product> newDatasource() {
        return new HttpDatasource<>("shopify", "http://localhost:8081", DatasourceType.EXTENSION,
                Set.of(DatasourceTrait.LIST_ITEMS, DatasourceTrait.ITEM_RESOLVER, DatasourceTrait.SCHEMATICS, DatasourceTrait.FILTERABLE),
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
    public Set<List<Filter>> filterSourceMethodProvider() {
        return FILTERS_AND_EXPECTED_MATCHING_ELEMENTS.keySet();
    }

    @Override
    public int getExpectedSizeForFilter(List<Filter> filters) {
        return FILTERS_AND_EXPECTED_MATCHING_ELEMENTS.get(filters);
    }

    static class SubsContext implements SubscriptionContext {

        @Override
        public String subscriptionId() {
            return SUBSCRIPTION_ID;
        }
    }
}
