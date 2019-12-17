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
- Install the Katalon Studio v6.0.3 or later: [win32](https://s3.amazonaws.com/katalon/release-beta/6.0.3/Katalon_Studio_Windows_32.zip), [win64](https://s3.amazonaws.com/katalon/release-beta/6.0.3/Katalon_Studio_Windows_64.zip), [macOS](https://s3.amazonaws.com/katalon/release-beta/6.0.3/Katalon+Studio.dmg), and [linux64](https://s3.amazonaws.com/katalon/release-beta/6.0.3/Katalon_Studio_Linux_64.tar.gz).
- Choose *Plugin* > *Install Plugin* in Katalon Studio main menu and select the generated jar file.
- Click on *Slack* toolbar button to configure Slack Integration settings that is under  is under *Plugins* category of Project Settings.

![slack_item](/docs/images/slack_item.png)

#### Slack Settings
- Uncheck *Using Slack*, and enter your Slack [Authentication Token](https://get.slack.help/hc/en-us/articles/215770388-Create-and-regenerate-API-tokens), a [channel](https://get.slack.help/hc/en-us/categories/200111606-Using-Slack#work-in-channels) name, and the location of your report directory (leave blank to only report test results summary)
- Click the *Test Connection* to check your credentials. If everything is OK, a message should be sent to Slack.

![test_message](/docs/images/test_message.png)
- Click the *Apply* button then *OK* to close the Project Settings dialog.

#### Run test execution
- Execute a test suite and wait for the execution finished, a summary message should be sent to your Slack channel.

![summary_message](/docs/images/summary_message.png)

#### Report file upload
- If you have generated reports with the report plug in, and you have indicated the location of your Reports folder in the Slack Integration settings, any report files (html, csv, or pdf) found under the ID for test suites executed will be uploaded to the same Slack channel.
- this requires the *files:write:user* scope for your app.