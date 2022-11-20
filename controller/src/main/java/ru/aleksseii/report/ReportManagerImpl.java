package ru.aleksseii.report;

import com.google.inject.Inject;
import com.zaxxer.hikari.HikariDataSource;
import generated.tables.records.ProductRecord;
import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.impl.DSL;
import ru.aleksseii.model.Organization;
import ru.aleksseii.model.Product;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.*;

import static generated.Tables.*;
import static org.jooq.impl.DSL.*;

public final class ReportManagerImpl implements ReportManager {

    private final @NotNull HikariDataSource dataSource;

    @Inject
    public ReportManagerImpl(@NotNull HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Выбрать первые 10 поставщиков по количеству поставленного товара
     * @return List of top 10 organization-senders
     */
    @Override
    public @NotNull List<@NotNull Organization> getTop10OrgSenders() {

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

           final generated.tables.Organization o = ORGANIZATION.as("o");
           final generated.tables.Waybill w = WAYBILL.as("w");
           final generated.tables.WaybillArticle wa = WAYBILL_ARTICLE.as("wa");

            Result<Record4<Integer, Long, String, String>> records = context
                    .select(o.ORG_ID, o.INN, o.NAME, o.BANK_ACCOUNT)
                    .from(o)
                    .join(w).on(w.ORG_SENDER_ID.equal(o.ORG_ID))
                    .join(wa).using(w.WAYBILL_ID)
                    .groupBy(o.ORG_ID)
                    .orderBy(sum(wa.AMOUNT).desc())
                    .limit(10)
                    .fetch();

            return records.stream()
                    .map(r -> new Organization(r.into(ORGANIZATION)))
                    .toList();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
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

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final generated.tables.Organization o = ORGANIZATION.as("o");
            final generated.tables.Waybill w = WAYBILL.as("w");
            final generated.tables.WaybillArticle wa = WAYBILL_ARTICLE.as("wa");

            Result<Record6<Integer, Long, String, String, Integer, BigDecimal>> records = context
                    .select(
                            o.ORG_ID, o.INN, o.NAME, o.BANK_ACCOUNT,
                            wa.PRODUCT_ID, sum(wa.AMOUNT).as("total_amount")
                    )
                    .from(o)
                    .join(w).on(w.ORG_SENDER_ID.equal(o.ORG_ID))
                    .join(wa).using(w.WAYBILL_ID)
                    .groupBy(o.ORG_ID, wa.PRODUCT_ID)
                    .orderBy(o.ORG_ID, wa.PRODUCT_ID)
                    .fetch();

            for (var record : records) {

                Organization organization = new Organization(record.into(ORGANIZATION));
                if (result.contains(organization)) {
                    continue;
                }

                Integer productId = record.getValue(PRODUCT.PRODUCT_ID);
                BigDecimal totalAmount = (BigDecimal) record.getValue("total_amount");

                if (productIdToAmount.containsKey(productId)) {

                    Integer requiredAmount = productIdToAmount.get(productId);
                    if (totalAmount.compareTo(BigDecimal.valueOf(requiredAmount)) > 0) {
                        result.add(organization);
                    }
                }
            }

        } catch (SQLException e) {
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
     * In {@code List<BigDecimal>} at [0] index placed amount per day,<br>
     * at [1] index placed proceeds per day
     * for the corresponding product and for the corresponding date
     */
    @Override
    public @NotNull Map<@NotNull Date, @NotNull Map<@NotNull Product, @NotNull List<@NotNull BigDecimal>>> getProductAmountAndSumForPeriod(
            @NotNull Date start,
            @NotNull Date end) {

        Map<Date, Map<Product, List<BigDecimal>>> result = new TreeMap<>();

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final generated.tables.Product p = PRODUCT.as("p");
            final generated.tables.Waybill w = WAYBILL.as("w");
            final generated.tables.WaybillArticle wa = WAYBILL_ARTICLE.as("wa");

            Result<Record6<LocalDate, Integer, String, String, BigDecimal, BigDecimal>> records = context
                    .select(
                            w.WAYBILL_DATE,
                            p.PRODUCT_ID, p.NAME, p.INTERNAL_CODE,
                            sum(wa.AMOUNT).as("amount_per_day"),
                            sum(wa.AMOUNT.mul(wa.PRICE)).as("proceeds_per_day")
                    )
                    .from(p)
                    .join(wa).using(p.PRODUCT_ID)
                    .join(w).using(w.WAYBILL_ID)
                    .where(w.WAYBILL_DATE.between(start.toLocalDate(), end.toLocalDate()))
                    .groupBy(w.WAYBILL_DATE, p.PRODUCT_ID)
                    .orderBy(w.WAYBILL_DATE, p.PRODUCT_ID)
                    .fetch();

            Map<Product, BigDecimal> totalAmount = new TreeMap<>(Comparator.comparingInt(Product::productId));
            Map<Product, BigDecimal> totalProceeds = new TreeMap<>(Comparator.comparingInt(Product::productId));

            for (var record : records) {

                Date date = Date.valueOf(record.getValue(WAYBILL.WAYBILL_DATE));
                Product product = new Product(record.into(PRODUCT));

                if (!(totalAmount.containsKey(product) || totalProceeds.containsKey(product))) {
                    totalAmount.put(product, BigDecimal.ZERO);
                    totalProceeds.put(product, BigDecimal.ZERO);
                }

                if (!result.containsKey(date)) {
                    result.put(date, new TreeMap<>(Comparator.comparingInt(Product::productId)));
                }

                Map<Product, List<BigDecimal>> productToList = result.get(date);
                if (!productToList.containsKey(product)) {
                    List<BigDecimal> productData = new ArrayList<>(2);
                    productData.add(BigDecimal.ZERO);
                    productData.add(BigDecimal.ZERO);
                    productToList.put(product, productData);
                }

                // List with 2 elements: [0] - amount per day, [1] - proceeds per day
                List<BigDecimal> productData = productToList.get(product);
                BigDecimal amount = (BigDecimal) record.getValue("amount_per_day");
                BigDecimal proceeds = (BigDecimal) record.getValue("proceeds_per_day");

                totalAmount.put(product, totalAmount.get(product).add(amount));
                totalProceeds.put(product, totalProceeds.get(product).add(proceeds));

                productData.set(0, productData.get(0).add(amount));
                productData.set(1, productData.get(1).add(proceeds));

                productToList.put(product, productData);

                result.put(date, productToList);
            }

            // outputting totals
            System.out.println("\r\nTotal Amount:");
            printMapContent(totalAmount);

            System.out.println("\r\nTotal Proceeds:");
            printMapContent(totalProceeds);
            System.out.println();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * Рассчитать среднюю цену по каждому товару за период
     * @param start start of the period
     * @param end end of the period
     * @return Map with Product as key and its average price as value
     */
    @Override
    public @NotNull Map<@NotNull Product, @NotNull Double> getProductAveragePriceForPeriod(
            @NotNull Date start,
            @NotNull Date end) {

        Map<Product, Double> result = new TreeMap<>(Comparator.comparingInt(Product::productId));

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = DSL.using(connection, SQLDialect.POSTGRES);

            final generated.tables.Product p = PRODUCT.as("p");
            final generated.tables.Waybill w = WAYBILL.as("w");
            final generated.tables.WaybillArticle wa = WAYBILL_ARTICLE.as("wa");

            Result<Record4<Integer, String, String, BigDecimal>> records = context
                    .select(
                            p.PRODUCT_ID, p.NAME, p.INTERNAL_CODE,
                            round(avg(wa.PRICE), 4).as("avg_price")
                    )
                    .from(p)
                    .join(wa).using(p.PRODUCT_ID)
                    .join(w).using(w.WAYBILL_ID)
                    .where(w.WAYBILL_DATE.between(start.toLocalDate(), end.toLocalDate()))
                    .groupBy(p.PRODUCT_ID)
                    .orderBy(p.PRODUCT_ID)
                    .fetch();

            for (var record : records) {

                Product product = new Product(record.into(PRODUCT));
                double avgPrice = Double.parseDouble(record.getValue("avg_price").toString());

                result.put(product, avgPrice);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Вывести список товаров, поставленных организациями за период.<br>
     * Если организация товары не поставляла, то она все равно должна быть отражена в списке.
     * @param start start of the period
     * @param end end of the period
     * @return Map of Organizations as key and List of Products sent by the corresponding organization as value
     */
    @Override
    public @NotNull Map<@NotNull Organization, @NotNull List<@NotNull Product>> getProductsSentByOrgForPeriod(
            @NotNull Date start,
            @NotNull Date end) {

        Map<Organization, List<Product>> result = new TreeMap<>(Comparator.comparingInt(Organization::orgId));

        try (Connection connection = dataSource.getConnection()) {

            final DSLContext context = using(connection, SQLDialect.POSTGRES);

            final generated.tables.Product p = PRODUCT.as("p");
            final generated.tables.Organization o = ORGANIZATION.as("o");
            final generated.tables.Waybill w = WAYBILL.as("w");
            final generated.tables.WaybillArticle wa = WAYBILL_ARTICLE.as("wa");

            Result<Record7<Integer, Long, String, String, Integer, String, String>> records = context
                    .select(
                            o.ORG_ID, o.INN, o.NAME.as("org_name"), o.BANK_ACCOUNT,
                            p.PRODUCT_ID, p.NAME.as("product_name"), p.INTERNAL_CODE
                    )
                    .from(o)
                    .leftJoin(w).on(w.ORG_SENDER_ID.equal(o.ORG_ID))
                    .leftJoin(wa).using(w.WAYBILL_ID)
                    .leftJoin(p).using(p.PRODUCT_ID)
                    .where(w.WAYBILL_DATE.between(start.toLocalDate(), end.toLocalDate()))
                    .or(w.WAYBILL_ID.isNull())
                    .groupBy(o.ORG_ID, p.PRODUCT_ID)
                    .orderBy(o.ORG_ID, p.PRODUCT_ID)
                    .fetch();

            for (var record : records) {

                Organization organization = new Organization(
                        record.getValue(ORGANIZATION.ORG_ID),
                        record.getValue(ORGANIZATION.INN),
                        (String) record.getValue("org_name"),
                        record.getValue(ORGANIZATION.BANK_ACCOUNT)
                );

                ProductRecord productRecord = new ProductRecord(
                        record.getValue(PRODUCT.PRODUCT_ID),
                        (String) record.getValue("product_name"),
                        record.getValue(PRODUCT.INTERNAL_CODE)
                );

                if (!result.containsKey(organization)) {
                    result.put(organization, new ArrayList<>());
                }

                if (productRecord.getName() != null && productRecord.getInternalCode() != null) {
                    List<Product> products = result.get(organization);
                    products.add(new Product(productRecord));
                    result.put(organization, products);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static <K, V> void printMapContent(@NotNull Map<K, V> map) {

        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("\t" + entry.getKey() + " " + "-".repeat(20) + " " + entry.getValue());
        }
    }
}
