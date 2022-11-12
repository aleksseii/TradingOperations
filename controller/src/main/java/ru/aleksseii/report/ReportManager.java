package ru.aleksseii.report;

import org.jetbrains.annotations.NotNull;
import ru.aleksseii.model.Organization;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ReportManager {

    @NotNull List<@NotNull Organization> getTop10OrgSenders();

    @NotNull Set<@NotNull Organization> getOrgsWhichSentProductsGTAmount(
            @NotNull Map<@NotNull Integer, @NotNull Integer> productToAmount);
}
