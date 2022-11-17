package ru.aleksseii.dao;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import generated.tables.records.ProductRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import ru.aleksseii.model.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static generated.Tables.PRODUCT;

@SuppressWarnings({ "SqlResolve", "SqlNoDataSourceInspection" })
public final class ProductDAO implements CrudDAO<Product> {

    private final @NotNull HikariDataSource dataSource;

    @Inject
    public ProductDAO(@NotNull HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }


    /**
     * @param id id to get instance by
     * @return instance if found by id, empty instance otherwise
     */
    @Override
    public @NotNull Product get(int id) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final ProductRecord record = context.fetchOne(PRODUCT, PRODUCT.PRODUCT_ID.equal(id));
            if (record != null) {
                return new Product(record);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Product();
    }

    public @NotNull List<@NotNull Product> get(@NotNull String name) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final Result<ProductRecord> productRecords = context.fetch(PRODUCT, PRODUCT.NAME.equal(name));
            if (!productRecords.isEmpty()) {
                return productRecords.stream().map(Product::new).toList();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public @NotNull List<@NotNull Product> all() {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final Result<ProductRecord> productRecords = context.fetch(PRODUCT);
            if (!productRecords.isEmpty()) {
                return productRecords.stream().map(Product::new).toList();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public int update(@NotNull Product entity) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            int productId = entity.productId();
            final ProductRecord productRecord = context.fetchOne(PRODUCT, PRODUCT.PRODUCT_ID.equal(productId));

            if (productRecord != null) {

                productRecord
                        .setName(entity.name())
                        .setInternalCode(entity.internalCode())
                        .store();
                return 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void save(@NotNull Product entity) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final ProductRecord productRecord = context.newRecord(PRODUCT);
            productRecord
                    .setName(entity.name())
                    .setInternalCode(entity.internalCode())
                    .store();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final ProductRecord productRecord = context.fetchOne(PRODUCT, PRODUCT.PRODUCT_ID.equal(id));
            if (productRecord != null) {
                productRecord.delete();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll() {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            context.deleteFrom(PRODUCT).execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
