package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.warehouse.api.WarehouseResource;
import com.warehouse.api.beans.Warehouse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@RequestScoped
public class WarehouseResourceImpl implements WarehouseResource {

  @Inject private WarehouseRepository warehouseRepository;

  @Override
  public List<Warehouse> listAllWarehousesUnits() {
    return warehouseRepository.getAll().stream().map(this::toWarehouseResponse).toList();
  }

  @Override
  public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {

      // Business Unit uniqueness
      if (warehouseRepository.existsByBusinessUnitCode(data.getBusinessUnitCode())) {
          throw new RuntimeException("Business Unit already exists");
      }

      // Stock <= Capacity
      if (data.getStock() > data.getCapacity()) {
          throw new RuntimeException("Stock cannot exceed capacity");
      }

      // (Location validation skipped if no Location table exists)

    var domainWarehouse = toDomainWarehouse(data);

    warehouseRepository.create(domainWarehouse);

    return toWarehouseResponse(domainWarehouse);
  }

  @Override
  public com.warehouse.api.beans.Warehouse getAWarehouseUnitByID(String id) {

    var domainWarehouse =
        warehouseRepository.findByBusinessUnitCode(id);

    return toWarehouseResponse(domainWarehouse);
  }

  @Override
  public void archiveAWarehouseUnitByID(String id) {

    var warehouse =
        warehouseRepository.findByBusinessUnitCode(id);

    if (warehouse.archivedAt != null) {
      throw new RuntimeException("Warehouse already archived");
    }

    warehouseRepository.remove(warehouse);
  }

  @Override
  public com.warehouse.api.beans.Warehouse replaceTheCurrentActiveWarehouse(
      String businessUnitCode,
      @NotNull com.warehouse.api.beans.Warehouse data) {

    var current =
        warehouseRepository.findActiveByBusinessUnitCode(businessUnitCode);

    // Capacity Accommodation
    if (data.getCapacity() < current.stock) {
      throw new RuntimeException(
          "New warehouse cannot accommodate existing stock");
    }

    // Stock Matching
    if (!data.getStock().equals(current.stock)) {
      throw new RuntimeException(
          "Stock must match previous warehouse");
    }

    // Archive old warehouse
    warehouseRepository.remove(current);

    // Create new warehouse
    var newDomainWarehouse = toDomainWarehouse(data);
    warehouseRepository.create(newDomainWarehouse);

    return toWarehouseResponse(newDomainWarehouse);
  }


  private com.warehouse.api.beans.Warehouse toWarehouseResponse(
      com.fulfilment.application.monolith.warehouses.domain.models.Warehouse warehouse) {

    var response = new com.warehouse.api.beans.Warehouse();
    response.setBusinessUnitCode(warehouse.businessUnitCode);
    response.setLocation(warehouse.location);
    response.setCapacity(warehouse.capacity);
    response.setStock(warehouse.stock);

    return response;
  }

  private com.fulfilment.application.monolith.warehouses.domain.models.Warehouse
      toDomainWarehouse(com.warehouse.api.beans.Warehouse apiWarehouse) {

    var domain =
        new com.fulfilment.application.monolith.warehouses.domain.models.Warehouse();

    domain.businessUnitCode = apiWarehouse.getBusinessUnitCode();
    domain.location = apiWarehouse.getLocation();
    domain.capacity = apiWarehouse.getCapacity();
    domain.stock = apiWarehouse.getStock();

    return domain;
  }
}
