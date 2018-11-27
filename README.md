##katalon-slack-plugin
This project is supporting Katalon-Slack integration.
The main purpose is collecting and sending a summary report of an execution (for Test Suite only) to a channel after the execution completes.

####Build
Requirements:
- JDK 1.8
- Maven 3.3+

Build

`mvn clean install`

####How to test in Katalon Studio
- Checkout or get a build of branch `demo_new_platform` of KS
- After KS opens, please click on `Plugin` menu, select `Install Plugin` and choose the generated jar file.
- Click on `Slack` item on KS main toolbar to configure your slack preferences.
- Execute a test suite and wait for a summary message after the execute completes.
- If you want to reload this plugin, please click on `Plugin` menu, select `Uninstall Plugin` then select `Install Plugin` again. 