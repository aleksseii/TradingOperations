package ru.aleksseii;

import com.google.inject.AbstractModule;
import ru.aleksseii.database.JDBCCredentials;

public final class TradingOperationsModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(JDBCCredentials.class).toInstance(JDBCCredentials.getDefault());
    }
}
