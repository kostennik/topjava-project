package ru.javawebinar.topjava;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.TestUtil.readFromJsonMvcResult;
import static ru.javawebinar.topjava.TestUtil.readListFromJsonMvcResult;

public class TestData {

    public static <T> void assertMatch(String[] ignoringFields, T actual, T expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, ignoringFields);
    }

    @SafeVarargs
    public static <T> void assertMatch(String[] ignoringFields, Iterable<T> actual, T... expected) {
        assertMatch(ignoringFields, actual, List.of(expected));
    }

    public static <T> void assertMatch(String[] ignoringFields, Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields(ignoringFields).isEqualTo(expected);
    }

    @SafeVarargs
    public static <T> ResultMatcher contentJson(String[] ignoringFields, Class<T> tClass, T... expected) {
        return result -> assertMatch(ignoringFields, readListFromJsonMvcResult(result, tClass), List.of(expected));
    }

    public static <T> ResultMatcher contentJson(String[] ignoringFields, Class<T> tClass, Meal expected) {
        return result -> assertMatch(ignoringFields, readFromJsonMvcResult(result, tClass), expected);
    }
}
