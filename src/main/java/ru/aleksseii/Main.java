package ru.aleksseii;

import ru.aleksseii.database.FlywayInitializer;

public final class Main {

    public static void main(String[] args) {

        FlywayInitializer.initDB();
    }
}