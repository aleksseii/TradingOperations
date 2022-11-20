package ru.aleksseii.dao;

import com.zaxxer.hikari.HikariDataSource;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.aleksseii.database.DataSourceManager;
import ru.aleksseii.database.FlywayInitializer;
import ru.aleksseii.model.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class ProductDAOTest {

    private static final int EXISTING_ID1 = 1;
    private static final int EXISTING_ID2 = 2;

    private static final @NotNull String EXISTING_NAME1 = "product_1";

    private static final @NotNull String EXISTING_INTERNAL_CODE1 = "code_1";
    private static final @NotNull String EXISTING_INTERNAL_CODE2 = "code_2";

    private static final @NotNull Product EXISTING_PRODUCT1 =
            new Product(EXISTING_ID1, EXISTING_NAME1, EXISTING_INTERNAL_CODE1);
    private static final @NotNull Product EXISTING_PRODUCT2 =
            new Product(EXISTING_ID2, EXISTING_NAME1, EXISTING_INTERNAL_CODE2);

    private static final @NotNull List<@NotNull Product> ALL_PRODUCTS = List.of(
            EXISTING_PRODUCT1,
            EXISTING_PRODUCT2,
            new Product(3, "product_3", "code_3"),
            new Product(4, "product_4", "code_4"),
            new Product(5, "product_5", "code_5")
    );

    private static final @NotNull HikariDataSource DATA_SOURCE = DataSourceManager.getHikariDataSource();

    private final @NotNull ProductDAO productDAO = new ProductDAO(DATA_SOURCE);

    @AfterAll
    static void closeDataSource() {

        FlywayInitializer.initDB();
        DATA_SOURCE.close();
    }

    @BeforeEach
    void initDB() {
        FlywayInitializer.initDB();
    }

    @Test
    @DisplayName("Should get single Product instance by provided id")
    void shouldGetById() {

        Product product = productDAO.get(EXISTING_ID1);
        assertEquals(EXISTING_PRODUCT1, product);
    }

    @Test
    @DisplayName("Should get list of Product instances by provided name")
    void shouldGetByName() {

        List<@NotNull Product> expected = List.of(EXISTING_PRODUCT1, EXISTING_PRODUCT2);
        List<@NotNull Product> products = productDAO.get(EXISTING_NAME1);

        MatcherAssert.assertThat(expected, Matchers.containsInAnyOrder(products.toArray()));
    }

    @Test
    @DisplayName("Should get list of all Product instances")
    void shouldGetAllProducts() {

        List<@NotNull Product> products = productDAO.all();

        MatcherAssert.assertThat(ALL_PRODUCTS, Matchers.containsInAnyOrder(products.toArray()));
    }

    @Test
    @DisplayName("Should update Product instance")
    void shouldUpdateProduct() {

        int id = 3;
        Product expectedProduct = new Product(id, "new product_3", "new code_3");

        int sizeBefore = productDAO.all().size();
        productDAO.update(expectedProduct);
        int sizeAfter = productDAO.all().size();

        assertEquals(sizeBefore, sizeAfter);
        assertEquals(expectedProduct, productDAO.get(id));
    }

    @Test
    @DisplayName("Should insert new Product instance")
    void shouldSaveNewProduct() {

        int sizeBefore = productDAO.all().size();
        int nextId = sizeBefore + 1;
        Product newProduct = new Product(nextId, "new product_6", "new code_6");

        productDAO.save(newProduct);
        int sizeAfter = productDAO.all().size();

        assertEquals(sizeBefore + 1, sizeAfter);
        assertEquals(newProduct, productDAO.get(nextId));
    }

    @Test
    @DisplayName("Should delete Product instance by provided id")
    void shouldDeleteProduct() {

        int id = EXISTING_ID1;
        int sizeBefore = productDAO.all().size();
        productDAO.delete(id);
        int sizeAfter = productDAO.all().size();

        assertEquals(sizeBefore - 1, sizeAfter);
        assertTrue(productDAO.get(id).isEmpty());
    }

    @Test
    @DisplayName("Should delete all instances from Product table")
    void shouldDeleteAllProducts() {

        assertFalse(productDAO.all().isEmpty());
        productDAO.deleteAll();
        assertTrue(productDAO.all().isEmpty());
    }
}
