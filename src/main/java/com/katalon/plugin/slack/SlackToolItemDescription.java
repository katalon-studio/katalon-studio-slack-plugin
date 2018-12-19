package com.katalon.plugin.slack;

import com.katalon.platform.api.util.DialogUtil;
import com.katalon.platform.api.extension.ToolItemDescription;

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
        return "platform:/plugin/"+ SlackConstants.PLUGIN_ID + "/icons/slack_32x24.png";
    }

    @Override
    public void handleEvent() {
        DialogUtil.openPreferencePage("com.katalon.plugin.slack.SlackPreferencePage");
    }

    @Override
    public boolean isItemEnabled() {
        return true;
    }
}
