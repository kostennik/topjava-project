package ru.javawebinar.topjava;

import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.TestUtil.readFromJsonMvcResult;
import static ru.javawebinar.topjava.TestUtil.readListFromJsonMvcResult;

public class TestData {

    public static <T> void assertMatch(String[] ignoredFields, T actual, T expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, ignoredFields);
    }

    @SafeVarargs
    public static <T> void assertMatch(String[] ignoredFields, Iterable<T> actual, T... expected) {
        assertMatch(ignoredFields, actual, List.of(expected));

    }

    public static <T> void assertMatch(String[] ignoredFields, Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields(ignoredFields).isEqualTo(expected);

    }

    public static <T> ResultMatcher contentJson(String[] ignoredFields, Class<T> tClass, T expected) {
        return result -> assertMatch(ignoredFields, readFromJsonMvcResult(result, tClass), expected);
    }

    @SafeVarargs
    public static <T> ResultMatcher contentJson(String[] ignoredFields, Class<T> tClass, T... expected) {
        return contentJson(ignoredFields, tClass, List.of(expected));

    }

    public static <T> ResultMatcher contentJson(String[] ignoredFields, Class<T> tClass, Iterable<T> expected) {
        return result -> assertMatch(ignoredFields, readListFromJsonMvcResult(result, tClass), expected);
    }
}
