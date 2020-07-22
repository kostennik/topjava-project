package ru.javawebinar.topjava;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.TestUtil.readFromJsonMvcResult;
import static ru.javawebinar.topjava.TestUtil.readListFromJsonMvcResult;

public class TestData {

    public static final Map<Class<? extends AbstractBaseEntity>, String[]> ignoreFieldsMap = new HashMap<>();

    static {
        ignoreFieldsMap.put(User.class, new String[]{"registered", "meals"});
        ignoreFieldsMap.put(Meal.class, new String[]{"user"});
    }

    public static <T> void assertMatch(T actual, T expected) {
        String[] ignoredFields = ignoreFieldsMap.getOrDefault(actual.getClass(), new String[0]);
        assertThat(actual).isEqualToIgnoringGivenFields(expected, ignoredFields);
    }

    @SafeVarargs
    public static <T> void assertMatch(Iterable<T> actual, T... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static <T> void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        String[] ignoredFields = ignoreFieldsMap.getOrDefault(actual.iterator().next().getClass(), new String[0]);
        assertThat(actual).usingElementComparatorIgnoringFields(ignoredFields).isEqualTo(expected);
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
}
