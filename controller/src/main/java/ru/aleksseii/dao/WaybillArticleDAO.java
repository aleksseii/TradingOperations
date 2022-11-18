package ru.aleksseii.dao;

import com.zaxxer.hikari.HikariDataSource;
import generated.tables.records.WaybillArticleRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import ru.aleksseii.model.WaybillArticle;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static generated.Tables.WAYBILL_ARTICLE;

@SuppressWarnings({"SqlResolve", "SqlNoDataSourceInspection"})
public final class WaybillArticleDAO implements CrudDAO<WaybillArticle> {

    private final @NotNull HikariDataSource dataSource;

    @Inject
    public WaybillArticleDAO(@NotNull HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public @NotNull WaybillArticle get(int id) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final WaybillArticleRecord record = context.fetchOne(
                    WAYBILL_ARTICLE,
                    WAYBILL_ARTICLE.WAYBILL_ARTICLE_ID.equal(id)
            );

            if (record != null) {
                return new WaybillArticle(record);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new WaybillArticle();
    }

    @Override
    public @NotNull List<@NotNull WaybillArticle> all() {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final Result<WaybillArticleRecord> waybillArticleRecords = context.fetch(WAYBILL_ARTICLE);
            if (!waybillArticleRecords.isEmpty()) {
                return waybillArticleRecords.stream().map(WaybillArticle::new).toList();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public int update(@NotNull WaybillArticle entity) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            int waybillArticleId = entity.waybillArticleId();
            final WaybillArticleRecord waybillArticleRecord = context.fetchOne(
                    WAYBILL_ARTICLE,
                    WAYBILL_ARTICLE.WAYBILL_ARTICLE_ID.equal(waybillArticleId)
            );

            if (waybillArticleRecord != null) {

                waybillArticleRecord
                        .setPrice(entity.price())
                        .setAmount(entity.amount())
                        .setWaybillId(entity.waybillId())
                        .setProductId(entity.productId())
                        .store();
                return 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void save(@NotNull WaybillArticle entity) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final WaybillArticleRecord waybillArticleRecord = context.newRecord(WAYBILL_ARTICLE);
            waybillArticleRecord
                    .setPrice(entity.price())
                    .setAmount(entity.amount())
                    .setWaybillId(entity.waybillId())
                    .setProductId(entity.productId())
                    .store();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final WaybillArticleRecord waybillArticleRecord = context.fetchOne(
                    WAYBILL_ARTICLE,
                    WAYBILL_ARTICLE.WAYBILL_ARTICLE_ID.equal(id)
            );

            if (waybillArticleRecord != null) {
                waybillArticleRecord.delete();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll() {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            context.deleteFrom(WAYBILL_ARTICLE).execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
