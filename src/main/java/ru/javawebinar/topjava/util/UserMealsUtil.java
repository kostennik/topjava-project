package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static ru.javawebinar.topjava.util.TimeUtil.isBetween;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        final List<UserMealWithExceed> filtered = getFilteredWithExceeded(mealList, LocalTime.of(10, 0), LocalTime.of(21, 0), 2000);
        filtered.forEach(System.out::println);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> exceeds = new ArrayList<>();
        Map<LocalDate, Integer> map = new HashMap<>();
        for (UserMeal meal : mealList) {

            final LocalDate date = meal.getDateTime().toLocalDate();
            boolean isBetween = isBetween(meal.getDateTime().toLocalTime(), startTime, endTime);
            int value = map.getOrDefault(date, 0) + meal.getCalories();
            boolean iGreater = value > caloriesPerDay;

            addMealWithExceed(exceeds, meal, isBetween, iGreater);
            map.put(date, value);
        }
        return exceeds;
    }

    private static void addMealWithExceed(List<UserMealWithExceed> exceeds, UserMeal meal, boolean isBetween, boolean isGreater) {
        if (isBetween && isGreater) {
            exceeds.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true));
        } else if (isBetween) {
            exceeds.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), false));
        }
    }
}
