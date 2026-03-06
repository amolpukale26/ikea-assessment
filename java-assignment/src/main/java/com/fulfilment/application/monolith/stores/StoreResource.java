package com.fulfilment.application.monolith.stores;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.enterprise.event.Event;

@Path("/stores")
public class StoreResource {

    @Inject
    Event<StoreEvent> storeEvent;

    @Path("{id}")
    @Transactional
    public Response delete(Long id) {
        Store entity = Store.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Store with id of " + id + " does not exist.", 404);
        }
        entity.delete();

        // Emit an event after the transaction is committed
        storeEvent.fire(new StoreEvent(id, StoreEventType.DELETED));

        return Response.status(204).build();
    }
}