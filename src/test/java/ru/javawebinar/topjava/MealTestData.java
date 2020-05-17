package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_1_ID = START_SEQ + 2;

    public static final Meal MEAL_6 = new Meal(START_SEQ + 7, LocalDateTime.of(2019, Month.OCTOBER, 22, 20, 0), "Ужин", 500);
    public static final Meal MEAL_5 = new Meal(START_SEQ + 6, LocalDateTime.of(2019, Month.OCTOBER, 22, 14, 0), "Обед", 500);
    public static final Meal MEAL_4 = new Meal(START_SEQ + 5, LocalDateTime.of(2019, Month.OCTOBER, 22, 10, 0), "Завтрак", 1000);
    public static final Meal MEAL_3 = new Meal(START_SEQ + 4, LocalDateTime.of(2019, Month.OCTOBER, 20, 20, 0), "Ужин", 510);
    public static final Meal MEAL_2 = new Meal(START_SEQ + 3, LocalDateTime.of(2019, Month.OCTOBER, 20, 14, 0), "Обед", 500);
    public static final Meal MEAL_1 = new Meal(MEAL_1_ID, LocalDateTime.of(2019, Month.OCTOBER, 20, 10, 0), "Завтрак", 1000);

    public static final List<Meal> MEAL_LIST_USER = new ArrayList<>(Arrays.asList(MEAL_6, MEAL_5, MEAL_4, MEAL_3, MEAL_2, MEAL_1));

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static void assertMatchIsBetweenDates(Iterable<Meal> actual, LocalDate startDate, LocalDate endDate) {
        actual.forEach(meal -> assertThat(meal.getDate()).isBetween(
                startDate != null ? startDate : LocalDate.MIN,
                endDate != null ? endDate : LocalDate.MAX));
    }
}
