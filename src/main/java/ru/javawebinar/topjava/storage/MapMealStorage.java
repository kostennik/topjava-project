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
    private static Map<Integer, Meal> map = new ConcurrentHashMap<>();
    private static AtomicInteger atomicId = new AtomicInteger(0);

    static {
        map.put(atomicId.get(), new Meal(atomicId.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        map.put(atomicId.get(), new Meal(atomicId.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        map.put(atomicId.get(), new Meal(atomicId.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        map.put(atomicId.get(), new Meal(atomicId.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        map.put(atomicId.get(), new Meal(atomicId.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        map.put(atomicId.get(), new Meal(atomicId.getAndIncrement(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(atomicId.getAndIncrement());
        }
        return map.put(meal.getId(), meal);
    }

    @Override
    public Meal get(int id) {
        return map.getOrDefault(id, new Meal());
    }

    @Override
    public void delete(int id) {
         map.remove(id);
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
