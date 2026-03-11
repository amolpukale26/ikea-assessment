package com.fulfilment.application.monolith.warehouses.domain.services;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouseStoreProduct;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseStoreProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class WarehouseFulfillmentService {

@Inject
WarehouseStoreProductRepository repo;

/**
* Assign a warehouse (by BU code) to fulfill a product (SKU) for a store.
* Enforces all 3 constraints from the bonus task.
*/
@Transactional
public void assign(String warehouseBuCode, Long storeId, String productSku) {

// Idempotency – nothing to do if already assigned
if (repo.exists(warehouseBuCode, storeId, productSku)) return;

// Rule 1: Each Product can be fulfilled by max 2 Warehouses per Store
if (repo.countWarehousesForStoreAndProduct(storeId, productSku) >= 2) {
throw new IllegalStateException("This product already has 2 warehouses for this store");
}

// Rule 2: Each Store can be fulfilled by max 3 Warehouses (distinct BU codes)
if (repo.countDistinctWarehousesForStore(storeId) >= 3) {
throw new IllegalStateException("Store already has 3 warehouses");
}

// Rule 3: Each Warehouse can store max 5 types of Products (distinct SKUs over all stores)
if (repo.countDistinctProductsForWarehouse(warehouseBuCode) >= 5) {
throw new IllegalStateException("Warehouse already stores 5 product types");
}

repo.persist(new DbWarehouseStoreProduct(warehouseBuCode, storeId, productSku));
}

/** Remove an assignment; no-op if not present. */
@Transactional
public void unassign(String warehouseBuCode, Long storeId, String productSku) {
repo.delete(warehouseBuCode, storeId, productSku);
}
}