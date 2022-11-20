package ru.aleksseii.model;

import generated.tables.records.ProductRecord;
import org.jetbrains.annotations.NotNull;

public record Product(int productId,
                      @NotNull String name,
                      @NotNull String internalCode) {

    public Product(@NotNull String name, @NotNull String internalCode) {
        this(0, name, internalCode);
    }

    public Product() {
        this("", "");
    }

    public Product(@NotNull ProductRecord record) {
        this(
                record.getProductId(),
                record.getName(),
                record.getInternalCode()
        );
    }

    public boolean isEmpty() {
        return this.equals(new Product());
    }
}
