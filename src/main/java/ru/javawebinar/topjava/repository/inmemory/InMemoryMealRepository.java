package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

public class InMemoryMealRepository extends AbstractInMemoryRepository<Meal> {
    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public List<Meal> getAll() {
        log.info("getAll");
        return (List<Meal>) repository.values();
    }

    @Override
    public Meal getByEmail(String email) {
        throw new UnsupportedOperationException("Class " + this.getClass().getSimpleName() + " not support method getByEmail");
    }
}

