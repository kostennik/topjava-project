package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.Collection;

public interface MealRepository {
    Meal save(Meal meal, int userId);

    Meal get(int id, int userId);

    boolean delete(int id, int userId);

    Meal update(Meal meal, int userId);

    Collection<Meal> getAll(int userId);

    Collection<Meal> getAllFilteredByDate(int userId, LocalDate start, LocalDate end);
}
