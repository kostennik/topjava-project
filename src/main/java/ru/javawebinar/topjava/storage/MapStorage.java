package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MapStorage {
    private Map<Integer, Meal> map = new ConcurrentHashMap<Integer, Meal>();
    private AtomicInteger atomicId = new AtomicInteger(0);

    public Meal save(Meal meal) {
        if(meal.getId() == null) {
            meal.setId(atomicId.getAndIncrement());
        }
        return map.put(meal.getId(), meal);
    }

    public Meal get(Integer id) {
        return id != null ? map.get(id) : null;
    }

    public void delete(Integer id) {
        if(id != null) {
            map.remove(id);
        }
    }

    public List<Meal> getAll() {
       return new ArrayList<>(map.values());
    }

    public int size() {
        return map.size();
    }

    public void clean() {
        map.clear();
    }
}
