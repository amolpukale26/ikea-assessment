package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseRepositoryUnitTest {

    static class StubRepository extends WarehouseRepository {
        DbWarehouse lastPersisted;
        DbWarehouse lastFound;
        long countResult;
        boolean deleted;

        @Override
        public void persist(DbWarehouse entity) {
            lastPersisted = entity;
        }

        @Override
        public PanacheQuery<DbWarehouse> find(String query, Object... params) {
            PanacheQuery<DbWarehouse> queryMock = Mockito.mock(PanacheQuery.class);
            Mockito.when(queryMock.firstResult()).thenAnswer(invocation -> lastFound);
            Mockito.when(queryMock.singleResultOptional()).thenAnswer(invocation -> Optional.ofNullable(lastFound));
            return queryMock;
        }

        @Override
        public long delete(String query, Object... params) {
            deleted = true;
            return 1;
        }

        @Override
        public long count(String query, Object... params) {
            return countResult;
        }

    }

    @Test
    public void testCreateAndExistsAndCountByLocation() {
        StubRepository repo = new StubRepository();

        // create should call persist
        Warehouse warehouse = new Warehouse();
        warehouse.businessUnitCode = "B1";
        warehouse.location = "LOC";
        warehouse.capacity = 10;
        warehouse.stock = 5;
        repo.create(warehouse);
        assertNotNull(repo.lastPersisted);
        assertEquals("B1", repo.lastPersisted.businessUnitCode);

        // exists uses count
        repo.countResult = 1;
        assertTrue(repo.existsByBusinessUnitCode("B1"));

        repo.countResult = 3;
        assertEquals(3, repo.countByLocation("LOC"));
    }

    @Test
    public void testFindUpdateRemoveAndActive() {
        StubRepository repo = new StubRepository();

        // findByBusinessUnitCode throws if not found
        repo.lastFound = null;
        assertThrows(RuntimeException.class, () -> repo.findByBusinessUnitCode("B1"));

        // update should throw if not found
        Warehouse update = new Warehouse();
        update.businessUnitCode = "B1";
        update.location = "LOC2";
        update.capacity = 10;
        update.stock = 2;
        assertThrows(RuntimeException.class, () -> repo.update(update));

        // remove should throw if not found
        assertThrows(RuntimeException.class, () -> repo.remove(update));

        // when found, update should mutate the returned entity
        DbWarehouse existing = new DbWarehouse();
        existing.businessUnitCode = "B1";
        existing.location = "LOC";
        existing.capacity = 10;
        existing.stock = 1;
        repo.lastFound = existing;

        repo.update(update);
        assertEquals("LOC2", existing.location);

        // remove should set archivedAt flag
        repo.remove(update);
        assertNotNull(existing.archivedAt);

        // findActive should throw when missing
        repo.lastFound = null;
        assertThrows(RuntimeException.class, () -> repo.findActiveByBusinessUnitCode("B1"));
    }
}
