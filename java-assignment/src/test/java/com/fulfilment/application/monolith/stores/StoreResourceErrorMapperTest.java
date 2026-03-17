package com.fulfilment.application.monolith.stores;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StoreResourceErrorMapperTest {

    @Test
    public void testWebApplicationExceptionMapping() {
        StoreResource.ErrorMapper mapper = new StoreResource.ErrorMapper();
        mapper.objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        Exception exception = new WebApplicationException("Store missing", 404);

        Response response = mapper.toResponse(exception);

        assertEquals(404, response.getStatus());
        assertTrue(response.getEntity().toString().contains("exceptionType"));
        assertTrue(response.getEntity().toString().contains("Store missing"));
    }

    @Test
    public void testGenericExceptionMappingDefaults500() {
        StoreResource.ErrorMapper mapper = new StoreResource.ErrorMapper();
        mapper.objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        Exception exception = new IllegalStateException("unknown");

        Response response = mapper.toResponse(exception);

        assertEquals(500, response.getStatus());
        assertTrue(response.getEntity().toString().contains("exceptionType"));
    }
}
