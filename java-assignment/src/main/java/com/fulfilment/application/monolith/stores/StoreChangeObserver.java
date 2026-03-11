package com.fulfilment.application.monolith.stores;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

/**
 * Observes store changes AFTER the DB transaction commits successfully
 * and triggers the legacy sync.
 */
@ApplicationScoped
public class StoreChangeObserver {

  private static final Logger LOG = Logger.getLogger(StoreChangeObserver.class);

  @Inject
  LegacyStoreManagerGateway legacy;

  void onAfterCommit(@Observes(during = TransactionPhase.AFTER_SUCCESS) StoreChangedEvent evt) {
    try {
      // Build a minimal Store instance to reuse existing gateway signatures
      Store store = new Store();
      store.id = evt.id();
      store.name = evt.name();
      store.quantityProductsInStock = evt.quantityProductsInStock();

      if (evt.type() == StoreEventType.CREATED) {
        legacy.createStoreOnLegacySystem(store);
      } else if (evt.type() == StoreEventType.UPDATED) {
        legacy.updateStoreOnLegacySystem(store);
      }
    } catch (Exception e) {
      // Transaction is already committed; log and consider retry/outbox strategy if
      // needed
      LOG.error("Post-commit legacy sync failed for store id=" + evt.id(), e);
    }
  }
}