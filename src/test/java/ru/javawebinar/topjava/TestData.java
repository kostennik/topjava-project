package ru.javawebinar.topjava;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.TestUtil.readFromJsonMvcResult;
import static ru.javawebinar.topjava.TestUtil.readListFromJsonMvcResult;

public class TestData {

    public static <T> void assertMatch(T actual, T expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, getIgnoreFields(actual));
    }

    @SafeVarargs
    public static <T> void assertMatch(Iterable<T> actual, T... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static <T> void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields(getIgnoreFields(actual.iterator().next())).isEqualTo(expected);
    }

    public static <T> ResultMatcher contentJson(Class<T> tClass, T expected) {
        return result -> assertMatch(readFromJsonMvcResult(result, tClass), expected);
    }

    @SafeVarargs
    public static <T> ResultMatcher contentJson(Class<T> tClass, T... expected) {
        return contentJson(tClass, List.of(expected));
    }

    public static <T> ResultMatcher contentJson(Class<T> tClass, Iterable<T> expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, tClass), expected);
    }

    private static <T> String[] getIgnoreFields(T object) {
        if (object instanceof User) {
            return UserTestData.IGNORE_FIELDS;
        } else if (object instanceof Meal) {
            return MealTestData.IGNORE_FIELDS;
        } else {
            return new String[0];
        }
    }
}
