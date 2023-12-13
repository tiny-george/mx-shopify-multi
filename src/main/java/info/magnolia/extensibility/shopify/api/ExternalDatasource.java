package info.magnolia.extensibility.shopify.api;

import info.magnolia.response.Response;

import java.util.List;

public interface ExternalDatasource<T, ID> {
    Response<List<T>> all();
    Response<T> byId(ID id);
}
