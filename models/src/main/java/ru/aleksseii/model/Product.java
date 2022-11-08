package ru.aleksseii.model;

import org.jetbrains.annotations.NotNull;

public record Product(int productId,
                      @NotNull String name,
                      @NotNull String internalCode) {
}
