package ru.javawebinar.topjava;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeWatcher extends TestWatcher {
    private static final Logger log = LoggerFactory.getLogger(TimeWatcher.class);
    private long startTime;
    private static long totalTime;
    private static StringBuilder report = new StringBuilder();

    @Override
    public Statement apply(Statement base, Description description) {
        startTime = System.currentTimeMillis();
        return super.apply(base, description);
    }

    @Override
    protected void finished(Description description) {
        super.finished(description);
        long duration = System.currentTimeMillis() - startTime;
        String methodName = description.getMethodName();
        log.info("Test {} time: {} ms.", methodName, duration);
        totalTime += duration;
        report.append(String.format("\n | %-30s | %8s |", methodName, duration));
        startTime = 0L;
    }

    public static void initReport(String className) {
        report = new StringBuilder();
        totalTime = 0L;
        String[] arr = className.split("\\.");
        report.append("\n" + "____________________________________________");
        report.append(String.format("\n %-10s %s", "", arr[arr.length - 1]));
        report.append("\n" + "____________________________________________");
        report.append(String.format("\n | %-30s | %8s |", "TEST NAME", "TIME(ms)"));
        report.append("\n" + "____________________________________________");
    }

    public static String getReport() {
        report.append("\n" + "____________________________________________");
        report.append(String.format("\n | %-30s | %8s |", "TOTAL", totalTime));
        report.append("\n" + "____________________________________________");
        return report.toString();
    }
}