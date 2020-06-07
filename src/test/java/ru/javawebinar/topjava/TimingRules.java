package ru.javawebinar.topjava;

import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TimingRules {
    private static final Logger log = LoggerFactory.getLogger("result");
    private static StringBuilder report = new StringBuilder();
    private static final String REPORT_LINE = "\n" + "_".repeat(36);
    private static long totalTime;


    public static final Stopwatch STOPWATCH = new Stopwatch() {
        private String className;

        @Override
        protected void finished(long nanos, Description description) {
            if (totalTime == 0) {
                setResultHeader(description);
            }
            long totalTime = TimeUnit.NANOSECONDS.toMillis(nanos);
            report.append(String.format("\n %-25s %8d", description.getMethodName(), totalTime));
            log.info("\n {} : {} {} ms\n", className, description.getMethodName(),totalTime);
            TimingRules.totalTime += totalTime;
        }

        private void setResultHeader(Description description) {
            String[] arr = description.getDisplayName().split("\\.");
            className = arr[arr.length - 1].split("\\)")[0];
            report.append(String.format("\n %-5s %s", "", className));
            report.append(REPORT_LINE);
            report.append(String.format("\n %-25s %8s", "TEST NAME", "TIME(ms)"));
            report.append(REPORT_LINE);
        }
    };

    public static final ExternalResource SUMMARY = new ExternalResource() {
        @Override
        protected void before() {
            totalTime = 0L;
            report = new StringBuilder();
        }

        @Override
        protected void after() {
            report.append(REPORT_LINE);
            report.append(String.format("\n %-25s %8s \n", "TOTAL", totalTime));
            log.info(report.toString());
        }
    };
}