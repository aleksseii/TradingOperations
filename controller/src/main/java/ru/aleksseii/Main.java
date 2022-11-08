package ru.aleksseii;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.aleksseii.database.ConnectionManager;
import ru.aleksseii.database.FlywayInitializer;

import java.sql.Connection;

public final class Main {

    public static void main(String[] args) {

        try {

            FlywayInitializer.initDB();
            Connection connection = ConnectionManager.getConnectionOrThrow();
            final Injector injector = Guice.createInjector(new TradingOperationsModule(connection));


        } catch (RuntimeException e) {
            System.err.println("Invalid database url provided");
            System.exit(0);
        }
    }
}
