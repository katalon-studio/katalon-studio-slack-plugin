package alex.testplugin;

import com.katalon.platform.api.extension.ui.DialogUtil;
import com.katalon.platform.api.extension.ui.toolbar.ToolItemDescription;

public class SlackToolItemDescription implements ToolItemDescription {

    @Override
    public String name() {
        return "Slack";
    }

    @Override
    public String toolItemId() {
        return "alex.plugin.slackToolItem";
    }

    @Override
    public String iconUrl() {
        return "platform:/plugin/alex.testplugin/icons/slack_32x24.png";
    }

    @Override
    public void handleEvent() {
        DialogUtil.openPreferencePage("alex.testplugin.SlackPreferencePage");
    }

    @Override
    public boolean isItemEnabled() {
        return true;
    }
}
