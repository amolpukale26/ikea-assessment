package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DbWarehouseTest {

    @Test
    public void testFromWarehouseAndToWarehouseRoundTrip() {
        Warehouse domain = new Warehouse();
        domain.businessUnitCode = "BU-001";
        domain.location = "LOC";
        domain.capacity = 100;
        domain.stock = 20;
        domain.archivedAt = LocalDateTime.now();

        DbWarehouse entity = DbWarehouse.fromWarehouse(domain);
        assertNotNull(entity.createdAt);
        assertEquals(domain.archivedAt, entity.archivedAt);

        Warehouse roundTrip = entity.toWarehouse();
        assertEquals(domain.businessUnitCode, roundTrip.businessUnitCode);
        assertEquals(domain.location, roundTrip.location);
        assertEquals(domain.capacity, roundTrip.capacity);
        assertEquals(domain.stock, roundTrip.stock);
        assertEquals(entity.archivedAt, roundTrip.archivedAt);
    }
}
