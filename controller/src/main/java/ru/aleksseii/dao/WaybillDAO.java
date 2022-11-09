package ru.aleksseii.dao;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import ru.aleksseii.model.Waybill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({ "SqlNoDataSourceInspection", "SqlResolve" })
public final class WaybillDAO implements CrudDAO<Waybill> {

    private static final @NotNull String SQL_SELECT_BY_ID = "SELECT * FROM waybill WHERE waybill_id = ?";

    private static final @NotNull String SQL_SELECT_ALL = "SELECT * FROM waybill";

    private static final @NotNull String SQL_UPDATE =
            "UPDATE waybill SET waybill_date = ?, org_sender_id = ? WHERE waybill_id = ?";

    private static final @NotNull String SQL_INSERT =
            "INSERT INTO waybill(waybill_date, org_sender_id) VALUES (?, ?)";

    private static final @NotNull String SQL_DELETE_BY_ID = "DELETE FROM waybill WHERE waybill_id = ?";

    @SuppressWarnings("SqlWithoutWhere")
    private static final @NotNull String SQL_DELETE_ALL = "DELETE FROM waybill";



    private final @NotNull Connection connection;

    @Inject
    public WaybillDAO(@NotNull Connection connection) {
        this.connection = connection;
    }

    @Override
    public @NotNull Waybill get(int id) {

        try (PreparedStatement selectStatement = connection.prepareStatement(SQL_SELECT_BY_ID)) {

            selectStatement.setInt(1, id);

            try (ResultSet resultSet = selectStatement.executeQuery()) {

                if (resultSet.next()) {
                    return getWaybillFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Waybill();
    }

    @Override
    public @NotNull List<@NotNull Waybill> all() {
        List<Waybill> resultWaybills = new ArrayList<>();

        try (Statement selectAllStatement = connection.createStatement()) {

            try (ResultSet resultSet = selectAllStatement.executeQuery(SQL_SELECT_ALL)) {

                while (resultSet.next()) {
                    resultWaybills.add(getWaybillFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultWaybills;
    }

    @Override
    public int update(@NotNull Waybill entity) {

        try (PreparedStatement updateStatement = connection.prepareStatement(SQL_UPDATE)) {

            updateStatement.setDate(1, entity.waybillDate());
            updateStatement.setInt(2, entity.orgSenderId());
            updateStatement.setInt(3, entity.waybillId());

            return updateStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void save(@NotNull Waybill entity) {

        try (PreparedStatement insertStatement = connection.prepareStatement(SQL_INSERT))  {

            insertStatement.setDate(1, entity.waybillDate());
            insertStatement.setInt(2, entity.orgSenderId());

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

    private @NotNull Waybill getWaybillFromResultSet(@NotNull ResultSet resultSet)
            throws SQLException {
        return new Waybill(
                resultSet.getInt("waybill_id"),
                resultSet.getDate("waybill_date"),
                resultSet.getInt("org_sender_id")
        );
    }
}
