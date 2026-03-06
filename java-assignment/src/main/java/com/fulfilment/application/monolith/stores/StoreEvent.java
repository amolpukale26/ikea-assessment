package com.fulfilment.application.monolith.stores;

public class StoreEvent {
    private final Long storeId;
    private final StoreEventType eventType;

    public StoreEvent(Long storeId, StoreEventType eventType) {
        this.storeId = storeId;
        this.eventType = eventType;
    }

    public Long getStoreId() {
        return storeId;
    }

    public StoreEventType getEventType() {
        return eventType;
    }
}