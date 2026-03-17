package com.fulfilment.application.monolith.stores;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LegacyStoreManagerGatewayTest {

    @Test
    public void testCreateStoreOnLegacySystemWritesTempFiles() {
        LegacyStoreManagerGateway gateway = new LegacyStoreManagerGateway();
        Store store = new Store("LEGACY_TEST");
        store.quantityProductsInStock = 12;

        assertDoesNotThrow(() -> gateway.createStoreOnLegacySystem(store));
    }

    @Test
    public void testUpdateStoreOnLegacySystemWritesTempFiles() {
        LegacyStoreManagerGateway gateway = new LegacyStoreManagerGateway();
        Store store = new Store("LEGACY_UPDATE");
        store.quantityProductsInStock = 20;

        assertDoesNotThrow(() -> gateway.updateStoreOnLegacySystem(store));
    }
}
