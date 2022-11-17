package ru.aleksseii.common;

import org.jetbrains.annotations.NotNull;
import ru.aleksseii.model.Organization;
import ru.aleksseii.model.Product;
import ru.aleksseii.model.Waybill;
import ru.aleksseii.model.WaybillArticle;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ObjectMapping {

    public static @NotNull Product getProductFromResultSet(@NotNull ResultSet resultSet)
            throws SQLException {

        return new Product(
                resultSet.getInt("product_id"),
                resultSet.getString("name"),
                resultSet.getString("internal_code")
        );
    }

    public static @NotNull Organization getOrganizationFromResultSet(@NotNull ResultSet resultSet)
            throws SQLException {

        return new Organization(
                resultSet.getInt("org_id"),
                resultSet.getLong("inn"),
                resultSet.getString("name"),
                resultSet.getString("bank_account")
        );
    }

    public static @NotNull Waybill getWaybillFromResultSet(@NotNull ResultSet resultSet)
            throws SQLException {
        return new Waybill(
                resultSet.getInt("waybill_id"),
                resultSet.getDate("waybill_date"),
                resultSet.getInt("org_sender_id")
        );
    }

    public static @NotNull WaybillArticle getWaybillArticleFromResultSet(@NotNull ResultSet resultSet)
            throws SQLException {
        return new WaybillArticle(
                resultSet.getInt("waybill_article_id"),
                resultSet.getLong("price"),
                resultSet.getInt("amount"),
                resultSet.getInt("waybill_id"),
                resultSet.getInt("product_id")
        );
    }
}
