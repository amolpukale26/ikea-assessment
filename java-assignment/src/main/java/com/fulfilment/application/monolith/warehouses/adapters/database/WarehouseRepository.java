package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

  @Override
  public List<Warehouse> getAll() {
    return this.listAll().stream().map(DbWarehouse::toWarehouse).toList();
  }

  @Override
  public void create(Warehouse warehouse) {

      DbWarehouse entity = DbWarehouse.fromWarehouse(warehouse);
      persist(entity);
  }

  @Override
  public void update(Warehouse warehouse) {

      DbWarehouse entity = find("businessUnitCode", warehouse.businessUnitCode)
              .firstResult();

      if (entity == null) {
          throw new RuntimeException("Warehouse not found");
      }

      entity.location = warehouse.location;
      entity.capacity = warehouse.capacity;
      entity.stock = warehouse.stock;
      entity.archivedAt = warehouse.archivedAt;
  }

  @Override
  public void remove(Warehouse warehouse) {

      DbWarehouse entity = find("businessUnitCode", warehouse.businessUnitCode)
              .firstResult();

      if (entity == null) {
          throw new RuntimeException("Warehouse not found");
      }

      entity.archivedAt = LocalDateTime.now();
  }

  @Override
  public Warehouse findByBusinessUnitCode(String buCode) {

      DbWarehouse entity = find("businessUnitCode", buCode)
              .firstResult();

      if (entity == null) {
          throw new RuntimeException("Warehouse not found");
      }

      return entity.toWarehouse();
  }

  public boolean existsByBusinessUnitCode(String buCode) {
      return count("businessUnitCode", buCode) > 0;
  }

  public long countByLocation(String location) {
      return count("location = ?1 and archivedAt is null", location);
  }

  public Warehouse findActiveByBusinessUnitCode(String buCode) {
      DbWarehouse entity = find("businessUnitCode = ?1 and archivedAt is null", buCode)
              .firstResult();

      if (entity == null) {
          throw new RuntimeException("Active warehouse not found");
      }

      return entity.toWarehouse();
  }
}
