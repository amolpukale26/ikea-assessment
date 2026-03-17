package com.fulfilment.application.monolith.stores;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class StoreChangeObserverTest {

    @Test
    public void testOnAfterCommitCreatedEventCallsLegacyCreate() {
        LegacyStoreManagerGateway mockLegacy = mock(LegacyStoreManagerGateway.class);
        StoreChangeObserver observer = new StoreChangeObserver();
        observer.legacy = mockLegacy;

        StoreChangedEvent event = new StoreChangedEvent(100L, "CREATED", 10, StoreEventType.CREATED);
        observer.onAfterCommit(event);

        verify(mockLegacy, times(1)).createStoreOnLegacySystem(any(Store.class));
        verify(mockLegacy, never()).updateStoreOnLegacySystem(any(Store.class));
    }

    @Test
    public void testOnAfterCommitUpdatedEventCallsLegacyUpdate() {
        LegacyStoreManagerGateway mockLegacy = mock(LegacyStoreManagerGateway.class);
        StoreChangeObserver observer = new StoreChangeObserver();
        observer.legacy = mockLegacy;

        StoreChangedEvent event = new StoreChangedEvent(101L, "UPDATED", 100, StoreEventType.UPDATED);
        observer.onAfterCommit(event);

        verify(mockLegacy, times(1)).updateStoreOnLegacySystem(any(Store.class));
        verify(mockLegacy, never()).createStoreOnLegacySystem(any(Store.class));
    }
}
