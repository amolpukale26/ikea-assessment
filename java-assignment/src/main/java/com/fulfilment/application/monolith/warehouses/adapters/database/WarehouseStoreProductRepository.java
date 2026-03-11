package com.fulfilment.application.monolith.warehouses.adapters.database;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WarehouseStoreProductRepository implements PanacheRepository<DbWarehouseStoreProduct> {

    /** Rule #1: Product can be fulfilled by max 2 warehouses per store */
    public long countWarehousesForStoreAndProduct(Long storeId, String productSku) {
        return count("storeId = ?1 and productSku = ?2", storeId, productSku);
    }

    /** Rule #2: Store can be fulfilled by max 3 warehouses (distinct BU codes) */
    public long countDistinctWarehousesForStore(Long storeId) {
        return getEntityManager()
                .createQuery(
                        "select count(distinct m.warehouseBuCode) " +
                        "from DbWarehouseStoreProduct m " +
                        "where m.storeId = :sid", Long.class)
                .setParameter("sid", storeId)
                .getSingleResult();
    }

    /** Rule #3: Warehouse can store max 5 products (distinct SKUs across all stores) */
    public long countDistinctProductsForWarehouse(String buCode) {
        return getEntityManager()
                .createQuery(
                        "select count(distinct m.productSku) " +
                        "from DbWarehouseStoreProduct m " +
                        "where m.warehouseBuCode = :bu", Long.class)
                .setParameter("bu", buCode)
                .getSingleResult();
    }

    public boolean exists(String buCode, Long storeId, String productSku) {
        return count("warehouseBuCode = ?1 and storeId = ?2 and productSku = ?3",
                buCode, storeId, productSku) > 0;
    }

    public void delete(String buCode, Long storeId, String productSku) {
        delete("warehouseBuCode = ?1 and storeId = ?2 and productSku = ?3",
                buCode, storeId, productSku);
    }
}