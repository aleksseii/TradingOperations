package ru.aleksseii.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;

public final class DataSourceManager {

    private static final @NotNull JDBCCredentials CREDENTIALS = JDBCCredentials.getDefault();

    public static @NotNull HikariDataSource getHikariDataSource() {
        Properties props = new Properties();
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        props.setProperty("dataSource.databaseName", CREDENTIALS.getDbName());
        props.setProperty("dataSource.user", CREDENTIALS.getUsername());
        props.setProperty("dataSource.password", CREDENTIALS.getPassword());

        HikariConfig config = new HikariConfig(props);

        return new HikariDataSource(config);
    }
}
