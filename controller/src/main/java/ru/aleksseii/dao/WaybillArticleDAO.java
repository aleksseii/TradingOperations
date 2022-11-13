package ru.aleksseii.dao;

import org.jetbrains.annotations.NotNull;
import ru.aleksseii.model.WaybillArticle;
import ru.aleksseii.report.ObjectMapping;

import javax.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"SqlResolve", "SqlNoDataSourceInspection"})
public final class WaybillArticleDAO implements CrudDAO<WaybillArticle> {

    private static final @NotNull String SQL_SELECT_BY_ID =
            "SELECT * FROM waybill_article WHERE waybill_article_id = ?";

    private static final @NotNull String SQL_SELECT_ALL = "SELECT * FROM waybill_article";

    private static final @NotNull String SQL_UPDATE =
                    "UPDATE waybill_article SET price = ?, amount = ?, waybill_id = ?, product_id = ? " +
                     "WHERE waybill_article_id = ?";

    private static final @NotNull String SQL_INSERT =
            "INSERT INTO waybill_article(price, amount, waybill_id, product_id) VALUES (?, ?, ?, ?)";

    private static final @NotNull String SQL_DELETE_BY_ID =
            "DELETE FROM waybill_article WHERE waybill_article_id = ?";

    @SuppressWarnings("SqlWithoutWhere")
    private static final @NotNull String SQL_DELETE_ALL = "DELETE FROM waybill_article";


    private final @NotNull Connection connection;

    @Inject
    public WaybillArticleDAO(@NotNull Connection connection) {
        this.connection = connection;
    }

    @Override
    public @NotNull WaybillArticle get(int id) {

        try (PreparedStatement selectStatement = connection.prepareStatement(SQL_SELECT_BY_ID)) {

            selectStatement.setInt(1, id);

            try (ResultSet resultSet = selectStatement.executeQuery()) {

                if (resultSet.next()) {
                    return ObjectMapping.getWaybillArticleFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new WaybillArticle();
    }

    @Override
    public @NotNull List<@NotNull WaybillArticle> all() {
        List<WaybillArticle> resultWaybillArticles = new ArrayList<>();

        try (Statement selectAllStatement = connection.createStatement()) {

            try (ResultSet resultSet = selectAllStatement.executeQuery(SQL_SELECT_ALL)) {

                while (resultSet.next()) {
                    resultWaybillArticles.add(ObjectMapping.getWaybillArticleFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultWaybillArticles;
    }

    @Override
    public int update(@NotNull WaybillArticle entity) {

        try (PreparedStatement updateStatement = connection.prepareStatement(SQL_UPDATE)) {

            updateStatement.setLong(1, entity.price());
            updateStatement.setInt(2, entity.amount());
            updateStatement.setInt(3, entity.waybillId());
            updateStatement.setInt(4, entity.productId());
            updateStatement.setInt(5, entity.waybillArticleId());

            return updateStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void save(@NotNull WaybillArticle entity) {

        try (PreparedStatement insertStatement = connection.prepareStatement(SQL_INSERT))  {

            insertStatement.setLong(1, entity.price());
            insertStatement.setInt(2, entity.amount());
            insertStatement.setInt(3, entity.waybillId());
            insertStatement.setInt(4, entity.productId());

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
