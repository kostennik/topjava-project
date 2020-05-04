package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalTime;

public class MealFilter {
    private LocalDate startDate = LocalDate.MIN;
    private LocalDate endDate = LocalDate.MAX;
    private LocalTime startTime = LocalTime.MIN;
    private LocalTime endTime = LocalTime.MAX;

    public MealFilter(String startDate, String endDate, String startTime, String endTime) {
        if (startDate != null && !startDate.isEmpty())
            this.startDate = LocalDate.parse(startDate);
        if (endDate != null && !endDate.isEmpty())
            this.endDate = LocalDate.parse(endDate);
        if (startTime != null && !startTime.isEmpty())
            this.startTime = LocalTime.parse(startTime);
        if (endTime != null && !endTime.isEmpty())
            this.endTime = LocalTime.parse(endTime);
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