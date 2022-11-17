package ru.aleksseii.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ProductTest {

    @Test
    void shouldReturnIfInstanceIsEmpty() {

        Product emptyProduct = new Product();
        assertTrue(emptyProduct.isEmpty());

        Product product = new Product(1, "name", "code");
        assertFalse(product.isEmpty());
    }
}
