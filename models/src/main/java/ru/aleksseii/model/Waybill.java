package ru.aleksseii.model;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;

public record Waybill(int waybillId,
                      @NotNull Date waybillDate,
                      int orgSenderId) {

    public Waybill(@NotNull Date waybillDate, int orgSenderId) {
        this(0, waybillDate, orgSenderId);
    }

    public Waybill() {
        this(0, new Date(0), 0);
    }

    public boolean isEmpty() {
        return this.equals(new Waybill());
    }
}
