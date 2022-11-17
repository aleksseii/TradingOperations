package ru.aleksseii.model;

import generated.tables.records.OrganizationRecord;
import org.jetbrains.annotations.NotNull;


public record Organization(int orgId,
                           long inn,
                           @NotNull String name,
                           @NotNull String bankAccount) {

    public Organization(long inn, @NotNull String name, @NotNull String bankAccount) {
        this(0, inn, name, bankAccount);
    }

    public Organization() {
        this(0, 0, "", "");
    }

    public Organization(@NotNull OrganizationRecord record) {
        this(
                record.getOrgId(),
                record.getInn(),
                record.getName(),
                record.getBankAccount()
        );
    }

    public boolean isEmpty() {
        return this.equals(new Organization());
    }
}
