package com.katalon.plugin.slack;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.katalon.platform.api.extension.ToolItemDescription;
import com.katalon.platform.api.service.ApplicationManager;
import com.katalon.platform.api.ui.DialogActionService;

public class SlackToolItemDescription implements ToolItemDescription {

    private static final Bundle BUNDLE = FrameworkUtil.getBundle(SlackToolItemDescription.class);

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
        String iconPath = IconResolver.resolve(BUNDLE, "icons/slack_32x24.png", "icons-v2/slack.svg");
        return "platform:/plugin/" + SlackConstants.PLUGIN_ID + "/" + iconPath;
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
