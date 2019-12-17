package com.katalon.plugin.slack;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.katalon.platform.api.exception.ResourceException;
import com.katalon.platform.api.preference.PluginPreference;
import com.katalon.platform.api.service.ApplicationManager;
import com.katalon.platform.api.ui.UISynchronizeService;

public class SlackPreferencePage extends PreferencePage implements SlackComponent {

    private Button chckEnableIntegration;

    private Group grpAuthentication;

    private Text txtToken;

    private Text txtChannel;

    private Text txtReport;

    private Composite container;

    private Button btnTestConnection;

    private Label lblConnectionStatus;

    private Thread thread;

    @Override
    protected Control createContents(Composite composite) {
        container = new Composite(composite, SWT.NONE);
        container.setLayout(new GridLayout(1, false));

        chckEnableIntegration = new Button(container, SWT.CHECK);
        chckEnableIntegration.setText("Using Slack");

        grpAuthentication = new Group(container, SWT.NONE);
        grpAuthentication.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        GridLayout glAuthentication = new GridLayout(2, false);
        glAuthentication.horizontalSpacing = 15;
        glAuthentication.verticalSpacing = 10;
        grpAuthentication.setLayout(glAuthentication);
        grpAuthentication.setText("Authentication");

        Label lblToken = new Label(grpAuthentication, SWT.NONE);
        lblToken.setText("Authentication Token");
        GridData gdLabel = new GridData(SWT.LEFT, SWT.TOP, false, false);
        lblToken.setLayoutData(gdLabel);

        txtToken = new Text(grpAuthentication, SWT.BORDER);
        GridData gdTxtToken = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gdTxtToken.widthHint = 200;
        txtToken.setLayoutData(gdTxtToken);

        Label lblChannel = new Label(grpAuthentication, SWT.NONE);
        lblChannel.setText("Channel/Group");
        lblToken.setLayoutData(gdLabel);

        txtChannel = new Text(grpAuthentication, SWT.BORDER);
        txtChannel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        Label lblReport = new Label(grpAuthentication, SWT.NONE);
        lblReport.setText("Report Folder (empty = no upload)");
        lblReport.setLayoutData(gdLabel);

        txtReport = new Text(grpAuthentication, SWT.BORDER);
        txtReport.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        btnTestConnection = new Button(grpAuthentication, SWT.PUSH);
        btnTestConnection.setText("Test Connection");
        btnTestConnection.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                testSlackConnection(txtToken.getText(), txtChannel.getText());
            }
        });

        lblConnectionStatus = new Label(grpAuthentication, SWT.NONE);
        lblConnectionStatus.setText("");
        lblConnectionStatus.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));

        handleControlModifyEventListeners();
        initializeInput();

        return container;
    }

    private void testSlackConnection(String token, String channel) {
        btnTestConnection.setEnabled(false);
        lblConnectionStatus.setForeground(lblConnectionStatus.getDisplay().getSystemColor(SWT.COLOR_BLACK));
        lblConnectionStatus.setText("Connecting...");
        thread = new Thread(() -> {
            try {
                SlackUtil.sendMessage(token, channel, "This is a test message from Katalon Studio using Slack Plugin");
                syncExec(() -> {
                    lblConnectionStatus
                            .setForeground(lblConnectionStatus.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
                    lblConnectionStatus.setText("Connection success");
                });
            } catch (Exception e) {
                e.printStackTrace(System.err);
                syncExec(() -> {
                    lblConnectionStatus
                            .setForeground(lblConnectionStatus.getDisplay().getSystemColor(SWT.COLOR_DARK_RED));
                    lblConnectionStatus
                            .setText("Connection failed. Reason: " + StringUtils.defaultString(e.getMessage()));
                });
            } finally {
                syncExec(() -> btnTestConnection.setEnabled(true));
            }
        });
        thread.start();
    }

    void syncExec(Runnable runnable) {
        if (lblConnectionStatus != null && !lblConnectionStatus.isDisposed()) {
            ApplicationManager.getInstance()
                    .getUIServiceManager()
                    .getService(UISynchronizeService.class)
                    .syncExec(runnable);
        }
    }

    private void handleControlModifyEventListeners() {
        chckEnableIntegration.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                recursiveSetEnabled(grpAuthentication, chckEnableIntegration.getSelection());
            }
        });
    }

    public static void recursiveSetEnabled(Control ctrl, boolean enabled) {
        if (ctrl instanceof Composite) {
            Composite comp = (Composite) ctrl;
            for (Control c : comp.getChildren()) {
                recursiveSetEnabled(c, enabled);
                c.setEnabled(enabled);
            }
        } else {
            ctrl.setEnabled(enabled);
        }
    }

    @Override
    public boolean performOk() {
        if (!isControlCreated()) {
            return true;
        }
        try {
            PluginPreference pluginStore = getPluginStore();

            pluginStore.setBoolean(SlackConstants.PREF_IS_SLACK_ENABLED, chckEnableIntegration.getSelection());
            pluginStore.setString(SlackConstants.PREF_AUTH_TOKEN, txtToken.getText());
            pluginStore.setString(SlackConstants.PREF_AUTH_CHANNEL, txtChannel.getText());
            pluginStore.setString(SlackConstants.PREF_REPORT_DIR, txtReport.getText());

            pluginStore.save();
            return true;
        } catch (ResourceException e) {
            MessageDialog.openWarning(getShell(), "Warning", "Unable to update Slack Integration Settings.");
            return false;
        }
    }

    private void initializeInput() {
        try {
            PluginPreference pluginStore = getPluginStore();

            chckEnableIntegration.setSelection(pluginStore.getBoolean(SlackConstants.PREF_IS_SLACK_ENABLED, false));
            chckEnableIntegration.notifyListeners(SWT.Selection, new Event());

            txtToken.setText(pluginStore.getString(SlackConstants.PREF_AUTH_TOKEN, ""));
            txtChannel.setText(pluginStore.getString(SlackConstants.PREF_AUTH_CHANNEL, ""));
            txtReport.setText(pluginStore.getString(SlackConstants.PREF_REPORT_DIR, ""));

            container.layout(true, true);
        } catch (ResourceException e) {
            MessageDialog.openWarning(getShell(), "Warning", "Unable to update Slack Integration Settings.");
        }
    }
}
