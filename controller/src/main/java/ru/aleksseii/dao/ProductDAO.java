package ru.aleksseii.dao;

import org.jetbrains.annotations.NotNull;
import ru.aleksseii.model.Product;

import java.util.List;

public class ProductDAO implements CrudDAO<Product> {

    public static final Product STUB_PRODUCT = new Product(-1, "", "");


    private static final @NotNull String SQL_SELECT_BY_ID = "SELECT * FROM product WHERE product_id = ?";

    private static final @NotNull String SQL_SELECT_BY_NAME = "SELECT * FROM product WHERE name = ?";

    private static final @NotNull String SQL_SELECT_ALL = "SELECT * FROM product";

    private static final @NotNull String SQL_UPDATE =
            "UPDATE product SET name = ?, internal_cod = ? WHERE product_id = ?";

    private static final @NotNull String SQL_INSERT =
            "INSERT INTO product(name, internal_code) VALUES (?, ?)";

    private static final @NotNull String SQL_DELETE_BY_ID =
            "DELETE FROM product WHERE id = ?";

    private static final @NotNull String SQL_DELETE_ALL =
            "DELETE FROM product";

    @Override
    public @NotNull Product get(int id) {
        return STUB_PRODUCT; // stub
    }

    @Override
    public @NotNull List<@NotNull Product> all() {
        return List.of(STUB_PRODUCT); // stub
    }

    @Override
    public @NotNull Product update(@NotNull Product entity) {
        return STUB_PRODUCT; // stub
    }

    @Override
    public void save(@NotNull Product entity) {
    }

    @Override
    public void delete(@NotNull Product entity) {
    }

    public @NotNull List<@NotNull Product> get(String name) {
        return List.of(STUB_PRODUCT); // stub
    }

    public void deleteAll() {
    }
}
