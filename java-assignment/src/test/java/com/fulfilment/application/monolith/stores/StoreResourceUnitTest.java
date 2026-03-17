package com.fulfilment.application.monolith.stores;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@QuarkusTest
@Transactional
public class StoreResourceUnitTest {

    @Inject
    StoreResource resource;

    @Inject
    Event<StoreChangedEvent> storeChangedEvent;

    @BeforeEach
    public void cleanup() {
        Store.deleteAll();
    }

    @Test
    public void testCreateWithIdThrows() {
        Store store = new Store();
        store.id = 123L;
        store.name = "FOO";

        assertThrows(WebApplicationException.class, () -> resource.create(store));
    }

    @Test
    public void testCreateCallsPersistAndFiresEvent() {
        Store store = new Store();
        store.name = "TEST";
        store.quantityProductsInStock = 5;

        var response = resource.create(store);
        assertEquals(201, response.getStatus());
        assertNotNull(store.id);
    }

    @Test
    public void testGetSingleNotFound() {
        assertThrows(WebApplicationException.class, () -> resource.getSingle(999L));
    }

    @Test
    public void testUpdateDoesValidationAndNotFound() {
        Store oldStore = new Store("OLD");
        oldStore.quantityProductsInStock = 1;
        oldStore.persist();

        Store invalid = new Store();
        invalid.quantityProductsInStock = 2;
        assertThrows(WebApplicationException.class, () -> resource.update(oldStore.id, invalid));

        Store updated = new Store();
        updated.name = "UPDATED";
        updated.quantityProductsInStock = 10;
        assertThrows(WebApplicationException.class, () -> resource.update(999L, updated));
    }

    @Test
    public void testPatchUpdatesNameOnlyAndNotFound() {
        Store initial = new Store("STORE1");
        initial.quantityProductsInStock = 20;
        initial.persist();

        Store patch = new Store();
        patch.name = "STORE1-UPDATED";
        patch.quantityProductsInStock = 0; // should keep old value

        Store result = resource.patch(initial.id, patch);
        assertEquals("STORE1-UPDATED", result.name);
        assertEquals(20, result.quantityProductsInStock);

        assertThrows(WebApplicationException.class, () -> resource.patch(999L, patch));
    }

    @Test
    public void testDeleteNotFoundThrows() {
        assertThrows(WebApplicationException.class, () -> resource.delete(999L));
    }

    @Test
    public void testUpdateMutatesAndFiresEvent() {
        Store initial = new Store("OLD");
        initial.quantityProductsInStock = 2;
        initial.persist();

        Store updated = new Store();
        updated.name = "NEW";
        updated.quantityProductsInStock = 10;

        Store result = resource.update(initial.id, updated);
        assertEquals("NEW", result.name);
        assertEquals(10, result.quantityProductsInStock);
    }

    @Test
    public void testDeleteDeletesAndReturnsNoContent() {
        Store initial = new Store("X");
        initial.persist();

        var response = resource.delete(initial.id);
        assertEquals(204, response.getStatus());
        assertNull(Store.findById(initial.id));
    }
}
