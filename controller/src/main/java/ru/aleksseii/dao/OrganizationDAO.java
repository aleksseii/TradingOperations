package ru.aleksseii.dao;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import ru.aleksseii.model.Organization;
import ru.aleksseii.report.ObjectMapping;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({ "SqlResolve", "SqlNoDataSourceInspection" })
public final class OrganizationDAO implements CrudDAO<Organization> {

    private static final @NotNull String SQL_SELECT_BY_ID = "SELECT * FROM organization WHERE org_id = ?";

    private static final @NotNull String SQL_SELECT_BY_NAME = "SELECT * FROM organization WHERE name = ?";

    private static final @NotNull String SQL_SELECT_BY_INN = "SELECT * FROM organization WHERE inn = ?";

    private static final @NotNull String SQL_SELECT_ALL = "SELECT * FROM organization";

    private static final @NotNull String SQL_UPDATE =
            "UPDATE organization SET inn = ?, name = ?, bank_account = ? WHERE org_id = ?";

    private static final @NotNull String SQL_INSERT =
            "INSERT INTO organization(inn, name, bank_account) VALUES (?, ?, ?)";

    private static final @NotNull String SQL_DELETE_BY_ID = "DELETE FROM organization WHERE org_id = ?";

    @SuppressWarnings("SqlWithoutWhere")
    private static final @NotNull String SQL_DELETE_ALL = "DELETE FROM organization";


    private final @NotNull Connection connection;

    @Inject
    public OrganizationDAO(@NotNull Connection connection) {
        this.connection = connection;
    }

    /**
     * @param id id to get instance by
     * @return instance if found by id, empty instance otherwise
     */
    @Override
    public @NotNull Organization get(int id) {

        try (PreparedStatement selectStatement = connection.prepareStatement(SQL_SELECT_BY_ID)) {

            selectStatement.setInt(1, id);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    return ObjectMapping.getOrganizationFromResultSet(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Organization();
    }

    public @NotNull List<@NotNull Organization> get(@NotNull String name) {

        List<Organization> resultOrganizations = new ArrayList<>();

        try (PreparedStatement selectByNameStatement = connection.prepareStatement(SQL_SELECT_BY_NAME)) {

            selectByNameStatement.setString(1, name);

            try (ResultSet resultSet = selectByNameStatement.executeQuery()) {
                while (resultSet.next()) {
                    resultOrganizations.add(ObjectMapping.getOrganizationFromResultSet(resultSet));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultOrganizations;
    }

    public @NotNull Organization getByInn(long inn) {

        try (PreparedStatement selectByInnStatement = connection.prepareStatement(SQL_SELECT_BY_INN)) {

            selectByInnStatement.setLong(1, inn);

            try (ResultSet resultSet = selectByInnStatement.executeQuery()) {
                if (resultSet.next()) {
                    return ObjectMapping.getOrganizationFromResultSet(resultSet);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Organization();
    }

    @Override
    public @NotNull List<@NotNull Organization> all() {

        List<Organization> resultOrganizations = new ArrayList<>();

        try (Statement selectAllStatement = connection.createStatement()) {

            try (ResultSet resultSet = selectAllStatement.executeQuery(SQL_SELECT_ALL)) {
                while (resultSet.next()) {
                    resultOrganizations.add(ObjectMapping.getOrganizationFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultOrganizations;
    }

    @Override
    public int update(@NotNull Organization entity) {

        try (PreparedStatement updateStatement = connection.prepareStatement(SQL_UPDATE)) {

            updateStatement.setLong(1, entity.inn());
            updateStatement.setString(2, entity.name());
            updateStatement.setString(3, entity.bankAccount());
            updateStatement.setInt(4, entity.orgId());

            return updateStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void save(@NotNull Organization entity) {

        try (PreparedStatement insertStatement = connection.prepareStatement(SQL_INSERT)) {

            insertStatement.setLong(1, entity.inn());
            insertStatement.setString(2, entity.name());
            insertStatement.setString(3, entity.bankAccount());

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
            deleteAllStatement.executeQuery(SQL_DELETE_ALL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
