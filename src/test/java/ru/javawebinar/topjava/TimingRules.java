package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TimingRules extends Stopwatch {
    private static final Logger log = LoggerFactory.getLogger("result");
    private static final String DIVIDER = "\n" + "-".repeat(36);
    private static StringBuilder report = new StringBuilder();
    private static long totalTime;

    @Override
    protected void finished(long nanos, Description description) {
        if (totalTime == 0) addHeader(description);
        long duration = TimeUnit.NANOSECONDS.toMillis(nanos);
        totalTime += duration;
        String result = String.format("\n %-25s %8s", description.getMethodName(), duration);
        report.append(result);
        log.info(result + " ms");
    }

    private void addHeader(Description description) {
        String[] arr = description.getClassName().split("\\.");
        report.append(String.format("\n %-5s %s", "", arr[arr.length - 1]));
        report.append(DIVIDER);
    }

    public static void getReport() {
        report.append(DIVIDER);
        report.append(String.format("\n %-25s %8s \n", "TOTAL (ms)", totalTime));
        log.info(report.toString());
        totalTime = 0;
        report = new StringBuilder();
    }
}