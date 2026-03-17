// package com.fulfilment.application.monolith.warehouses.adapters.database;

// import io.quarkus.test.junit.QuarkusTest;
// import jakarta.inject.Inject;
// import jakarta.transaction.Transactional;
// import org.junit.jupiter.api.Test;

// import java.util.List;
// // 
// import static org.junit.jupiter.api.Assertions.*;

// // @QuarkusTest
// public class WarehouseStoreProductRepositoryTest {

//     @Inject
//     WarehouseStoreProductRepository repository;

//     @Inject
//     WarehouseStoreProductRepository repo;

//     @Test
//     @Transactional
//     public void testCreateAndCountRules() {
//         // Prepare sample data
//         DbWarehouseStoreProduct p1 = new DbWarehouseStoreProduct();
//         p1.storeId = 1L;
//         p1.productSku = "SKU1";
//         p1.warehouseBuCode = "BU1";
//         repository.persist(p1);

//         DbWarehouseStoreProduct p2 = new DbWarehouseStoreProduct();
//         p2.storeId = 1L;
//         p2.productSku = "SKU1";
//         p2.warehouseBuCode = "BU2";
//         repository.persist(p2);

//         DbWarehouseStoreProduct p3 = new DbWarehouseStoreProduct();
//         p3.storeId = 1L;
//         p3.productSku = "SKU2";
//         p3.warehouseBuCode = "BU1";
//         repository.persist(p3);

//         // Rule #1: max 2 warehouses per store/product
//         long countWarehouses = repository.countWarehousesForStoreAndProduct(1L, "SKU1");
//         assertEquals(2, countWarehouses);

//         // Rule #2: max 3 distinct warehouses per store
//         long countDistinctWarehouses = repository.countDistinctWarehousesForStore(1L);
//         assertEquals(2, countDistinctWarehouses);

//         // Rule #3: max 5 distinct products per warehouse
//         long countDistinctProducts = repository.countDistinctProductsForWarehouse("BU1");
//         assertEquals(2, countDistinctProducts);

//         // exists() check
//         assertTrue(repository.exists("BU1", 1L, "SKU1"));
//         assertFalse(repository.exists("BU2", 1L, "SKU3"));

//         // delete() check
//         repository.delete("BU1", 1L, "SKU1");
//         assertFalse(repository.exists("BU1", 1L, "SKU1"));
//     }
// }