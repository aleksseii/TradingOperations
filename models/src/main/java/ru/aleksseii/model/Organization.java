package ru.aleksseii.model;

import org.jetbrains.annotations.NotNull;


public record Organization(int orgId,
                           long inn,
                           @NotNull String name,
                           @NotNull String bankAccount) {
}
