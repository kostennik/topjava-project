package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimeParser {
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public DateTimeParser(String startDate, String endDate, String startTime, String endTime) {
        if (!startDate.isEmpty())
            this.startDate = LocalDate.parse(startDate);

        if (!endDate.isEmpty())
            this.endDate = LocalDate.parse(endDate);

        if (!startTime.isEmpty())
            this.startTime = LocalTime.parse(startTime);

        if (!endTime.isEmpty())
            this.endTime = LocalTime.parse(endTime);
    }

    public DateTimeParser(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        this.startDate = startDate != null ? startDate : LocalDate.MIN;
        this.endDate = endDate != null ? endDate : LocalDate.MAX;
        this.startTime = startTime != null ? startTime : LocalTime.MIN;
        this.endTime = endTime != null ? endTime : LocalTime.MAX;
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