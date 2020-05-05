package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository {
    Meal save(Meal meal, int userId);

    Meal get(int id, int userId);

    boolean delete(int id, int userId);

    Meal update(Meal meal, int userId);

    List<Meal> getAll(int userId);

    List<Meal> getAllFilteredByDate(int userId, LocalDate start, LocalDate end);
}