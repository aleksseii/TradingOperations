package ru.aleksseii.model;

import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public final class WaybillTest {

    @Test
    void shouldReturnIfInstanceIsEmpty() {

        Waybill emptyWaybill = new Waybill();
        assertTrue(emptyWaybill.isEmpty());

        Waybill waybill = new Waybill(1, Date.valueOf("2022-11-09"), 1);
        assertFalse(waybill.isEmpty());
    }
}