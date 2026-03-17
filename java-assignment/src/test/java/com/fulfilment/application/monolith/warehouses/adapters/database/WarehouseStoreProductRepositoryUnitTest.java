package com.fulfilment.application.monolith.warehouses.adapters.database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseStoreProductRepositoryUnitTest {

    static class StubWarehouseStoreProductRepository extends WarehouseStoreProductRepository {
        int countCalls;
        int deleteCalls;

        @Override
        public long count(String query, Object... params) {
            countCalls++;
            return 1L;
        }

        @Override
        public long countDistinctWarehousesForStore(Long storeId) {
            countCalls++;
            return 1L;
        }

        @Override
        public long countDistinctProductsForWarehouse(String buCode) {
            countCalls++;
            return 1L;
        }

        @Override
        public boolean exists(String buCode, Long storeId, String productSku) {
            countCalls++;
            return true;
        }

        @Override
        public long delete(String query, Object... params) {
            deleteCalls++;
            return 1;
        }
    }

    @Test
    public void testRulesAndExistsDelete() {
        StubWarehouseStoreProductRepository repo = new StubWarehouseStoreProductRepository();

        assertEquals(1L, repo.countWarehousesForStoreAndProduct(1L, "SKU1"));
        assertEquals(1L, repo.countDistinctWarehousesForStore(1L));
        assertEquals(1L, repo.countDistinctProductsForWarehouse("BU1"));
        assertTrue(repo.exists("BU1", 1L, "SKU1"));

        repo.delete("BU1", 1L, "SKU1");
        assertEquals(1, repo.deleteCalls);
        assertTrue(repo.deleteCalls > 0);
        assertEquals(4, repo.countCalls);
    }
}
