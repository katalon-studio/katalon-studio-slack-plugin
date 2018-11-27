package alex.testplugin;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.shortcut.Shortcut;
import com.github.seratch.jslack.shortcut.model.ApiToken;
import com.github.seratch.jslack.shortcut.model.ChannelName;
import com.katalon.platform.api.extension.event.EventListener;
import com.katalon.platform.api.extension.event.EventListenerInitializer;
import com.katalon.platform.api.extension.execution.ExecutionEvent;
import com.katalon.platform.api.extension.execution.TestSuiteExecutionContext;
import com.katalon.platform.api.extension.execution.impl.TestSuiteExecutionEvent;
import com.katalon.platform.api.extension.execution.impl.TestSuiteStatusSummary;
import com.katalon.platform.api.service.EventConstants;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.event.Event;
import org.osgi.service.prefs.Preferences;

public class SlackEventListenerInitializer implements EventListenerInitializer {

    @Override
    public void registerListener(EventListener listener) {
        listener.on(Event.class, event -> {
                try {
                    Preferences preferences = InstanceScope.INSTANCE
                            .getNode("alex.testplugin");
                    boolean isIntegrationEnabled = preferences.getBoolean("isSlackEnabled", false);
                    if (!isIntegrationEnabled) {
                        return;
                    }
                    String authToken = preferences.get("authentication.token", "");
                    String channel = preferences.get("authentication.channel", "");

                    if (ExecutionEvent.TEST_SUITE_FINISHED_EVENT.equals(event.getTopic())) {
                        TestSuiteExecutionEvent eventObject = (TestSuiteExecutionEvent) event.getProperty(
                                EventConstants.EVENT_DATA_PROPERTY_NAME);
                        Slack slack = Slack.getInstance();
                        ApiToken token = ApiToken.of(authToken);
                        Shortcut shortcut = slack.shortcut(token);

                        TestSuiteExecutionContext testSuiteContext = eventObject.getExecutionContext();
                        TestSuiteStatusSummary testSuiteSummary = TestSuiteStatusSummary.of(testSuiteContext);
                        shortcut.post(ChannelName.of(channel), "From plugin alex.testplugin " +
                                "- Summary execution result of test suite: " + testSuiteContext.getSourceId() + ":\n"
                                + "Total passes: " + Integer.toString(testSuiteSummary.getTotalPasses())
                                + "\nTotal failures: " + Integer.toString(testSuiteSummary.getTotalFailures())
                                + "\nTotal errors: " + Integer.toString(testSuiteSummary.getTotalErrors()));
                    }
                } catch(Exception e){
                    e.printStackTrace(System.out);
                }
        });
    }
}
