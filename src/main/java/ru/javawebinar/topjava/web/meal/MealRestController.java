package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.util.Collection;

@Controller
public class MealRestController {
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Collection<Meal> getAll(int userId) {
        return service.getAll(userId);
    }

    public Meal get(int id, int userId) {
        return service.get(id, userId);
    }

    public Meal create(Meal meal) {
        return service.create(meal);
    }

    public void delete(int id, int userId) {
        service.delete(id, userId);
    }

    public void update(Meal meal) {
        service.update(meal);
    }
}