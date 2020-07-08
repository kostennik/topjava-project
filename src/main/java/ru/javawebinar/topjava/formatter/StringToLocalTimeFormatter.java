package ru.javawebinar.topjava.formatter;

import org.springframework.format.Formatter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class StringToLocalTimeFormatter implements Formatter<LocalTime> {

    private DateTimeFormatter formatter = (DateTimeFormatter.ofPattern("HH:mm"));

    public String print(LocalTime date, Locale locale) {
        if (date == null) {
            return "";
        }
        return date.format(formatter);
    }

    public LocalTime parse(String formatted, Locale locale) {
        if (formatted.isEmpty()) {
            return null;
        }
        return LocalTime.parse(formatted, formatter);
    }
}
