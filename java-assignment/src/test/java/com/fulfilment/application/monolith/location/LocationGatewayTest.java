package com.fulfilment.application.monolith.location;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LocationGatewayTest {

    private final LocationGateway gateway = new LocationGateway();

    @Test
    public void testWhenResolveExistingLocationShouldReturn() {
        Location location = gateway.resolveByIdentifier("ZWOLLE-001");

        assertNotNull(location);
        assertEquals("ZWOLLE-001", location.getIdentification());
        assertEquals(1, location.getMaxNumberOfWarehouses());
        assertEquals(40, location.getMaxCapacity());
    }

    @Test
    public void testWhenResolveNonExistingLocationShouldReturnNull() {
        Location location = gateway.resolveByIdentifier("UNKNOWN-001");

        assertNull(location);
    }

    @Test
    public void testWhenIdentifierIsNullShouldReturnNull() {
        Location location = gateway.resolveByIdentifier(null);

        assertNull(location);
    }

    @Test
    public void testWhenIdentifierIsEmptyShouldReturnNull() {
        Location location = gateway.resolveByIdentifier("");

        assertNull(location);
    }

    @Test
    public void testWhenIdentifierHasSpacesShouldStillResolve() {
        Location location = gateway.resolveByIdentifier("  ZWOLLE-001  ");

        assertNotNull(location);
        assertEquals("ZWOLLE-001", location.getIdentification());
    }
}