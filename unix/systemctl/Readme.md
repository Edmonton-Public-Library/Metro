### Fri Feb 11 2022

Project Notes
-------------
## Notes:
As of version 1.3.XX the MeCard server's sip2.properties file requires the following
entries.
```
<entry key="user-not-found">User not found {text from AF field}</entry>
<entry key="user-pin-invalid">Invalid PIN {text from AF field}</entry>
```
Failing to include them will throw SEVERE: 'user-pin-invalid' unset in sip2.properties message.

As of version 1.0 the MeCard server is meant to be run as a service under Linux using systemctl.

## Instructions for setting up the mecard as a service
1) Edit the mecard.service file to suit your environment.
2) Make sure service.sh is in the directory you specified as 'WorkingDirectory' in the mecard.service.
3) Copy the mecard.service file to /etc/systemd/system. You'll need sudo for that.
4) If you have an entry in your crontab to restart the service comment it out. When the service is tested and running remove the entry from the crontab.
5) Start the service:
 sudo systemctl daemon-reload
 sudo systemctl enable mecard.service
 sudo systemctl start mecard
 sudo systemctl status mecard
6) Stop the service with:
 sudo systemctl stop mecard
 
You can still use the Makefile included as well as it now contains the handy shortcuts, but requires admin privileges. It contains another rule; status which shows the output of systemd startup and shutdown messages.


Product Description:
Script written by Andrew Nisbet for Edmonton Public Library, distributable by the enclosed license.
Outputs basic usage stats for reports for Pam.

Repository Information:
This product is under version control using Git.

Dependencies:
-------------
commons-cli-1.2.jar
commons-codec-1.8.jar
fluent-hc-4.3.4.jar
gson-2.2.4.jar
httpclient-4.3.4.jar
httpcore-4.3.2.jar
junit-4.10.jar
mysql-connector-java-5.1.31-bin.jar
sqljdbc4.jar

Known Issues:
-------------
* Fixed - If a customer logs in with the wrong password they can receive the following message.
`
Sorry, we are unable to register your account. :You must have an email address to use this service. Please contact your home library for assistance
`
