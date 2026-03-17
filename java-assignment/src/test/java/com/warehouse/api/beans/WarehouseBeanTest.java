package com.warehouse.api.beans;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseBeanTest {

    @Test
    public void testBeanGettersSetters() {
        Warehouse w = new Warehouse();
        w.setId("id-1");
        w.setBusinessUnitCode("BU1");
        w.setLocation("LOC");
        w.setCapacity(10);
        w.setStock(5);

        assertEquals("id-1", w.getId());
        assertEquals("BU1", w.getBusinessUnitCode());
        assertEquals("LOC", w.getLocation());
        assertEquals(10, w.getCapacity());
        assertEquals(5, w.getStock());
    }
}
