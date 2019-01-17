package com.katalon.plugin.slack;

import com.katalon.platform.api.extension.ToolItemDescription;
import com.katalon.platform.api.service.ApplicationManager;
import com.katalon.platform.api.ui.DialogActionService;

public class SlackToolItemDescription implements ToolItemDescription {

    @Override
    public String name() {
        return "Slack";
    }

    @Override
    public String toolItemId() {
        return SlackConstants.PLUGIN_ID + ".slackToolItem";
    }

    @Override
    public String iconUrl() {
        return "platform:/plugin/" + SlackConstants.PLUGIN_ID + "/icons/slack_32x24.png";
    }

    @Override
    public void handleEvent() {
        ApplicationManager.getInstance().getUIServiceManager().getService(DialogActionService.class).openPluginPreferencePage(
                SlackConstants.PREF_PAGE_ID);
    }

    @Override
    public boolean isItemEnabled() {
        return true;
    }
}
