package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimeParser {
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private DateTimeParser() {
    }

    public static DateTimeParser parseStringToDate(String startDate, String startTime, String endDate, String endTime) {
        DateTimeParser dateTimeParser = new DateTimeParser();

        if (!startDate.isEmpty())
            dateTimeParser.startDate = LocalDate.parse(startDate);

        if (!endDate.isEmpty())
            dateTimeParser.endDate = LocalDate.parse(endDate);

        if (!startTime.isEmpty())
            dateTimeParser.startTime = LocalTime.parse(startTime);

        if (!endTime.isEmpty())
            dateTimeParser.endTime = LocalTime.parse(endTime);

        return dateTimeParser;
    }

    public static DateTimeParser parseNullDateValuesToDefault(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        DateTimeParser dateTimeParser = new DateTimeParser();

        dateTimeParser.startDate = startDate != null ? startDate : LocalDate.MIN;
        dateTimeParser.endDate = endDate != null ? endDate : LocalDate.MAX;
        dateTimeParser.startTime = startTime != null ? startTime : LocalTime.MIN;
        dateTimeParser.endTime = endTime != null ? endTime : LocalTime.MAX;

        return dateTimeParser;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}