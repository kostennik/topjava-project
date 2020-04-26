package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        final List<UserMealWithExceed> filtered1 = getFilteredWithExceededOne(mealList, LocalTime.of(11, 0), LocalTime.of(13, 0), 2000);
        filtered1.forEach(System.out::println);
        final List<UserMealWithExceed> filtered2 = getFilteredWithExceededTwo(mealList, LocalTime.of(11, 0), LocalTime.of(13, 0), 2000);
        System.out.println(filtered2.equals(filtered1));
        filtered2.forEach(System.out::println);
    }

    private static List<UserMealWithExceed> getFilteredWithExceededOne(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return mealList.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(), Collectors.collectingAndThen(Collectors.toList(), list -> list.stream()
                        .filter(value -> isBetween(value.getDateTime().toLocalTime(), startTime, endTime))
                        .map(mealV -> {
                            boolean isExceeded = list.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay;
                            return new UserMealWithExceed(mealV.getDateTime(), mealV.getDescription(), mealV.getCalories(), isExceeded);
                        })
                        .collect(Collectors.toList()))))
                .values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private static List<UserMealWithExceed> getFilteredWithExceededTwo(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return mealList.stream()
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate())).values()
                .stream().map(v -> {
                    boolean exceed = v.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay;
                    return v.stream()
                            .filter(userMeal -> TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                            .map(userMeal -> new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), exceed))
                            .collect(Collectors.toList());
                }).flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
