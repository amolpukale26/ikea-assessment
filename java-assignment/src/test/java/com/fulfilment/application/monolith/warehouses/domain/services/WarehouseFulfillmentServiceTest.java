package com.fulfilment.application.monolith.warehouses.domain.services;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouseStoreProduct;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseStoreProductRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class WarehouseFulfillmentServiceTest {

    @Inject
    WarehouseFulfillmentService service;

    @Inject
    WarehouseStoreProductRepository repo;

    @Test
    @Transactional
    public void testAssignWithRules() {
        service.assign("BU1", 1L, "SKU1");
        service.assign("BU2", 1L, "SKU1");
        assertTrue(repo.exists("BU1", 1L, "SKU1"));
        assertTrue(repo.exists("BU2", 1L, "SKU1"));
        assertThrows(IllegalStateException.class, () -> service.assign("BU3", 1L, "SKU1"));

        service.assign("BUA", 2L, "SKU1");
        service.assign("BUB", 2L, "SKU2");
        service.assign("BUC", 2L, "SKU3");
        assertThrows(IllegalStateException.class, () -> service.assign("BUD", 2L, "SKU4"));

        service.assign("BUX", 3L, "SKU1");
        service.assign("BUX", 4L, "SKU2");
        service.assign("BUX", 5L, "SKU3");
        service.assign("BUX", 6L, "SKU4");
        service.assign("BUX", 7L, "SKU5");
        assertThrows(IllegalStateException.class, () -> service.assign("BUX", 8L, "SKU6"));
    }

    @Test
    @Transactional
    public void testUnassignNoOp() {
        // Should not throw if missing
        service.unassign("BU1", 111L, "MISSING");
    }
}
