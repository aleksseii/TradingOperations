package ru.aleksseii.dao;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import ru.aleksseii.model.Product;
import ru.aleksseii.report.ObjectMapping;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({ "SqlResolve", "SqlNoDataSourceInspection" })
public final class ProductDAO implements CrudDAO<Product> {

    private static final @NotNull String SQL_SELECT_BY_ID = "SELECT * FROM product WHERE product_id = ?";

    private static final @NotNull String SQL_SELECT_BY_NAME = "SELECT * FROM product WHERE name = ?";

    private static final @NotNull String SQL_SELECT_ALL = "SELECT * FROM product";

    private static final @NotNull String SQL_UPDATE =
            "UPDATE product SET name = ?, internal_code = ? WHERE product_id = ?";

    private static final @NotNull String SQL_INSERT =
            "INSERT INTO product(name, internal_code) VALUES (?, ?)";

    private static final @NotNull String SQL_DELETE_BY_ID = "DELETE FROM product WHERE product_id = ?";

    @SuppressWarnings("SqlWithoutWhere")
    private static final @NotNull String SQL_DELETE_ALL = "DELETE FROM product";


    private final @NotNull Connection connection;

    @Inject
    public ProductDAO(@NotNull Connection connection) {
        this.connection = connection;
    }

    /**
     * @param id id to get instance by
     * @return instance if found by id, empty instance otherwise
     */
    @Override
    public @NotNull Product get(int id) {

        try (PreparedStatement selectStatement = connection.prepareStatement(SQL_SELECT_BY_ID)) {

            selectStatement.setInt(1, id);

            try (ResultSet resultSet = selectStatement.executeQuery()) {

                if (resultSet.next()) {
                    return ObjectMapping.getProductFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Product();
    }

    public @NotNull List<@NotNull Product> get(@NotNull String name) {

        List<Product> resultProducts = new ArrayList<>();

        try (PreparedStatement selectByNameStatement = connection.prepareStatement(SQL_SELECT_BY_NAME)) {

            selectByNameStatement.setString(1, name);

            try (ResultSet resultSet = selectByNameStatement.executeQuery()) {
                while (resultSet.next()) {
                    resultProducts.add(ObjectMapping.getProductFromResultSet(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultProducts;
    }

    @Override
    public @NotNull List<@NotNull Product> all() {

        List<Product> resultProducts = new ArrayList<>();

        try (Statement selectAllStatement = connection.createStatement()) {

            try (ResultSet resultSet = selectAllStatement.executeQuery(SQL_SELECT_ALL)) {

                while (resultSet.next()) {
                    resultProducts.add(ObjectMapping.getProductFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultProducts;
    }

    @Override
    public int update(@NotNull Product entity) {

        try (PreparedStatement updateStatement = connection.prepareStatement(SQL_UPDATE)) {

            updateStatement.setString(1, entity.name());
            updateStatement.setString(2, entity.internalCode());
            updateStatement.setInt(3, entity.productId());

            return updateStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void save(@NotNull Product entity) {

        try (PreparedStatement insertStatement = connection.prepareStatement(SQL_INSERT))  {

            insertStatement.setString(1, entity.name());
            insertStatement.setString(2, entity.internalCode());

            insertStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {

        try (PreparedStatement deleteStatement = connection.prepareStatement(SQL_DELETE_BY_ID)) {

            deleteStatement.setInt(1, id);
            deleteStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll() {

        try (Statement deleteAllStatement = connection.createStatement()) {
            deleteAllStatement.executeUpdate(SQL_DELETE_ALL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
