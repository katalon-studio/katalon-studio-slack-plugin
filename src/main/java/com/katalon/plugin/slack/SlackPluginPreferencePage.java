package com.katalon.plugin.slack;

import org.eclipse.jface.preference.PreferencePage;

import com.katalon.platform.api.extension.PluginPreferencePage;

public class SlackPluginPreferencePage implements PluginPreferencePage {

    @Override
    public String getName() {
        return "Slack";
    }

    @Override
    public String getPageId() {
        return "com.katalon.plugin.slack.SlackPluginPreferenPage";
    }

    @Override
    public Class<? extends PreferencePage> getPreferencePageClass() {
        return SlackPreferencePage.class;
    }

}
