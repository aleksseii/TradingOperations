package ru.aleksseii.model;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;

public record Waybill(int waybillId,
                      @NotNull Date waybillDate,
                      int orgSenderId) {
}
