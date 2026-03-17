package com.fulfilment.application.monolith.products;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductResourceUnitTest {

    private ProductRepository repository;
    private ProductResource resource;

    @BeforeEach
    public void setup() {
        repository = mock(ProductRepository.class);
        resource = new ProductResource();
        resource.productRepository = repository;
    }

    @Test
    public void testGetReturnsList() {
        when(repository.listAll(any())).thenReturn(List.of(new Product("A")));

        List<Product> result = resource.get();

        assertEquals(1, result.size());
        verify(repository, times(1)).listAll(any());
    }

    @Test
    public void testGetSingleNotFoundThrows() {
        when(repository.findById(5L)).thenReturn(null);

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> resource.getSingle(5L));
        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    public void testCreateWithIdThrows() {
        Product p = new Product("X");
        p.id = 1L;

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> resource.create(p));
        assertEquals(422, ex.getResponse().getStatus());
    }

    @Test
    public void testCreatePersistsAndReturns201() {
        Product p = new Product("X");
        Response response = resource.create(p);

        assertEquals(201, response.getStatus());
        assertSame(p, response.getEntity());
        verify(repository, times(1)).persist(p);
    }

    @Test
    public void testUpdateMissingNameThrows() {
        Product p = new Product();
        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> resource.update(1L, p));
        assertEquals(422, ex.getResponse().getStatus());
    }

    @Test
    public void testUpdateNotFoundThrows() {
        Product p = new Product("X");
        when(repository.findById(1L)).thenReturn(null);

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> resource.update(1L, p));
        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    public void testUpdateSuccessCopiesFields() {
        Product existing = new Product("OLD");
        existing.id = 1L;
        existing.description = "old";
        existing.price = BigDecimal.ONE;
        existing.stock = 5;

        when(repository.findById(1L)).thenReturn(existing);

        Product update = new Product("NEW");
        update.description = "new";
        update.price = BigDecimal.valueOf(10);
        update.stock = 50;

        Product result = resource.update(1L, update);

        assertEquals("NEW", result.name);
        assertEquals("new", result.description);
        assertEquals(BigDecimal.valueOf(10), result.price);
        assertEquals(50, result.stock);
        verify(repository).persist(existing);
    }

    @Test
    public void testDeleteNotFoundThrows() {
        when(repository.findById(123L)).thenReturn(null);

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> resource.delete(123L));
        assertEquals(404, ex.getResponse().getStatus());
    }

    @Test
    public void testDeleteSuccess() {
        Product existing = new Product("X");
        existing.id = 100L;
        when(repository.findById(100L)).thenReturn(existing);

        Response response = resource.delete(100L);
        assertEquals(204, response.getStatus());
        verify(repository).delete(existing);
    }
}
