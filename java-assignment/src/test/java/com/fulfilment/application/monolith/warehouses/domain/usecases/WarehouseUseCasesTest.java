package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class WarehouseUseCasesTest {

    @Mock
    WarehouseStore warehouseStore;

    @InjectMocks
    CreateWarehouseUseCase createUseCase;

    @InjectMocks
    ArchiveWarehouseUseCase archiveUseCase;

    @InjectMocks
    ReplaceWarehouseUseCase replaceUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateWarehouseReturnsToStore() {
        Warehouse w = new Warehouse();
        w.businessUnitCode = "BU99";
        createUseCase.create(w);
        verify(warehouseStore, times(1)).create(w);
    }

    @Test
    public void testArchiveWarehouseCallsUpdate() {
        Warehouse w = new Warehouse();
        w.businessUnitCode = "BU99";
        archiveUseCase.archive(w);
        verify(warehouseStore, times(1)).update(w);
    }

    @Test
    public void testReplaceWarehouseCallsUpdate() {
        Warehouse w = new Warehouse();
        w.businessUnitCode = "BU99";
        replaceUseCase.replace(w);
        verify(warehouseStore, times(1)).update(w);
    }
}
