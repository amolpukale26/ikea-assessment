package com.fulfilment.application.monolith.warehouses.adapters.database;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class WarehouseStoreProductRepositoryQuarkusTest {

    @Inject
    WarehouseStoreProductRepository repository;

    @Test
    @Transactional
    public void testRepositoryRulesAndQueries() {
        repository.persist(new DbWarehouseStoreProduct("BU1", 1L, "SKU1"));
        repository.persist(new DbWarehouseStoreProduct("BU2", 1L, "SKU1"));
        repository.persist(new DbWarehouseStoreProduct("BU1", 1L, "SKU2"));

        assertEquals(2L, repository.countWarehousesForStoreAndProduct(1L, "SKU1"));
        assertEquals(2L, repository.countDistinctWarehousesForStore(1L));
        assertEquals(2L, repository.countDistinctProductsForWarehouse("BU1"));
        assertTrue(repository.exists("BU1", 1L, "SKU1"));

        repository.delete("BU1", 1L, "SKU1");
        assertFalse(repository.exists("BU1", 1L, "SKU1"));
    }
}
