package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MapMealStorage implements MealStorage {
    private Map<Integer, Meal> map = new ConcurrentHashMap<>();
    private AtomicInteger atomicId = new AtomicInteger(0);

    {
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(atomicId.getAndIncrement());
        }
        map.put(meal.getId(), meal);
        return map.get(meal.getId());
    }

    @Override
    public Meal get(Integer id) {
        Meal meal = id != null ? map.get(id) : null;
        return meal != null ? meal : new Meal();
    }

    @Override
    public void delete(Integer id) {
        if (id != null) map.remove(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void clean() {
        map.clear();
    }
}
