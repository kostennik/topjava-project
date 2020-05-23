package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.TimeWatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;

import static java.time.LocalDateTime.of;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    private static final Logger log = LoggerFactory.getLogger(MealServiceTest.class);
    private static final String NOT_FOUND_ENTITY_WITH_ID = "Not found entity with id=";

    @Autowired
    private MealService service;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Rule
    public final TestRule watchman = new TimeWatcher();

    @BeforeClass
    public static void beforeClassFunction() {
        TimeWatcher.initReport(log.getName());
    }

    @AfterClass
    public static void afterClassFunction() {
        log.info(TimeWatcher.getReport());
    }

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    public void deleteNotFound() {
        expectedExceptionWithMessage(NotFoundException.class, NOT_FOUND_ENTITY_WITH_ID + 1);
        service.delete(1, USER_ID);
    }

    @Test
    public void deleteNotOwn() {
        expectedExceptionWithMessage(NotFoundException.class, NOT_FOUND_ENTITY_WITH_ID + MEAL1_ID);
        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = getCreated();
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(newMeal, created);
        assertMatch(service.getAll(USER_ID), newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void get() {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() {
        expectedExceptionWithMessage(NotFoundException.class, NOT_FOUND_ENTITY_WITH_ID + 1);
        service.get(1, USER_ID);
    }

    @Test
    public void getNotOwn() {
        expectedExceptionWithMessage(NotFoundException.class, NOT_FOUND_ENTITY_WITH_ID + MEAL1_ID);
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() {
        expectedExceptionWithMessage(NotFoundException.class, NOT_FOUND_ENTITY_WITH_ID + MEAL1.getId());
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void createDuplicateDateTime() {
        expectedExceptionWithMessage(DataAccessException.class, "");
        Meal meal = new Meal(of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
        service.create(meal, USER_ID);
    }

    @Test
    public void getBetween() {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30),
                USER_ID), MEAL3, MEAL2, MEAL1);
        assertMatch(service.getBetweenDates(
                null,
                LocalDate.of(2015, Month.MAY, 30),
                USER_ID), MEAL3, MEAL2, MEAL1);
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 31),
                null,
                USER_ID), MEAL6, MEAL5, MEAL4);
    }

    private void expectedExceptionWithMessage(Class<? extends RuntimeException> exc, String msg) {
        exception.expect(exc);
        exception.expectMessage(msg);
    }
}