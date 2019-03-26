package com.katalon.plugin.slack;

import org.osgi.service.event.Event;

import com.katalon.platform.api.event.EventListener;
import com.katalon.platform.api.event.ExecutionEvent;
import com.katalon.platform.api.exception.ResourceException;
import com.katalon.platform.api.execution.TestSuiteExecutionContext;
import com.katalon.platform.api.extension.EventListenerInitializer;
import com.katalon.platform.api.preference.PluginPreference;

public class SlackEventListenerInitializer implements EventListenerInitializer, SlackComponent {

    @Override
    public void registerListener(EventListener listener) {
        listener.on(Event.class, event -> {
            try {
                PluginPreference preferences = getPluginStore();
                boolean isIntegrationEnabled = preferences.getBoolean(SlackConstants.PREF_IS_SLACK_ENABLED, false);
                if (!isIntegrationEnabled) {
                    return;
                }
                String authToken = preferences.getString(SlackConstants.PREF_AUTH_TOKEN, "");
                String channel = preferences.getString(SlackConstants.PREF_AUTH_CHANNEL, "");

                if (ExecutionEvent.TEST_SUITE_FINISHED_EVENT.equals(event.getTopic())) {
                    ExecutionEvent eventObject = (ExecutionEvent) event.getProperty("org.eclipse.e4.data");

                    TestSuiteExecutionContext testSuiteContext = (TestSuiteExecutionContext) eventObject
                            .getExecutionContext();
                    TestSuiteStatusSummary testSuiteSummary = TestSuiteStatusSummary.of(testSuiteContext);
                    System.out.println("Slack: Start sending summary message to channel: " + channel);
                    String message = "Summary execution result of test suite: " + testSuiteContext.getSourceId()
                            + "\nTotal test cases: " + Integer.toString(testSuiteSummary.getTotalTestCases())
                            + "\nTotal passes: " + Integer.toString(testSuiteSummary.getTotalPasses())
                            + "\nTotal failures: " + Integer.toString(testSuiteSummary.getTotalFailures())
                            + "\nTotal errors: " + Integer.toString(testSuiteSummary.getTotalErrors())
                            + "\nTotal skipped: " + Integer.toString(testSuiteSummary.getTotalSkipped());
                    SlackUtil.sendMessage(authToken, channel, message);

                    System.out.println("Slack: Summary message has been successfully sent");
                }
            } catch (ResourceException | SlackException e) {
                e.printStackTrace(System.err);
            }
        });
    }
}
