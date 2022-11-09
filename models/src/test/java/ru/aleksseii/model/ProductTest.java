package ru.aleksseii.model;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class ProductTest {

    private static final int PRODUCT_ID = 1;

    private static final @NotNull String NAME = "default name";

    private static final @NotNull String INTERNAL_CODE = "default internal code";

    @Test
    void shouldCreateDefaultInstance() {

        Product product = new Product(PRODUCT_ID, NAME, INTERNAL_CODE);

        assertAll(
                () -> assertEquals(PRODUCT_ID, product.productId()),
                () -> assertEquals(NAME, product.name()),
                () -> assertEquals(INTERNAL_CODE, product.internalCode())
        );
    }

    @Test
    void shouldCreateNoIdInstance() {

        Product product = new Product(NAME, INTERNAL_CODE);

        assertAll(
                () -> assertEquals(NAME, product.name()),
                () -> assertEquals(INTERNAL_CODE, product.internalCode())
        );
    }

    @Test
    void shouldCreateEmptyInstance() {

        Product product = new Product();

        assertAll(
                () -> assertEquals(0, product.productId()),
                () -> assertEquals("", product.name()),
                () -> assertEquals("", product.internalCode())
        );
    }

    @Test
    void shouldReturnIfInstanceIsEmpty() {

        Product product = new Product();
        assertTrue(product.isEmpty());
    }
}
