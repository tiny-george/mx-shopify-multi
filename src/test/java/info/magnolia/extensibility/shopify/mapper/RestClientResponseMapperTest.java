package info.magnolia.extensibility.shopify.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import info.magnolia.extensibility.exception.ExtensionException;
import info.magnolia.extensibility.exception.NotAuthorizedException;
import info.magnolia.extensibility.exception.NotFoundException;
import info.magnolia.extensibility.exception.NotValidException;

import org.junit.jupiter.api.Test;

import jakarta.ws.rs.core.Response;

class RestClientResponseMapperTest {

    private static final String AN_ELEMENT = "an element";

    @Test
    void shouldReturnNotFoundExceptionOn404Response() {
        runResponseMapperTest (Response.Status.NOT_FOUND.getStatusCode(), "Element not found", NotFoundException.class);
    }

    @Test
    void shouldReturnNotValidExceptionOn400Response() {
        runResponseMapperTest (Response.Status.BAD_REQUEST.getStatusCode(), "There was a problem invoking the remote api", NotValidException.class);
    }

    @Test
    void shouldReturnNotAuthorizedExceptionOn401Response() {
        runResponseMapperTest (Response.Status.UNAUTHORIZED.getStatusCode(), "There was an authorization problem calling remote api", NotAuthorizedException.class);
    }

    @Test
    void shouldReturnExtensionExceptionOn500Response() {
        runResponseMapperTest (Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "There was a problem calling remote api", ExtensionException.class);
    }

    @Test
    void shouldReturnExtensionExceptionOnAnyOtherResponseCode() {
        runResponseMapperTest (Response.Status.BAD_GATEWAY.getStatusCode(), "There was a problem invoking the remote api", ExtensionException.class);
    }


    private void runResponseMapperTest (int statusCode, String expectedTitle, Class expectedClass){
        Response mockedResponse = mock (Response.class);
        when (mockedResponse.getStatus()).thenReturn(statusCode);
        when (mockedResponse.readEntity(String.class)).thenReturn (AN_ELEMENT);

        RestClientResponseMapper restClientResponseMapper =  new RestClientResponseMapper();

        ExtensionException extensionException = restClientResponseMapper.toThrowable(mockedResponse);

        assertTrue(extensionException.getClass().isAssignableFrom(expectedClass));
        assertEquals(expectedTitle, extensionException.getTitle());
        assertEquals(AN_ELEMENT, extensionException.getDetail());

    }
}