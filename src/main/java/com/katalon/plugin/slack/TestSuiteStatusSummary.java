package com.katalon.plugin.slack;

import com.katalon.platform.api.execution.TestSuiteExecutionContext;

public class TestSuiteStatusSummary {
    private int totalPasses;

    private int totalFailures;

    private int totalErrors;

    private int totalSkipped;

    private TestSuiteStatusSummary() {
        totalPasses = 0;
        totalFailures = 0;
        totalErrors = 0;
        totalSkipped = 0;
    }

    public int getTotalPasses() {
        return totalPasses;
    }

    public int getTotalFailures() {
        return totalFailures;
    }

    public int getTotalErrors() {
        return totalErrors;
    }

    public int getTotalSkipped() {
        return totalSkipped;
    }

    public static TestSuiteStatusSummary of(TestSuiteExecutionContext testSuiteContext) {
        TestSuiteStatusSummary summary = new TestSuiteStatusSummary();

        testSuiteContext.getTestCaseContexts().forEach(tcContext -> {
            switch (tcContext.getTestCaseStatus()) {
                case "PASSED":
                    summary.totalPasses++;
                    break;
                case "FAILED":
                    summary.totalFailures++;
                    break;
                case "ERROR":
                    summary.totalErrors++;
                    break;
                default:
                    summary.totalSkipped++;
            }
        });
        return summary;
    }
}
