package com.fulfilment.application.monolith.stores;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;

public class StoreTransactionObserver {

    @Inject
    LegacyStoreManagerGateway legacyStoreManagerGateway;

    public void onStoreEvent(@Observes(during = TransactionPhase.AFTER_SUCCESS) StoreEvent event) {
        if (event.getEventType() == StoreEventType.DELETED) {
            legacyStoreManagerGateway.notifyStoreDeleted(event.getStoreId());
        }
    }
}