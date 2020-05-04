package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "BuserName", "email@mail.ru", "password", new HashSet<>(Collections.singletonList(Role.ROLE_ADMIN))));
            adminUserController.create(new User(null, "CuserName", "email@mail.ru", "password", new HashSet<>(Collections.singletonList(Role.ROLE_ADMIN))));
            adminUserController.create(new User(null, "AuserName", "email@mail.ru", "password", new HashSet<>(Collections.singletonList(Role.ROLE_USER))));
            adminUserController.getAll().forEach(System.out::println);
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.create(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 18, 0), "Завтрак", 500));
            mealRestController.getAll().forEach(System.out::println);
        }
    }
}
