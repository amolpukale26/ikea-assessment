package com.fulfilment.application.monolith.warehouses.adapters.database;

import jakarta.persistence.*;

@Entity
@Table(
name = "warehouse_store_product",
uniqueConstraints = @UniqueConstraint(
columnNames = {"warehouseBuCode", "storeId", "productSku"}
)
)
public class DbWarehouseStoreProduct {

@Id
@GeneratedValue
public Long id;

@Column(nullable = false)
public String warehouseBuCode; // Warehouse identity (BU code) - replacement-proof

@Column(nullable = false)
public Long storeId; // Store.id (PanacheEntity)

@Column(nullable = false)
public String productSku; // Product identity (SKU string)

public DbWarehouseStoreProduct() {}

public DbWarehouseStoreProduct(String warehouseBuCode, Long storeId, String productSku) {
this.warehouseBuCode = warehouseBuCode;
this.storeId = storeId;
this.productSku = productSku;
}
}