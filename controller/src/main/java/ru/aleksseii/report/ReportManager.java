package ru.aleksseii.report;

import org.jetbrains.annotations.NotNull;
import ru.aleksseii.model.Organization;
import ru.aleksseii.model.Product;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ReportManager {

    @NotNull List<@NotNull Organization> getTop10OrgSenders();

    @NotNull Set<@NotNull Organization> getOrgsWhichSentProductsGTAmount(
            @NotNull Map<@NotNull Integer, @NotNull Integer> productToAmount);

    @NotNull Map<@NotNull Date, @NotNull Map<@NotNull Product, @NotNull List<@NotNull Long>>> getProductAmountAndSumForPeriod(
            @NotNull Date start,
            @NotNull Date end);

    @NotNull Map<@NotNull Product, @NotNull Double> getProductAveragePriceForPeriod(
            @NotNull Date start,
            @NotNull Date end);
}
