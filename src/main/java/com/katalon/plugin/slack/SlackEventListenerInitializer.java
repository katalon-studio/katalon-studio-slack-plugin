package com.katalon.plugin.slack;

import java.io.IOException;

import org.osgi.service.event.Event;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.shortcut.Shortcut;
import com.github.seratch.jslack.shortcut.model.ApiToken;
import com.github.seratch.jslack.shortcut.model.ChannelName;
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
                    Slack slack = Slack.getInstance();
                    ApiToken token = ApiToken.of(authToken);
                    Shortcut shortcut = slack.shortcut(token);

                    TestSuiteExecutionContext testSuiteContext = (TestSuiteExecutionContext) eventObject
                            .getExecutionContext();
                    TestSuiteStatusSummary testSuiteSummary = TestSuiteStatusSummary.of(testSuiteContext);
                    shortcut.post(ChannelName.of(channel),
                            "Summary execution result of test suite: " + testSuiteContext.getSourceId()
                                    + "\nTotal test case: " + Integer.toString(testSuiteSummary.getTotalTestCases())
                                    + "\nTotal passes: " + Integer.toString(testSuiteSummary.getTotalPasses())
                                    + "\nTotal failures: " + Integer.toString(testSuiteSummary.getTotalFailures())
                                    + "\nTotal errors: " + Integer.toString(testSuiteSummary.getTotalErrors())
                                    + "\nTotal skipped: " + Integer.toString(testSuiteSummary.getTotalSkipped()));
                }
            } catch (ResourceException | IOException | SlackApiException e) {
                e.printStackTrace(System.out);
            }
        });
    }
}
