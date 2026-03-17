package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WarehouseResourceImplTest {

    private WarehouseRepository repository;
    private WarehouseResourceImpl resource;

    @BeforeEach
    public void setUp() throws Exception {
        repository = mock(WarehouseRepository.class);
        resource = new WarehouseResourceImpl();
        java.lang.reflect.Field field = WarehouseResourceImpl.class.getDeclaredField("warehouseRepository");
        field.setAccessible(true);
        field.set(resource, repository);
    }

    private com.warehouse.api.beans.Warehouse apiWarehouse(String bu, String location, int capacity, int stock) {
        com.warehouse.api.beans.Warehouse w = new com.warehouse.api.beans.Warehouse();
        w.setBusinessUnitCode(bu);
        w.setLocation(location);
        w.setCapacity(capacity);
        w.setStock(stock);
        return w;
    }

    @Test
    public void testCreateANewWarehouseUnitValid() {
        com.warehouse.api.beans.Warehouse input = apiWarehouse("BU1", "LOC", 100, 20);
        when(repository.existsByBusinessUnitCode("BU1")).thenReturn(false);

        var result = resource.createANewWarehouseUnit(input);

        assertEquals("BU1", result.getBusinessUnitCode());
        assertEquals("LOC", result.getLocation());
        verify(repository, times(1)).create(any(Warehouse.class));
    }

    @Test
    public void testCreateNewWarehouseUnitDuplicateThrows() {
        com.warehouse.api.beans.Warehouse input = apiWarehouse("BU1", "LOC", 100, 20);
        when(repository.existsByBusinessUnitCode("BU1")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> resource.createANewWarehouseUnit(input));
    }

    @Test
    public void testCreateNewWarehouseUnitStockTooHighThrows() {
        com.warehouse.api.beans.Warehouse input = apiWarehouse("BU2", "LOC", 10, 20);
        when(repository.existsByBusinessUnitCode("BU2")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> resource.createANewWarehouseUnit(input));
    }

    @Test
    public void testArchiveWarehouseAlreadyArchivedThrows() {
        Warehouse existing = new Warehouse();
        existing.businessUnitCode = "BU1";
        existing.archivedAt = java.time.LocalDateTime.now();
        when(repository.findByBusinessUnitCode("BU1")).thenReturn(existing);

        assertThrows(RuntimeException.class, () -> resource.archiveAWarehouseUnitByID("BU1"));
    }

    @Test
    public void testReplaceCurrentActiveWarehouseRules() {
        Warehouse current = new Warehouse();
        current.businessUnitCode = "BU1";
        current.location = "LOC";
        current.capacity = 100;
        current.stock = 20;
        current.archivedAt = null;

        when(repository.findActiveByBusinessUnitCode("BU1")).thenReturn(current);

        com.warehouse.api.beans.Warehouse newWarehouse = apiWarehouse("BU1", "LOC2", 15, 20);
        assertThrows(RuntimeException.class, () -> resource.replaceTheCurrentActiveWarehouse("BU1", newWarehouse));

        com.warehouse.api.beans.Warehouse newWarehouse2 = apiWarehouse("BU1", "LOC2", 100, 10);
        assertThrows(RuntimeException.class, () -> resource.replaceTheCurrentActiveWarehouse("BU1", newWarehouse2));
    }

    @Test
    public void testListAllWarehousesUnits() {
        Warehouse w1 = new Warehouse();
        w1.businessUnitCode = "BU1";
        w1.location = "LOC1";
        w1.capacity = 10;
        w1.stock = 5;

        Warehouse w2 = new Warehouse();
        w2.businessUnitCode = "BU2";
        w2.location = "LOC2";
        w2.capacity = 20;
        w2.stock = 3;

        when(repository.getAll()).thenReturn(java.util.List.of(w1, w2));

        var response = resource.listAllWarehousesUnits();
        assertEquals(2, response.size());
        assertEquals("BU1", response.get(0).getBusinessUnitCode());
    }

    @Test
    public void testGetAWarehouseUnitByID() {
        Warehouse w1 = new Warehouse();
        w1.businessUnitCode = "BU1";
        w1.location = "LOC1";
        w1.capacity = 10;
        w1.stock = 5;

        when(repository.findByBusinessUnitCode("BU1")).thenReturn(w1);

        var response = resource.getAWarehouseUnitByID("BU1");
        assertEquals("LOC1", response.getLocation());
    }

    @Test
    public void testArchiveWarehouseUnit() {
        Warehouse w1 = new Warehouse();
        w1.businessUnitCode = "BU1";
        w1.location = "LOC1";
        w1.capacity = 10;
        w1.stock = 5;
        w1.archivedAt = null;

        when(repository.findByBusinessUnitCode("BU1")).thenReturn(w1);

        assertDoesNotThrow(() -> resource.archiveAWarehouseUnitByID("BU1"));
        verify(repository, times(1)).remove(w1);
    }

    @Test
    public void testReplaceCurrentActiveWarehouseSuccess() {
        Warehouse current = new Warehouse();
        current.businessUnitCode = "BU1";
        current.location = "LOC";
        current.capacity = 100;
        current.stock = 20;
        current.archivedAt = null;

        when(repository.findActiveByBusinessUnitCode("BU1")).thenReturn(current);

        com.warehouse.api.beans.Warehouse next = apiWarehouse("BU1", "LOC2", 100, 20);
        var result = resource.replaceTheCurrentActiveWarehouse("BU1", next);

        assertEquals("LOC2", result.getLocation());
        verify(repository, times(1)).remove(current);
        verify(repository, times(1)).create(any(Warehouse.class));
    }
}
