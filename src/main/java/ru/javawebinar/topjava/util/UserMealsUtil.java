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
        final List<UserMealWithExceed> filtered = getFilteredWithExceeded(mealList, LocalTime.of(10, 0), LocalTime.of(13, 0), 2000);
        filtered.forEach(System.out::println);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> exceeds = new ArrayList<>();
        Map<LocalDate, Integer> map = new HashMap<>();
        List<UserMeal> meals = new ArrayList<>();
        for (UserMeal meal : mealList) {
            if(isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                meals.add(meal);
            }
            final LocalDate date = meal.getDateTime().toLocalDate();
            int value = map.getOrDefault(date, 0) + meal.getCalories();
            map.put(date, value);
        }
        for(UserMeal meal : meals) {
            exceeds.add(new UserMealWithExceed(
                    meal.getDateTime(),
                    meal.getDescription(),
                    meal.getCalories(),
                    map.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
        }
        return exceeds;
    }



}
