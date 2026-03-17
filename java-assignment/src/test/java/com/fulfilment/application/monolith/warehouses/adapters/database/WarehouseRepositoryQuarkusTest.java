package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class WarehouseRepositoryQuarkusTest {

    @Inject
    WarehouseRepository repository;

    @Test
    @Transactional
    public void testCreateFindAndUpdateAndDelete() {
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "BU-1";
        warehouse.location = "LOC-1";
        warehouse.capacity = 100;
        warehouse.stock = 10;

        repository.create(warehouse);

        Warehouse found = repository.findByBusinessUnitCode("BU-1");
        assertNotNull(found);
        assertEquals("LOC-1", found.location);

        assertTrue(repository.existsByBusinessUnitCode("BU-1"));
        assertEquals(1, repository.countByLocation("LOC-1"));

        found.stock = 20;
        repository.update(found);
        Warehouse updated = repository.findByBusinessUnitCode("BU-1");
        assertEquals(20, updated.stock);

        repository.remove(updated);
        Warehouse archived = repository.findByBusinessUnitCode("BU-1");
        assertNotNull(archived);
        assertNotNull(archived.archivedAt);
    }

    @Test
    @Transactional
    public void testFindActiveByBusinessUnitCode() {
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "BU-2";
        warehouse.location = "LOC-2";
        warehouse.capacity = 90;
        warehouse.stock = 5;

        repository.create(warehouse);
        Warehouse active = repository.findActiveByBusinessUnitCode("BU-2");
        assertNotNull(active);
        assertEquals("BU-2", active.businessUnitCode);
    }
}
