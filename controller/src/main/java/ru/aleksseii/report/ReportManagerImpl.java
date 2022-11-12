package ru.aleksseii.report;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import ru.aleksseii.dao.OrganizationDAO;
import ru.aleksseii.model.Organization;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public final class ReportManagerImpl implements ReportManager {

    public static final String BASE_PATH = "controller/src/main/resources/db/select_queries/";
    private static final @NotNull String REPORT_1_SQL_FILE_NAME = "report1_top_10_org_senders.sql";

    private static final @NotNull String REPORT_2_SQL_FILE_NAME = "report2_orgs_and_amount_of_each_sent_product.sql";


    private final @NotNull Connection connection;

    @Inject
    public ReportManagerImpl(@NotNull Connection connection) {
        this.connection = connection;
    }

    /**
     * Выбрать первые 10 поставщиков по количеству поставленного товара
     * @return List of top 10 organization-senders
     */
    @Override
    public @NotNull List<@NotNull Organization> getTop10OrgSenders() {

        List<@NotNull Organization> result = new ArrayList<>();

        try (Statement selectStatement = connection.createStatement()) {

            String sqlSelectQuery = readSQLFromFile(BASE_PATH + REPORT_1_SQL_FILE_NAME);
            ResultSet resultSet = selectStatement.executeQuery(sqlSelectQuery);

            while (resultSet.next()) {
                result.add(OrganizationDAO.getOrganizationFromResultSet(resultSet));
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Выбрать поставщиков с количеством поставленного товара выше указанного значения
     * (товар и его количество должны допускать множественное указание). <br>
     * Should get List of Organizations, each of which sent corresponding product
     * Greater Than (GT) corresponding amount
     * @param productIdToAmount map of product ids to a number.
     *                        Required amount of products should be greater than that number
     * @return List of organizations satisfying the condition
     */
    @Override
    public @NotNull Set<@NotNull Organization> getOrgsWhichSentProductsGTAmount(
            @NotNull Map<@NotNull Integer, @NotNull Integer> productIdToAmount) {

        Set<@NotNull Organization> result = new HashSet<>();

        try (Statement selectStatement = connection.createStatement()) {

            String sqlSelectQuery = readSQLFromFile(BASE_PATH + REPORT_2_SQL_FILE_NAME);
            ResultSet resultSet = selectStatement.executeQuery(sqlSelectQuery);

            while (resultSet.next()) {

                int productId = resultSet.getInt("product_id");
                int totalAmount = resultSet.getInt("total_amount");

                if (productIdToAmount.containsKey(productId)) {
                    if (totalAmount > productIdToAmount.get(productId)) {

                        Organization organization = OrganizationDAO.getOrganizationFromResultSet(resultSet);
                        result.add(organization);
                    }
                }
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static @NotNull String readSQLFromFile(@NotNull String filePath) throws IOException {

        if (!filePath.endsWith(".sql")) {
            throw new IllegalArgumentException("Input should be .sql file");
        }

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        return String.join("\n", lines);
    }
}
