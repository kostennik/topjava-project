package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.*;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public Meal get(int id, int userId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public void update(Meal meal, int userId) throws NotFoundException {
        checkNotFoundWithId(repository.update(meal, userId), meal.getId());
    }

    public void delete(int id, int userId) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public List<MealTo> getAll(int userId) {
        final List<Meal> meals = repository.getAll(userId);
        return getTos(meals, DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAllFiltered(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        final List<Meal> allFilteredByDate = repository.getAllFilteredByDate(userId, startDate, endDate);
        return getFilteredTos(allFilteredByDate, DEFAULT_CALORIES_PER_DAY, startTime, endTime);
    }
}