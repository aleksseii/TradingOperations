package ru.aleksseii.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public final class JDBCCredentials {

    private static final @NotNull JDBCCredentials DEFAULT = new JDBCCredentials(
            "127.0.0.1",
            "5432",
            "trading_operations_db",
            "postgres",
            "postgres"
    );

    private static final @NotNull String PREFIX = "jdbc:postgresql";

    private final @NotNull String host;

    private final @NotNull String port;

    @Getter
    private final @NotNull String dbName;

    @Getter
    private final @NotNull String username;

    @Getter
    private final @NotNull String password;

    public @NotNull String getUrl() {
        return PREFIX + "://" + host + ':' + port + '/' + dbName;
    }

    public static @NotNull JDBCCredentials getDefault() {
        return DEFAULT;
    }
}
