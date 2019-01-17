package com.katalon.plugin.slack;

import com.katalon.platform.api.exception.ResourceException;
import com.katalon.platform.api.preference.PluginPreference;
import com.katalon.platform.api.service.ApplicationManager;

public interface SlackComponent {
    default PluginPreference getPluginStore() throws ResourceException {
        PluginPreference pluginStore = ApplicationManager.getInstance().getPreferenceManager().getPluginPreference(
                ApplicationManager.getInstance().getProjectManager().getCurrentProject().getId(),
                SlackConstants.PLUGIN_ID);
        return pluginStore;
    }
}
