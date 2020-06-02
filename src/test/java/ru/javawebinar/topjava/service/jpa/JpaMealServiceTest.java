package ru.javawebinar.topjava.service.jpa;

import org.junit.BeforeClass;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.TimeWatcher;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import java.lang.invoke.MethodHandles;

@ActiveProfiles(Profiles.JPA)
public class JpaMealServiceTest extends AbstractMealServiceTest {
    @BeforeClass
    public static void beforeClassFunction() {
        TimeWatcher.initReport(MethodHandles.lookup().lookupClass().getSimpleName());
    }
}
