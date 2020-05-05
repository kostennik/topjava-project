package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetween;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private AtomicInteger counter = new AtomicInteger(0);
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();

    {
        log.info("populating meal repository");
        MealsUtil.MEALS.forEach(meal -> save(meal, SecurityUtil.authUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);
        if (meal != null && meal.isNew()) {
            Map<Integer, Meal> mealMap = repository.getOrDefault(userId, new HashMap<>());
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);
            repository.put(userId, mealMap);
            return meal;
        }
        return null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap != null ? mealMap.get(id) : null;
    }

    @Override
    public Meal update(Meal meal, int userId) {
        log.info("update {}", meal);
        if (meal != null) {
            Map<Integer, Meal> mealMap = repository.get(userId);
            return mealMap != null ? mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) : null;
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap != null && mealMap.remove(id) != null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("getAll sorted by date desc");
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap != null)
            return mealMap.values()
                    .stream()
                    .sorted(Comparator.comparing(Meal::getDate).thenComparing(Meal::getTime).reversed())
                    .collect(Collectors.toList());
        return Collections.EMPTY_LIST;
    }

    @Override
    public Collection<Meal> getAllFilteredByDate(int userId, LocalDate start, LocalDate end) {
        log.info("getAllFilteredByDate sorted by date desc");
        return getAll(userId).stream()
                .filter(meal -> isBetween(meal.getDate(), start, end))
                .collect(Collectors.toList());
    }
}

