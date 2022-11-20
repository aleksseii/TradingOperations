package ru.aleksseii.common;

import com.google.inject.AbstractModule;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

public final class TradingOperationsModule extends AbstractModule {

    private final @NotNull HikariDataSource dataSource;

    public TradingOperationsModule(@NotNull HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void configure() {

        bind(HikariDataSource.class).toInstance(dataSource);
    }
}
