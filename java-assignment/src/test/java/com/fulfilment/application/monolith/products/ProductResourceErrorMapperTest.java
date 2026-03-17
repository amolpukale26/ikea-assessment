package com.fulfilment.application.monolith.products;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductResourceErrorMapperTest {

    @Test
    public void testWebApplicationExceptionMapping() {
        ProductResource.ErrorMapper mapper = new ProductResource.ErrorMapper();
        mapper.objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        Exception exception = new WebApplicationException("Not found", 404);

        Response response = mapper.toResponse(exception);

        assertEquals(404, response.getStatus());
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity().toString().contains("exceptionType"));
        assertTrue(response.getEntity().toString().contains("error"));
    }

    @Test
    public void testGenericExceptionMappingDefaults500() {
        ProductResource.ErrorMapper mapper = new ProductResource.ErrorMapper();
        mapper.objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        Exception exception = new RuntimeException("boom");

        Response response = mapper.toResponse(exception);

        assertEquals(500, response.getStatus());
        assertTrue(response.getEntity().toString().contains("exceptionType"));
        assertTrue(response.getEntity().toString().contains("boom"));
    }
}
