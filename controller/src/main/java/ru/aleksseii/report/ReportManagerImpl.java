package ru.aleksseii.report;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import ru.aleksseii.dao.OrganizationDAO;
import ru.aleksseii.dao.ProductDAO;
import ru.aleksseii.model.Organization;
import ru.aleksseii.model.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.*;
import java.util.*;

public final class ReportManagerImpl implements ReportManager {

    public static final String BASE_PATH = "controller/src/main/resources/db/select_queries/";
    private static final @NotNull String REPORT_1_SQL_FILE_NAME = "report1_top_10_org_senders.sql";
    private static final @NotNull String REPORT_2_SQL_FILE_NAME = "report2_orgs_and_amount_of_each_sent_product.sql";
    public static final String REPORT_3_SQL_FILE_NAME = "report3_product_amount_proceeds_per_day.sql";
    public static final String REPORT_4_SQL_FILE_NAME = "report4_product_avg_prices.sql";

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

                Organization organization = OrganizationDAO.getOrganizationFromResultSet(resultSet);
                if (result.contains(organization)) {
                    continue;
                }

                int productId = resultSet.getInt("product_id");
                int totalAmount = resultSet.getInt("total_amount");

                if (productIdToAmount.containsKey(productId)) {
                    if (totalAmount > productIdToAmount.get(productId)) {
                        result.add(organization);
                    }
                }
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * За каждый день для каждого товара рассчитать количество и сумму<br>
     * полученного товара в указанном периоде, посчитать итоги за период
     * @param start start of the period
     * @param end end of the period
     * @return Map with Date of the waybill as a key and Map of products with its data as a value.<br>
     * In List<Long> at [0] index placed amount per day,<br>
     * at [1] index placed proceeds per day
     * for the corresponding product and for the corresponding date
     */
    @Override
    public @NotNull Map<@NotNull Date, @NotNull Map<@NotNull Product, @NotNull List<@NotNull Long>>> getProductAmountAndSumForPeriod(
            @NotNull Date start,
            @NotNull Date end) {

        Map<Date, Map<Product, List<Long>>> result = new TreeMap<>();

        try (PreparedStatement selectStatement = connection.prepareStatement(
                readSQLFromFile(BASE_PATH + REPORT_3_SQL_FILE_NAME)
        )) {

          selectStatement.setDate(1, start);
          selectStatement.setDate(2, end);

            try (ResultSet resultSet = selectStatement.executeQuery()) {

                Map<Product, Integer> totalAmount = new TreeMap<>(Comparator.comparingInt(Product::productId));
                Map<Product, Long> totalProceeds = new TreeMap<>(Comparator.comparingInt(Product::productId));

                while (resultSet.next()) {

                    Date date = resultSet.getDate("waybill_date");
                    Product product = ProductDAO.getProductFromResultSet(resultSet);

                    if (!(totalAmount.containsKey(product) || totalProceeds.containsKey(product))) {
                        totalAmount.put(product, 0);
                        totalProceeds.put(product, 0L);
                    }

                    if (!result.containsKey(date)) {
                        result.put(date, new TreeMap<>(Comparator.comparingInt(Product::productId)));
                    }

                    Map<Product, List<Long>> productToList = result.get(date);
                    if (!productToList.containsKey(product)) {
                        List<Long> productData = new ArrayList<>(2);
                        productData.add(0L);
                        productData.add(0L);
                        productToList.put(product, productData);
                    }

                    // List with 2 elements: [0] - amount per day, [1] - proceeds per day
                    List<Long> productData = productToList.get(product);
                    int amount = resultSet.getInt("amount_per_day");
                    long proceeds = resultSet.getLong("proceeds_per_day");

                    totalAmount.put(product, totalAmount.get(product) + amount);
                    totalProceeds.put(product, totalProceeds.get(product) + proceeds);

                    productData.set(0, productData.get(0) + amount);
                    productData.set(1, productData.get(1) + proceeds);

                    productToList.put(product, productData);

                    result.put(date, productToList);
                }

                // outputting totals
                System.out.println("\nTotal Amount:");
                printMapContent(totalAmount);

                System.out.println("\nTotal Proceeds:");
                printMapContent(totalProceeds);
                System.out.println();
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    @Override
    public @NotNull Map<@NotNull Product, @NotNull Double> getProductAveragePriceForPeriod(
            @NotNull Date start,
            @NotNull Date end) {

        Map<Product, Double> result = new TreeMap<>(Comparator.comparingInt(Product::productId));

        try (PreparedStatement selectStatement = connection.prepareStatement(
                readSQLFromFile(BASE_PATH + REPORT_4_SQL_FILE_NAME)
        )) {

            selectStatement.setDate(1, start);
            selectStatement.setDate(2, end);

            try (ResultSet resultSet = selectStatement.executeQuery()) {

                while (resultSet.next()) {

                    Product product = ProductDAO.getProductFromResultSet(resultSet);
                    double avgPrice = resultSet.getDouble("avg_price");

                    result.put(product, avgPrice);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static <K, V> void printMapContent(Map<K, V> map) {

        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("\t" + entry.getKey() + " " + "-".repeat(20) + " " + entry.getValue());
        }
    }

    private static @NotNull String readSQLFromFile(@NotNull String filePath) throws IOException {

        if (!filePath.endsWith(".sql")) {
            throw new IllegalArgumentException("Input should be .sql file");
        }

        List<String> lines = Files.readAllLines(Paths.get(filePath));
        return String.join("\n", lines);
    }
}
