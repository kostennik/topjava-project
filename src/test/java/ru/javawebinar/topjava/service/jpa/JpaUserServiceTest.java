package ru.javawebinar.topjava.service.jpa;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.service.TimeWatcher;
import ru.javawebinar.topjava.service.UserServiceTest;

import java.lang.invoke.MethodHandles;

@ActiveProfiles(Profiles.JPA)
public class JpaUserServiceTest extends UserServiceTest {
    @BeforeClass
    public static void beforeClassFunction() {
        TimeWatcher.initReport(MethodHandles.lookup().lookupClass().getSimpleName());
    }

    @Test
    public void getUnsupportedOperationWithMeal() {
        thrown.expect(UnsupportedOperationException.class);
        service.getWithMeals(UserTestData.USER_ID);
    }
}
