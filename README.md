# Katalon Studio plugin for Slack integration

This Katalon Studio plugin provides Slack integration functions.

The main purpose is to collect and send a summary report of an execution to a channel after the execution has completed.

## Build

Requirements:
- JDK 1.8
- Maven 3.3+

`mvn clean package`

## Usage
#### Install
- Install the Katalon Studio v6.0.3 or later.
- Choose *Plugin* > *Install Plugin* in Katalon Studio main menu and select the generated jar file.
- Click on *Slack* toolbar button to configure Slack Integration settings that is under  is under *Plugins* category of Project Settings.
![slack_item](/docs/images/slack_item.png)

#### Slack Settings
- Uncheck *Using Slack*, and enter your Slack [Authentication Token](https://get.slack.help/hc/en-us/articles/215770388-Create-and-regenerate-API-tokens) and a [channel](https://get.slack.help/hc/en-us/categories/200111606-Using-Slack#work-in-channels) name.
- Click the *Test Connection* to check your credentials. If everything is OK, a message should be sent to Slack.
![test_message](/docs/images/test_message.png)
- Click the *Apply* button then *OK* to close the Project Settings dialog.

#### Run test execution
- Execute a test suite and wait for the execution finished, a summary message should be sent to your Slack channel.
![summary_message](/docs/images/summary_message.png)
