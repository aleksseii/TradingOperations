package ru.aleksseii.database;

import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;

public final class FlywayInitializer {

    private static final @NotNull JDBCCredentials CREDENTIALS = JDBCCredentials.getDefault();

    public static void initDB() {

        final Flyway flyway = Flyway.configure()
                .dataSource(CREDENTIALS.getUrl(), CREDENTIALS.getUsername(), CREDENTIALS.getPassword())
                .locations("db/migrations")
                .cleanDisabled(false)
                .load();
        flyway.clean();
        flyway.migrate();
    }
}
