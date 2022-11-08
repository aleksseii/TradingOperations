package ru.aleksseii;

import com.google.inject.AbstractModule;
import org.jetbrains.annotations.NotNull;
import ru.aleksseii.database.JDBCCredentials;

import java.sql.Connection;

public final class TradingOperationsModule extends AbstractModule {

    private static final @NotNull JDBCCredentials CREDENTIALS = JDBCCredentials.getDefault();

    private final @NotNull Connection connection;

    public TradingOperationsModule(@NotNull Connection connection) {
        this.connection = connection;
    }

    @Override
    protected void configure() {

        bind(Connection.class).toInstance(connection);
    }
}
