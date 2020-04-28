package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {
    Meal save(Meal meal);

    Meal get(Integer id);

    void delete(Integer id);

    List<Meal> getAll();

    int size();

    void clean();
}
