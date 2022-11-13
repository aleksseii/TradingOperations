package ru.aleksseii.common;

import com.google.inject.AbstractModule;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

public final class TradingOperationsModule extends AbstractModule {

    private final @NotNull Connection connection;

    public TradingOperationsModule(@NotNull Connection connection) {
        this.connection = connection;
    }

    @Override
    protected void configure() {

        bind(Connection.class).toInstance(connection);
    }
}
