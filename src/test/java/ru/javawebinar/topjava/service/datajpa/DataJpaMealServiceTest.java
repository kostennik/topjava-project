package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.MEAL1_ID;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {
    @Test
    @Override
    public void getWithUser() {
        Meal meal = mealService.getWithUser(MEAL1_ID, USER_ID);
        User user = meal.getUser();
        UserTestData.assertMatch(user, USER);
    }

    @Test
    public void getNotFoundWithUser() {
        thrown.expect(NotFoundException.class);
        mealService.getWithUser(MEAL1_ID, ADMIN_ID);
    }
}