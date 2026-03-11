package com.fulfilment.application.monolith.stores;

/** Simple immutable event carrying the data we need for legacy sync. */
public record StoreChangedEvent(Long id, String name, int quantityProductsInStock, StoreEventType type) {
}