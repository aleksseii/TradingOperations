package ru.aleksseii.model;

import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public final class WaybillArticleTest {

    @Test
    void shouldReturnIfInstanceIsEmpty() {
        WaybillArticle emptyWaybillArticle = new WaybillArticle();
        assertTrue(emptyWaybillArticle.isEmpty());

        WaybillArticle waybill = new WaybillArticle(1, 1000L, 3, 2, 5);
        assertFalse(waybill.isEmpty());
    }
}