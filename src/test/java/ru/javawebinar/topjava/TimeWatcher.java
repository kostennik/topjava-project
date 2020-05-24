package ru.javawebinar.topjava;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeWatcher extends TestWatcher {
    private static final Logger log = LoggerFactory.getLogger(TimeWatcher.class);
    private static final String RED_MARK = "\u001B[31m" + "x" + "\u001B[30m";
    private static final String GREEN_MARK = "\u001B[32m" + "v" + "\u001B[30m";
    private long startTime;
    private static long totalTime;
    private static StringBuilder report = new StringBuilder();

    @Override
    public Statement apply(Statement base, Description description) {
        startTime = System.currentTimeMillis();
        return super.apply(base, description);
    }

    @Override
    protected void succeeded(Description description) {
        super.succeeded(description);
        setResult(description, GREEN_MARK);
    }

    @Override
    protected void failed(Throwable e, Description description) {
        super.failed(e, description);
        setResult(description, RED_MARK);
    }

    public static void initReport(String className) {
        report = new StringBuilder();
        totalTime = 0L;
        String[] arr = className.split("\\.");
        report.append("\n" + "__________________________________________________");
        report.append(String.format("\n %-15s %s", "", arr[arr.length - 1]));
        report.append("\n" + "__________________________________________________");
        report.append(String.format("\n | %-34s | %8s |", "TEST NAME", "TIME(ms)"));
        report.append("\n" + "__________________________________________________");
    }

    public static String getReport() {
        report.append("\n" + "__________________________________________________");
        report.append(String.format("\n | %-34s | %8s |", "TOTAL", totalTime));
        report.append("\n" + "__________________________________________________");
        return report.toString();
    }

    private void setResult(Description description, String redMark) {
        long duration = System.currentTimeMillis() - startTime;
        String methodName = description.getMethodName();
        log.info("Test {} time: {} ms.", methodName, duration);
        totalTime += duration;
        report.append(String.format("\n | %s | %-30s | %8s |", redMark, methodName, duration));
        startTime = 0L;
    }
}