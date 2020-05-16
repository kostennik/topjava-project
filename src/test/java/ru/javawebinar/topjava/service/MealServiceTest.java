package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;

import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal expected = new Meal(LocalDateTime.of(2019, Month.OCTOBER, 22, 22, 0), "Позний ужин", 200);
        Meal actual = service.create(expected, USER_ID);
        expected.setId(START_SEQ + 14);
        assertMatch(actual, expected);
        assertMatch(service.get(START_SEQ + 14, USER_ID), expected);
    }

    @Test(expected = DuplicateKeyException.class)
    public void createDuplicateDateTime() {
        Meal expected = new Meal(LocalDateTime.of(2019, Month.OCTOBER, 22, 20, 0), "Дублирующийся ужин", 100);
        service.create(expected, USER_ID);
    }

    @Test
    public void get() {
        assertMatch(service.get(MEAL_1_ID, USER_ID), MEAL_1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(MEAL_1_ID, ADMIN_ID);
    }

    @Test
    public void update() {
        Meal expected = new Meal(MEAL_1_ID, LocalDateTime.of(2019, Month.OCTOBER, 20, 10, 0), "Обновленный завтрак", 1000);
        service.update(expected, USER_ID);
        Meal actual = service.get(MEAL_1_ID, USER_ID);
        assertMatch(actual, expected);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        Meal expected = new Meal(START_SEQ + 6, LocalDateTime.of(2019, Month.OCTOBER, 22, 20, 0), "Обновленный ужин", 610);
        service.update(expected, ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(MEAL_1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL_6, MEAL_5, MEAL_4, MEAL_3, MEAL_2);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(MEAL_1_ID, ADMIN_ID);
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), MEAL_LIST_USER);
    }

    @Test
    public void getAllNotExist() {
        assertMatch(service.getAll(NOT_EXIST_ID), Collections.emptyList());
    }

    @Test
    public void getBetweenDates() {
        assertMatch(service.getBetweenDates(LocalDate.of(2019, Month.OCTOBER, 22), LocalDate.of(2019, Month.OCTOBER, 22), USER_ID), MEAL_6, MEAL_5, MEAL_4);
        assertMatch(service.getBetweenDates(LocalDate.of(2019, Month.OCTOBER, 20), LocalDate.of(2019, Month.OCTOBER, 20), USER_ID), MEAL_3, MEAL_2, MEAL_1);
        assertMatch(service.getBetweenDates(LocalDate.of(2019, Month.OCTOBER, 20), LocalDate.of(2019, Month.OCTOBER, 22), USER_ID), MEAL_LIST_USER);
    }

    @Test
    public void getBetweenDatesNotExist() {
        assertMatch(service.getBetweenDates(LocalDate.of(2019, Month.OCTOBER, 21), LocalDate.of(2019, Month.OCTOBER, 21), USER_ID), Collections.emptyList());
        assertMatch(service.getBetweenDates(LocalDate.of(2019, Month.OCTOBER, 22), LocalDate.of(2019, Month.OCTOBER, 22), NOT_EXIST_ID), Collections.emptyList());
    }
}