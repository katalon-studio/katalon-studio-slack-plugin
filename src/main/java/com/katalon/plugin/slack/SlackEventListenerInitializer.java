package com.katalon.plugin.slack;

import org.apache.commons.io.FilenameUtils;
import org.osgi.service.event.Event;
import java.io.File;
import com.katalon.platform.api.event.EventListener;
import com.katalon.platform.api.event.ExecutionEvent;
import com.katalon.platform.api.exception.ResourceException;
import com.katalon.platform.api.execution.TestSuiteExecutionContext;
import com.katalon.platform.api.extension.EventListenerInitializer;
import com.katalon.platform.api.preference.PluginPreference;

public class SlackEventListenerInitializer implements EventListenerInitializer, SlackComponent {

    String authToken;
    String channel;
    String reportDir;
    String apiToken;

    @Override
    public void registerListener(EventListener listener) {
        listener.on(Event.class, event -> {
            try {
                PluginPreference preferences = getPluginStore();
                boolean isIntegrationEnabled = preferences.getBoolean(SlackConstants.PREF_IS_SLACK_ENABLED, false);
                if (!isIntegrationEnabled) {
                    return;
                }
                authToken = preferences.getString(SlackConstants.PREF_AUTH_TOKEN, "");
                channel = preferences.getString(SlackConstants.PREF_AUTH_CHANNEL, "");
                reportDir = preferences.getString(SlackConstants.PREF_REPORT_DIR, "");
                apiToken = preferences.getString(SlackConstants.PREF_AUTH_TOKEN, "");
                if (ExecutionEvent.TEST_SUITE_FINISHED_EVENT.equals(event.getTopic())) {
                    ExecutionEvent eventObject = (ExecutionEvent) event.getProperty("org.eclipse.e4.data");

                    TestSuiteExecutionContext testSuiteContext = (TestSuiteExecutionContext) eventObject
                            .getExecutionContext();
                    TestSuiteStatusSummary testSuiteSummary = TestSuiteStatusSummary.of(testSuiteContext);
                    System.out.println("Slack: Start sending summary message to channel: " + channel);
                    String message = "Summary execution result of test suite: " + testSuiteContext.getSourceId() + ", ID: " + testSuiteContext.getId() 
                            + "\nTotal test cases: " + Integer.toString(testSuiteSummary.getTotalTestCases())
                            + "\nTotal passes: " + Integer.toString(testSuiteSummary.getTotalPasses())
                            + "\nTotal failures: " + Integer.toString(testSuiteSummary.getTotalFailures())
                            + "\nTotal errors: " + Integer.toString(testSuiteSummary.getTotalErrors())
                            + "\nTotal skipped: " + Integer.toString(testSuiteSummary.getTotalSkipped());
                    SlackUtil.sendMessage(authToken, channel, message);

                    System.out.println("Slack: Summary message has been successfully sent");
                    // check and see if report exists, if so, send it:
                    reportDir.trim();
                    if(!reportDir.isEmpty()){
                        File dir = new File(reportDir);
                        FolderFinder(dir, testSuiteContext);
                    }
                }
            } catch (ResourceException | SlackException e) {
                e.printStackTrace(System.err);
            }
        });
    }

    //dig through folders to find the correct report folder for this execution
    //necessary because Suite Collections nest the folder under other IDs' folders
    public void FolderFinder(File dir, TestSuiteExecutionContext context) throws SlackException {
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.isDirectory()) {
                   if(child.getName().equals(context.getId())){
                        ReportFinder(child, context);
                   }
                   else{
                       FolderFinder(child, context);
                   }
                }
            }
        }
    }

    //dig through report folder to find the reports
    public void ReportFinder(File dir, TestSuiteExecutionContext context) throws SlackException {
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.isDirectory()) {
                    ReportFinder(child, context);
                } else {
                    String ext = FilenameUtils.getExtension(child.getName());
                    if (ext.equals("pdf") || ext.equals("html") || ext.equals("csv")) {
                        SlackUtil.sendFile(authToken, channel, context.getSourceId(), context.getId(), child);
                    }
                }
            }
        }
    }
}