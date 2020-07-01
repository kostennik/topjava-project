package ru.javawebinar.topjava.service;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;

import static java.time.LocalDateTime.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.TestData.assertMatch;

public abstract class AbstractMealServiceTest extends AbstractServiceTest {

    @Autowired
    protected MealService service;

    @Test
    void delete() {
        service.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () ->
                service.get(MEAL1_ID, USER_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.delete(1, USER_ID));
    }

    @Test
    void deleteNotOwn() {
        assertThrows(NotFoundException.class, () ->
                service.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    void create() {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(MEAL_IGNORE, created, newMeal);
        assertMatch(MEAL_IGNORE, service.get(newId, USER_ID), newMeal);
    }

    @Test
    void get() {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(MEAL_IGNORE, actual, ADMIN_MEAL1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.get(1, ADMIN_ID));
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class, () ->
                service.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(MEAL_IGNORE, service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void updateNotFound() {
        NotFoundException e = assertThrows(NotFoundException.class, () -> service.update(MEAL1, ADMIN_ID));
        assertEquals(e.getMessage(), "Not found entity with id=" + MEAL1_ID);
    }

    @Test
    void getAll() {
        assertMatch(MEAL_IGNORE, service.getAll(USER_ID), MEALS);
    }

    @Test
    void getBetween() {
        assertMatch(MEAL_IGNORE, service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL3, MEAL2, MEAL1);
    }

    @Test
    void getBetweenWithNullDates() {
        assertMatch(MEAL_IGNORE, service.getBetweenDates(null, null, USER_ID), MEALS);
    }

    @Test
     void createWithException() throws Exception {
        Assumptions.assumeTrue(isJpaBased(), "Validation not supported (JPA only)");
        validateRootCause(() -> service.create(new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "  ", 300), USER_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Meal(null, null, "Description", 300), USER_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "Description", 9), USER_ID), ConstraintViolationException.class);
        validateRootCause(() -> service.create(new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "Description", 5001), USER_ID), ConstraintViolationException.class);
    }
}