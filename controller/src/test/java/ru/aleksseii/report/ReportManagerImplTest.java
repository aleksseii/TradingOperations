package ru.aleksseii.report;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.aleksseii.common.TradingOperationsModule;
import ru.aleksseii.database.ConnectionManager;
import ru.aleksseii.database.FlywayInitializer;
import ru.aleksseii.model.Organization;
import ru.aleksseii.model.Product;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ReportManagerImplTest {

    private static final @NotNull Organization ORGANIZATION_1 = new Organization(1, 111L, "org_1", "acc_1");
    private static final @NotNull Organization ORGANIZATION_2 = new Organization(2, 222L, "org_1", "acc_2");
    private static final @NotNull Organization ORGANIZATION_3 = new Organization(3, 333L, "org_3", "acc_3");

    private static final @NotNull Product PRODUCT_1 = new Product(1, "product_1", "code_1");
    private static final @NotNull Product PRODUCT_3 = new Product(3, "product_3", "code_3");
    private static final @NotNull Product PRODUCT_4 = new Product(4, "product_4", "code_4");
    private static final @NotNull Product PRODUCT_5 = new Product(5, "product_5", "code_5");

    private static final @NotNull Date START_DATE = Date.valueOf("2022-11-05");
    private static final @NotNull Date END_DATE = Date.valueOf("2022-11-08");

    private static final @NotNull List<@NotNull Organization> TOP_10_ORG_SENDERS = List.of(
            ORGANIZATION_3,
            ORGANIZATION_1,
            ORGANIZATION_2
    );

    private static final @NotNull Set<@NotNull Organization> ORGS_WHICH_SENT_PRODUCTS_GT_AMOUNT = Set.of(
            ORGANIZATION_2,
            ORGANIZATION_3
    );

    private static final @NotNull Map<@NotNull Product, @NotNull Double> PRODUCT_TO_AVERAGE_PRICE_FOR_PERIOD = Map.of(
            PRODUCT_1, 900d,
            PRODUCT_3, 5_000d,
            PRODUCT_4, 475d,
            PRODUCT_5, 300d
    );

    private static final @NotNull Map<@NotNull Organization, @NotNull List<@NotNull Product>> ORG_TO_SENT_PRODUCTS_FOR_PERIOD = Map.of(
            ORGANIZATION_1, List.of(PRODUCT_5),
            ORGANIZATION_2, List.of(PRODUCT_1, PRODUCT_3, PRODUCT_4),
            ORGANIZATION_3, List.of(PRODUCT_4, PRODUCT_5)
    );

    private static final @NotNull Map<@NotNull Date, @NotNull Map<@NotNull Product, @NotNull List<@NotNull Long>>> PRODUCT_AMOUNT_AND_SUM = Map.of(

            START_DATE, Map.of(
                    PRODUCT_3, List.of(2L, 10_000L)
            ),
            Date.valueOf("2022-11-06"), Map.of(
                    PRODUCT_4, List.of(3L, 1_500L),
                    PRODUCT_5, List.of(10L, 3_000L)),
            Date.valueOf("2022-11-07"), Map.of(
                    PRODUCT_5, List.of(3L, 900L)
            ),
            END_DATE, Map.of(
                    PRODUCT_1, List.of(4L, 3_600L),
                    PRODUCT_4, List.of(5L, 2_250L)
            )
    );

    private static final @NotNull Connection CONNECTION = ConnectionManager.getConnectionOrThrow();

    private static final @NotNull Injector INJECTOR = Guice.createInjector(new TradingOperationsModule(CONNECTION));

    private final @NotNull ReportManager reportManager = INJECTOR.getInstance(ReportManagerImpl.class);

    @BeforeAll
    static void initDB() {
        FlywayInitializer.initDB();
    }

    @AfterAll
    static void afterAll() {
        initDB();
        try {
            CONNECTION.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should get List of Top-10 organization-senders")
    void shouldGetTop10OrgSenders() {

        List<@NotNull Organization> orgSenders = reportManager.getTop10OrgSenders();

        assertEquals(TOP_10_ORG_SENDERS, orgSenders);
    }

    @Test
    @DisplayName("Should get organization-senders which sent products greater than provided amount")
    void shouldGetOrgsWhichSentProductsGTAmount() {

        Set<@NotNull Organization> organizations = reportManager.getOrgsWhichSentProductsGTAmount(Map.of(
                        1, 3,
                        5, 5
        ));

        assertEquals(ORGS_WHICH_SENT_PRODUCTS_GT_AMOUNT, organizations);
    }

    @Test
    @DisplayName("Should get product average price for period")
    void shouldGetProductAveragePriceForPeriod() {

        Map<@NotNull Product, @NotNull Double> productToAveragePrice =
                reportManager.getProductAveragePriceForPeriod(START_DATE, END_DATE);

        assertEquals(PRODUCT_TO_AVERAGE_PRICE_FOR_PERIOD, productToAveragePrice);
    }

    @Test
    @DisplayName("Should get products sent by organization for provided period")
    void shouldGetProductsSentByOrgForPeriod() {

        Map<@NotNull Organization, @NotNull List<@NotNull Product>> orgToProducts =
                reportManager.getProductsSentByOrgForPeriod(START_DATE, END_DATE);

        assertEquals(ORG_TO_SENT_PRODUCTS_FOR_PERIOD, orgToProducts);
    }

    @Test
    @DisplayName("Should get amount and sum of products for provided period and print total amount and total sum")
    void shouldGetProductAmountAndSumForPeriod() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        System.setOut(new PrintStream(outputStream));
        Map<Date, Map<Product, List<Long>>> result = reportManager.getProductAmountAndSumForPeriod(
                START_DATE,
                END_DATE
        );
        System.setOut(originalOut);

        assertEquals(PRODUCT_AMOUNT_AND_SUM, result);
        assertEquals(getExpectedOutput(), outputStream.toString());
    }

    private static @NotNull String getExpectedOutput() {

        Map<Product, Integer> totalAmount = new TreeMap<>(Comparator.comparingInt(Product::productId));
        totalAmount.put(PRODUCT_1, 4);
        totalAmount.put(PRODUCT_3, 2);
        totalAmount.put(PRODUCT_4, 8);
        totalAmount.put(PRODUCT_5, 13);

        Map<Product, Long> totalProceed = new TreeMap<>(Comparator.comparingInt(Product::productId));
        totalProceed.put(PRODUCT_1, 3_600L);
        totalProceed.put(PRODUCT_3, 10_000L);
        totalProceed.put(PRODUCT_4, 3_750L);
        totalProceed.put(PRODUCT_5, 3_900L);

        return "\r\nTotal Amount:\r\n" +
                getMapContentAsString(totalAmount) +
                "\r\nTotal Proceeds:\r\n" +
                getMapContentAsString(totalProceed) +
                "\r\n";
    }

    private static <K, V> @NotNull String getMapContentAsString(@NotNull Map<K, V> map) {

        final @NotNull StringBuilder sb = new StringBuilder();

        for (Map.Entry<K, V> entry : map.entrySet()) {
            sb.append("\t")
                    .append(entry.getKey())
                    .append(" ")
                    .append("-".repeat(20))
                    .append(" ")
                    .append(entry.getValue())
                    .append("\r\n");
        }
        return sb.toString();
    }
}
