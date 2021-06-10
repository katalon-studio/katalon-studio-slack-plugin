package com.katalon.plugin.slack;

public interface SlackConstants {
    String PLUGIN_ID = "com.katalon.katalon-studio-slack-plugin";
    
    String PREF_PAGE_ID = "com.katalon.plugin.slack.SlackPluginPreferenPage"; 

    String PREF_IS_SLACK_ENABLED = "isSlackEnabled";

    String PREF_AUTH_TOKEN = "authentication.token";
    
    String PREF_AUTH_CHANNEL = "authentication.channel";
    
    String OAUTH_TOKEN_HEADER_NAME = "Authorization";
    
    String OAUTH_TOKEN_HEADER_VALUE = "Bearer {0}";
}
