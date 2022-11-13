package ru.aleksseii;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.aleksseii.common.TradingOperationsModule;
import ru.aleksseii.database.ConnectionManager;
import ru.aleksseii.database.FlywayInitializer;

import java.sql.Connection;
import java.sql.SQLException;

public final class Main {

    public static void main(String[] args) {

        try {

            FlywayInitializer.initDB();
            Connection connection = ConnectionManager.getConnectionOrThrow();
            final Injector injector = Guice.createInjector(new TradingOperationsModule(connection));


            connection.close();

        } catch (RuntimeException | SQLException e) {
            System.err.println("Invalid database url provided");
            System.exit(0);
        }
    }
}
