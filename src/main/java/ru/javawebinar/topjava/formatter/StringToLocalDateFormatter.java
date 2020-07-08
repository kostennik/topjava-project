package ru.javawebinar.topjava.formatter;

import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class StringToLocalDateFormatter implements Formatter<LocalDate> {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public String print(LocalDate date, Locale locale) {
        if (date == null) {
            return "";
        }
        return date.format(formatter);
    }

    public LocalDate parse(String formatted, Locale locale) {
        if (formatted.isEmpty()) {
            return null;
        }
        return LocalDate.parse(formatted, formatter);
    }
}
