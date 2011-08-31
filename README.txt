USAGE

This plugin enables you to send Confluence pages to myGengo for translation,
manage running jobs and publish the competed localizations in Confluence.

1. Installation and configuration
Upload the .jar in the Plugin category of the administration page of
Confluence. In the configuration menu of the plugin, save the public and
private API key you got from myGengo.
To recive asynchronous updates about your running jobs and comments, set the
"Default Callback URL" in myGengos config page to
'<host>/<confluence_root>/plugins/servlet/callback'.

2. Sending Wiki pages
Navigate to the page you want translated. The 'Translate with myGengo' option
will show under the 'Extras' menu.
On the Translation Form select the right source language, add target languages
and a comment to give directions for the translator. Click the submit button.

3. Manage jobs
A 'Manage myGengo jobs' link will show up on the Dashboard after you installed the plugin. It
lists running and completed translation jobs as well as links to cancel,
approve, review, comment on and publish jobs depending on their status.

BUILDING

The compiled plugin in target/ should be up-to-date. If you want to build the plugin 
from source, install the Confluence plugin SDK from the Atlassian 
webpage (http://confluence.atlassian.com/display/DEVNET/Installing+the+Atlassian+Plugin+SDK).

In the 'mygengo-confluence' directory, use atlas-cli to compile a .jar 
with the 'package' command.

meedidy@googlemail.com
