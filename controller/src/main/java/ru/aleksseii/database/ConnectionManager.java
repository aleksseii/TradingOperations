package ru.aleksseii.database;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {

    private static final @NotNull JDBCCredentials CREDENTIALS = JDBCCredentials.getDefault();

    public static Connection getConnectionOrThrow() throws RuntimeException {

        try (Connection connection = DriverManager.getConnection(
                CREDENTIALS.getUrl(),
                CREDENTIALS.getUsername(),
                CREDENTIALS.getPassword()
        )) {

            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
