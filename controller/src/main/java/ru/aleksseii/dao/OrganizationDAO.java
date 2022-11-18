package ru.aleksseii.dao;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import generated.tables.records.OrganizationRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import ru.aleksseii.model.Organization;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static generated.Tables.ORGANIZATION;

public final class OrganizationDAO implements CrudDAO<Organization> {

    private final @NotNull HikariDataSource dataSource;

    @Inject
    public OrganizationDAO(@NotNull HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }


    /**
     * @param id id to get instance by
     * @return instance if found by id, empty instance otherwise
     */
    @Override
    public @NotNull Organization get(int id) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final OrganizationRecord record = context.fetchOne(ORGANIZATION, ORGANIZATION.ORG_ID.equal(id));
            if (record != null) {
                return new Organization(record);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Organization();
    }

    public @NotNull List<@NotNull Organization> get(@NotNull String name) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            Result<OrganizationRecord> orgRecords = context.fetch(ORGANIZATION, ORGANIZATION.NAME.equal(name));
            if (!orgRecords.isEmpty()) {
                return orgRecords.stream().map(Organization::new).toList();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     * @param inn inn to get instance by
     * @return instance if found by inn, empty instance otherwise
     */
    public @NotNull Organization getByInn(long inn) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final OrganizationRecord record = context.fetchOne(ORGANIZATION, ORGANIZATION.INN.equal(inn));
            if (record != null) {
                return new Organization(record);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Organization();
    }

    @Override
    public @NotNull List<@NotNull Organization> all() {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final Result<OrganizationRecord> orgRecords = context.fetch(ORGANIZATION);
            if (!orgRecords.isEmpty()) {
                return orgRecords.stream().map(Organization::new).toList();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public int update(@NotNull Organization entity) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            int orgId = entity.orgId();
            OrganizationRecord orgRecord = context.fetchOne(ORGANIZATION, ORGANIZATION.ORG_ID.equal(orgId));

            if (orgRecord != null) {

                orgRecord
                        .setInn(entity.inn())
                        .setName(entity.name())
                        .setBankAccount(entity.bankAccount())
                        .store();
                return 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void save(@NotNull Organization entity) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            OrganizationRecord orgRecord = context.newRecord(ORGANIZATION);
            orgRecord
                    .setInn(entity.inn())
                    .setName(entity.name())
                    .setBankAccount(entity.bankAccount())
                    .store();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final OrganizationRecord orgRecord = context.fetchOne(ORGANIZATION, ORGANIZATION.ORG_ID.equal(id));
            if (orgRecord != null) {
                orgRecord.delete();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll() {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            context.deleteFrom(ORGANIZATION).execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
