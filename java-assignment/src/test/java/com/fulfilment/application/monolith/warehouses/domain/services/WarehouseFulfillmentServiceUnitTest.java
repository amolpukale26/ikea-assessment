package com.fulfilment.application.monolith.warehouses.domain.services;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseStoreProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseFulfillmentServiceUnitTest {

    @Test
    public void testAssignAndRules() {
        WarehouseStoreProductRepository repo = Mockito.mock(WarehouseStoreProductRepository.class);
        Mockito.when(repo.exists("BU1", 1L, "SKU1")).thenReturn(false);
        Mockito.when(repo.countWarehousesForStoreAndProduct(1L, "SKU1")).thenReturn(0L);
        Mockito.when(repo.countDistinctWarehousesForStore(1L)).thenReturn(0L);
        Mockito.when(repo.countDistinctProductsForWarehouse("BU1")).thenReturn(0L);

        WarehouseFulfillmentService service = new WarehouseFulfillmentService();
        service.repo = repo;

        service.assign("BU1", 1L, "SKU1");
        Mockito.verify(repo).persist(Mockito.any(com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouseStoreProduct.class));

        Mockito.when(repo.exists("BU1", 1L, "SKU1")).thenReturn(true);
        service.assign("BU1", 1L, "SKU1"); // idempotent

        Mockito.when(repo.exists("BU2", 1L, "SKU2")).thenReturn(false);
        Mockito.when(repo.countWarehousesForStoreAndProduct(1L, "SKU2")).thenReturn(2L);
        assertThrows(IllegalStateException.class, () -> service.assign("BU2", 1L, "SKU2"));

        Mockito.when(repo.countWarehousesForStoreAndProduct(1L, "SKU2")).thenReturn(1L);
        Mockito.when(repo.countDistinctWarehousesForStore(1L)).thenReturn(3L);
        assertThrows(IllegalStateException.class, () -> service.assign("BU3", 1L, "SKU2"));

        Mockito.when(repo.countDistinctWarehousesForStore(1L)).thenReturn(1L);
        Mockito.when(repo.countDistinctProductsForWarehouse("BU1")).thenReturn(5L);
        assertThrows(IllegalStateException.class, () -> service.assign("BU1", 2L, "SKU9"));
    }

    @Test
    public void testUnassign() {
        WarehouseStoreProductRepository repo = Mockito.mock(WarehouseStoreProductRepository.class);
        WarehouseFulfillmentService service = new WarehouseFulfillmentService();
        service.repo = repo;

        service.unassign("BU1", 1L, "SKU1");
        Mockito.verify(repo).delete("BU1", 1L, "SKU1");
    }
}
