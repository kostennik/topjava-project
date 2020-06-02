package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TimeWatcher extends Stopwatch {
    private static final Logger log = LoggerFactory.getLogger("result");

    private static StringBuilder report = new StringBuilder();
    private static final String REPORT_LINE = "\n" + "_".repeat(36);

    private static long totalTime;


    @Override
    protected void finished(long nanos, Description description) {
        long totalTime = TimeUnit.NANOSECONDS.toMillis(nanos);
        String result = String.format("\n %-25s %8d", description.getMethodName(), totalTime);
        report.append(result);
        TimeWatcher.totalTime += totalTime;
        log.info(result + " ms\n");
    }

    public static void initReport(String className) {
        totalTime = 0L;
        report = new StringBuilder();
        String[] arr = className.split("\\.");
        report.append(String.format("\n %-5s %s", "", arr[arr.length - 1]));
        report.append(REPORT_LINE);
        report.append(String.format("\n %-25s %8s", "TEST NAME", "TIME(ms)"));
        report.append(REPORT_LINE);
    }

    public static String getReport() {
        report.append(REPORT_LINE);
        report.append(String.format("\n %-25s %8s \n", "TOTAL", totalTime));
        return report.toString();
    }
}