package ru.aleksseii.dao;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import generated.tables.records.WaybillRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import ru.aleksseii.model.Waybill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static generated.Tables.WAYBILL;

@SuppressWarnings({ "SqlNoDataSourceInspection", "SqlResolve" })
public final class WaybillDAO implements CrudDAO<Waybill> {

    private final @NotNull HikariDataSource dataSource;

    @Inject
    public WaybillDAO(@NotNull HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @param id id to get instance by
     * @return instance if found by id, empty instance otherwise
     */
    @Override
    public @NotNull Waybill get(int id) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            WaybillRecord record = context.fetchOne(WAYBILL, WAYBILL.WAYBILL_ID.equal(id));
            if (record != null) {
                return new Waybill(record);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Waybill();
    }

    @Override
    public @NotNull List<@NotNull Waybill> all() {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            Result<WaybillRecord> waybillRecords = context.fetch(WAYBILL);
            if (!waybillRecords.isEmpty()) {
                return waybillRecords.stream().map(Waybill::new).toList();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public int update(@NotNull Waybill entity) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            int waybillId = entity.waybillId();
            final WaybillRecord waybillRecord = context.fetchOne(WAYBILL, WAYBILL.WAYBILL_ID.equal(waybillId));

            if (waybillRecord != null) {

                waybillRecord
                        .setWaybillDate(entity.waybillDate().toLocalDate())
                        .setOrgSenderId(entity.orgSenderId())
                        .store();
                return 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void save(@NotNull Waybill entity) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final WaybillRecord waybillRecord = context.newRecord(WAYBILL);
            waybillRecord
                    .setWaybillDate(entity.waybillDate().toLocalDate())
                    .setOrgSenderId(entity.orgSenderId())
                    .store();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final WaybillRecord waybillRecord = context.fetchOne(WAYBILL, WAYBILL.WAYBILL_ID.equal(id));
            if (waybillRecord != null) {
                waybillRecord.delete();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll() {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            context.deleteFrom(WAYBILL).execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
