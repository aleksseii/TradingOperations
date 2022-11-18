package ru.aleksseii.common;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import ru.aleksseii.dao.OrganizationDAO;
import ru.aleksseii.dao.ProductDAO;
import ru.aleksseii.dao.WaybillArticleDAO;
import ru.aleksseii.dao.WaybillDAO;
import ru.aleksseii.database.DataSourceManager;
import ru.aleksseii.database.FlywayInitializer;
import ru.aleksseii.model.Organization;
import ru.aleksseii.model.Product;
import ru.aleksseii.model.Waybill;
import ru.aleksseii.model.WaybillArticle;
import ru.aleksseii.report.ReportManager;
import ru.aleksseii.report.ReportManagerImpl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@SuppressWarnings("DuplicatedCode")
public final class Demonstrating {

    private static final @NotNull HikariDataSource DATA_SOURCE = DataSourceManager.getHikariDataSource();
    private static final @NotNull Injector INJECTOR = Guice.createInjector(new TradingOperationsModule(DATA_SOURCE));
    private static final @NotNull String DELIMITER = "=".repeat(100);

    private static final @NotNull Date START_DATE = Date.valueOf("2022-11-05");
    private static final @NotNull Date END_DATE = Date.valueOf("2022-11-08");

    public static void demonstrate() {

        productDAODemo();
        organizationDAODemo();
        waybillDAODemo();
        waybillArticleDAODemo();
        reportManagerDemo();

        FlywayInitializer.initDB();
        DATA_SOURCE.close();
    }

    private static void productDAODemo() {

        FlywayInitializer.initDB();

        final ProductDAO productDAO = INJECTOR.getInstance(ProductDAO.class);

        System.out.println("\n\nDemonstrating ProductDAO:\n" + DELIMITER + '\n' + DELIMITER);

        int id = 1;
        System.out.println("Getting by id = `" + id + "`:\n");
        printWithDelimiter(List.of(productDAO.get(id)));

        String name = "product_1";
        System.out.println("Getting by name = `" + name + "`:\n");
        printWithDelimiter(productDAO.get(name));

        System.out.println("Printing all:\n");
        printWithDelimiter(productDAO.all());

        productDAO.update(new Product(1, "new name_1", "new code_1"));
        System.out.println("After updating 1-st instance:\n");
        printWithDelimiter(productDAO.all());

        productDAO.delete(1);
        System.out.println("After deleting 1-st instance:\n");
        printWithDelimiter(productDAO.all());

        productDAO.deleteAll();
        System.out.println("After deleting all instances:\n");
        System.out.println(productDAO.all().isEmpty() ? "list is empty" : "list is not empty");
        printWithDelimiter(productDAO.all());

        productDAO.save(new Product("random name_1", "random code_1"));
        productDAO.save(new Product("random name_2", "random code_2"));
        System.out.println("After inserting couple of new instances:\n");
        printWithDelimiter(productDAO.all());
    }


    private static void organizationDAODemo() {

        FlywayInitializer.initDB();

        final OrganizationDAO orgDAO = INJECTOR.getInstance(OrganizationDAO.class);

        System.out.println("\n\nDemonstrating OrganizationDAO:\n" + DELIMITER + '\n' + DELIMITER);

        int id = 1;
        System.out.println("Getting by id = `" + id + "`:\n");
        printWithDelimiter(List.of(orgDAO.get(1)));

        String name = "org_1";
        System.out.println("Getting by name = `" + name + "`:\n");
        printWithDelimiter(orgDAO.get(name));

        long inn = 222;
        System.out.println("Getting by inn = `" + inn + "`\n");
        printWithDelimiter(List.of(orgDAO.getByInn(inn)));

        System.out.println("Printing all:\n");
        printWithDelimiter(orgDAO.all());

        orgDAO.update(new Organization(1, 999L, "new name_1", "new acc_1"));
        System.out.println("After updating 1-st instance:\n");
        printWithDelimiter(orgDAO.all());

        orgDAO.delete(1);
        System.out.println("After deleting 1-st instance:\n");
        printWithDelimiter(orgDAO.all());

        orgDAO.save(new Organization(777L, "random name_1", "random acc_1"));
        orgDAO.save(new Organization(888L, "random name_2", "random acc_2"));
        System.out.println("After inserting couple of new instances:\n");
        printWithDelimiter(orgDAO.all());
    }

    private static void waybillDAODemo() {

        FlywayInitializer.initDB();

        final WaybillDAO waybillDAO = INJECTOR.getInstance(WaybillDAO.class);

        System.out.println("\n\nDemonstrating WaybillDAO:\n" + DELIMITER + '\n' + DELIMITER);

        int id = 1;
        System.out.println("Getting by id = `" + id + "`:\n");
        printWithDelimiter(List.of(waybillDAO.get(id)));

        System.out.println("Printing all:\n");
        printWithDelimiter(waybillDAO.all());

        waybillDAO.update(new Waybill(1, Date.valueOf("2022-11-14"), 3));
        System.out.println("After updating 1-st instance:\n");
        printWithDelimiter(waybillDAO.all());

        waybillDAO.delete(1);
        System.out.println("After deleting 1-st instance:\n");
        printWithDelimiter(waybillDAO.all());

        waybillDAO.deleteAll();
        System.out.println("After deleting all instances:\n");
        System.out.println(waybillDAO.all().isEmpty() ? "list is empty" : "list is not empty");
        printWithDelimiter(waybillDAO.all());

        waybillDAO.save(new Waybill(Date.valueOf("2022-11-15"), 1));
        waybillDAO.save(new Waybill(Date.valueOf("2022-11-16"), 2));
        System.out.println("After inserting couple of new instances:\n");
        printWithDelimiter(waybillDAO.all());
    }

    private static void waybillArticleDAODemo() {

        FlywayInitializer.initDB();

        final WaybillArticleDAO waybillArticleDAO = INJECTOR.getInstance(WaybillArticleDAO.class);

        System.out.println("\n\nDemonstrating WaybillArticleDAO:\n" + DELIMITER + '\n' + DELIMITER);

        int id = 1;
        System.out.println("Getting by id = `" + id + "`:\n");
        printWithDelimiter(List.of(waybillArticleDAO.get(id)));

        System.out.println("Printing all:\n");
        printWithDelimiter(waybillArticleDAO.all());

        waybillArticleDAO.update(new WaybillArticle(1, 100_000L, 4, 2, 2));
        System.out.println("After updating 1-st instance:\n");
        printWithDelimiter(waybillArticleDAO.all());

        waybillArticleDAO.delete(1);
        System.out.println("After deleting 1-st instance:\n");
        printWithDelimiter(waybillArticleDAO.all());

        waybillArticleDAO.deleteAll();
        System.out.println("After deleting all instances:\n");
        System.out.println(waybillArticleDAO.all().isEmpty() ? "list is empty" : "list is not empty");
        printWithDelimiter(waybillArticleDAO.all());

        waybillArticleDAO.save(new WaybillArticle(5_000L, 7, 1, 3));
        waybillArticleDAO.save(new WaybillArticle(500L, 15, 3, 1));
        System.out.println("After inserting couple of new instances:\n");
        printWithDelimiter(waybillArticleDAO.all());
    }

    private static void reportManagerDemo() {

        FlywayInitializer.initDB();

        final ReportManager reportManager = INJECTOR.getInstance(ReportManagerImpl.class);

        System.out.println("\n\nDemonstrating ReportManagerImpl:\n" + DELIMITER + '\n' + DELIMITER);


        System.out.println("Getting Top-10 organization-senders (report 1):\n");
        printWithDelimiter(reportManager.getTop10OrgSenders());


        System.out.println("""
                Getting organizations which sent more than 3 product with product_id = 1
                OR more than 5 products with product_id = 5 (report 2):
                """);
        printWithDelimiter(reportManager.getOrgsWhichSentProductsGTAmount(Map.of(1, 3, 5, 5)));


        System.out.println("Getting amount and proceed for every product for each day\nin period from " +
                START_DATE + " to " + END_DATE + ".\nAnd getting totals for the period (report 3):\n");
        Map<Date, Map<Product, List<BigDecimal>>> result =
                reportManager.getProductAmountAndSumForPeriod(START_DATE, END_DATE);

        for (Map.Entry<Date, Map<Product, List<BigDecimal>>> outerEntry : result.entrySet()) {
            Date date = outerEntry.getKey();
            System.out.println("Date: " + date);

            for (Map.Entry<Product, List<BigDecimal>> innerEntry : outerEntry.getValue().entrySet()) {

                Product product = innerEntry.getKey();
                List<BigDecimal> productList = innerEntry.getValue();
                System.out.println("\t\t" + product +
                        ": \tAmount = " + productList.get(0) + ", \tProceeds = " + productList.get(1));
            }
        }
        System.out.println(DELIMITER);


        System.out.println("Getting average price for every product\nfor the period from " +
                START_DATE + " to " + END_DATE + " (report 4):\n");
        Map<Product, Double> productToAverage = reportManager.getProductAveragePriceForPeriod(START_DATE, END_DATE);
        printMapContent(productToAverage);
        System.out.println(DELIMITER);


        System.out.println("Getting lists of products sent by organizations for the period from " +
                START_DATE + " to " + END_DATE + " (report 5):\n");
        Map<Organization,List<Product>> orgToProducts = reportManager.getProductsSentByOrgForPeriod(START_DATE, END_DATE);
        printMapContent(orgToProducts);
    }

    private static <K, V> void printMapContent(@NotNull Map<K, V> map) {

        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("\t" + entry.getKey() + " " + "-".repeat(20) + " " + entry.getValue());
        }
    }

    private static <T> void printWithDelimiter(@NotNull Collection<T> collection) {
        collection.forEach(System.out::println);
        System.out.println(DELIMITER);
    }
}
