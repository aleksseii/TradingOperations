package ru.aleksseii.dao;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CrudDAO<T> {

    @NotNull T get(int id);

    @NotNull List<@NotNull T> all();

    int update(@NotNull T entity);

    void save(@NotNull T entity);

    void delete(int id);

    void deleteAll();
}
