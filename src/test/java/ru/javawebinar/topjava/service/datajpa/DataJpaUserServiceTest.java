package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collections;

import static ru.javawebinar.topjava.MealTestData.MEALS;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.NEW_USER;
import static ru.javawebinar.topjava.UserTestData.NOT_EXIST_ID;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {
    @Test
    @Override
    public void getWithMeals() {
        User user = userService.getWithMeals(UserTestData.USER_ID);
        assertMatch(user.getMeals(), MEALS);
    }

    @Test
    public void getNotFoundWithMeals() {
        thrown.expect(NotFoundException.class);
        userService.getWithMeals(NOT_EXIST_ID);
    }

    @Test
    public void getWithNotExistMeals() {
        User created = userService.create(new User(NEW_USER));
        User userWithMeals = userService.getWithMeals(created.getId());
        assertMatch(userWithMeals.getMeals(), Collections.EMPTY_LIST);
    }
}
