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
- Uncheck *Using Slack*, and enter your Slack [Authentication Token](https://get.slack.help/hc/en-us/articles/215770388-Create-and-regenerate-API-tokens) and a [channel](https://get.slack.help/hc/en-us/categories/200111606-Using-Slack#work-in-channels) name.
- Click the *Test Connection* to check your credentials. If everything is OK, a message should be sent to Slack.

![test_message](/docs/images/test_message.png)
- Click the *Apply* button then *OK* to close the Project Settings dialog.

#### Run test execution
- Execute a test suite and wait for the execution finished, a summary message should be sent to your Slack channel.

![summary_message](/docs/images/summary_message.png)

## Companion products

### Katalon TestOps

[Katalon TestOps](https://analytics.katalon.com) is a web-based application that provides dynamic perspectives and an insightful look at your automation testing data. You can leverage your automation testing data by transforming and visualizing your data; analyzing test results; seamlessly integrating with such tools as Katalon Studio and Jira; maximizing the testing capacity with remote execution.

* Read our [documentation](https://docs.katalon.com/katalon-analytics/docs/overview.html).
* Ask a question on [Forum](https://forum.katalon.com/categories/katalon-analytics).
* Request a new feature on [GitHub](CONTRIBUTING.md).
* Vote for [Popular Feature Requests](https://github.com/katalon-analytics/katalon-analytics/issues?q=is%3Aopen+is%3Aissue+label%3Afeature-request+sort%3Areactions-%2B1-desc).
* File a bug in [GitHub Issues](https://github.com/katalon-analytics/katalon-analytics/issues).

### Katalon Studio
[Katalon Studio](https://www.katalon.com) is a free and complete automation testing solution for Web, Mobile, and API testing with modern methodologies (Data-Driven Testing, TDD/BDD, Page Object Model, etc.) as well as advanced integration (JIRA, qTest, Slack, CI, Katalon TestOps, etc.). Learn more about [Katalon Studio features](https://www.katalon.com/features/).
