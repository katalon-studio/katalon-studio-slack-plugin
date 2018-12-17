package com.katalon.plugin.slack;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.event.Event;
import org.osgi.service.prefs.Preferences;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.shortcut.Shortcut;
import com.github.seratch.jslack.shortcut.model.ApiToken;
import com.github.seratch.jslack.shortcut.model.ChannelName;
import com.katalon.platform.api.event.EventListener;
import com.katalon.platform.api.event.ExecutionEvent;
import com.katalon.platform.api.execution.TestSuiteExecutionContext;
import com.katalon.platform.api.extension.EventListenerInitializer;

public class SlackEventListenerInitializer implements EventListenerInitializer {

    @Override
    public void registerListener(EventListener listener) {
        listener.on(Event.class, event -> {
            try {
                Preferences preferences = InstanceScope.INSTANCE.getNode(SlackConstants.PLUGIN_ID);
                boolean isIntegrationEnabled = preferences.getBoolean(SlackConstants.PREF_IS_SLACK_ENABLED, false);
                if (!isIntegrationEnabled) {
                    return;
                }
                String authToken = preferences.get(SlackConstants.PREF_AUTH_TOKEN, "");
                String channel = preferences.get(SlackConstants.PREF_AUTH_CHANNEL, "");

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
                                    + "\nTotal passes: " + Integer.toString(testSuiteSummary.getTotalPasses()) 
                                    + "\nTotal failures: " + Integer.toString(testSuiteSummary.getTotalFailures()) 
                                    + "\nTotal errors: "+ Integer.toString(testSuiteSummary.getTotalErrors()));
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        });
    }
}
