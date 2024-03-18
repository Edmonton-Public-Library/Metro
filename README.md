
# Metro Federation of Libraries

The Metro Federation of Libraries members are Edmonton Public Library (EPL), 
Ft. Saskatchewan Public Library (FSPL), St. Albert Public Library (SAPL), 
and Strathcona County Library (SCL). The Metro Federation members share 
an interest in collaborating to increase access to library collections 
and programs for our customers across the Metro Edmonton region. The 
Me Card project was created to reduce barriers in accessing the library 
collections and diverse programs of the Metro Federation libraries.


# What is the Me Card service?
----------------------------

The Me Card is a web-based service that allows customers with a library 
card from an affiliated Metro Federation library to create an account 
with and access the collections and programs at one or more other 
affiliated Metro Federation libraries.  Interested customers complete 
a self-service web form to create an account with libraries other than 
their home library, letting them use their home library card as their 
library card at any registered library.

Metro is the server that runs on the ILS server and performs
the function of negotiating and translating communications between different ILSs.
The design is centered around a few simple requests that are initiated by the 
customer on a website, then translated and sent to the home (host) library, a 
response is generated, sent back to the website. The website then confirms the 
transaction is a way that ensures that no identifiable customer information is 
stored then passes the response on again as another request to the guest library.
The guest then creates a new user record in their ILS. 

----------
# What's new
----------
## Version 2.03.02
* Horizon sites are warned if they don't use password-restriction entries in `environment.properties`. See [Version 2.02.00 notes](#version-20200) for more information.

## Version 2.03.01
* Horizon sites now use the `SitePasswordRestrictions` as do the `SIP` class(es). The password-restriction entries in `environment.properties` remain optional. See [Version 2.02.00 notes](#version-20200) for more information.

## Version 2.02.00
* Bug fix for Polaris libraries that use `User1` through `User5` properties. These are now set correctly if the site opts to make them required. They can be set up in the site's `CustomerNormalizer` class.

* Polaris sites can now set password restrictions on inbound customer accounts. All passwords that contravene the restrictions are hashed to strings of digits to a maximum 19 digits long. The hashed PIN is then sent back to the customer in an email from the ME Libraries web site. This is similar to how customer PINs are handled on Horizon.

* The **environment.properties** can include *any* or *all* of the following additional **optional** keys.
```xml
<!-- Horizon sites would set this to '4'. -->
<entry key="password-max-length">16</entry>
<!-- Horizon sites would leave this set to '4'. -->
<entry key="password-min-length">4</entry>
<!-- this means only allow digits 0-9 in passwords. -->
<entry key="allowed-password-characters">1234567890</entry>
```

**Note** The default entries (those not specified) are:
```xml
<entry key="password-max-length">256</entry>
<entry key="password-min-length">4</entry>
<entry key="allowed-password-characters">abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_=+[]{}|;:'\",.<>/?` </entry>
```
**Note**: The space character is the only white space allowed by default.

**Note to the developer**: To implement this on another ILS or node you will want to make the changes in the sites `site.example.ExampleCustomerNormalizer.normalize()` and `site.example.ExampleCustomerNormalizer.finalize()` methods.

## Version 2.00.XX
* MeCard servers now support Polaris web services (PAPI) version 7.x. 
* If using PAPI uses the date format (set in the **environment properties**) to `“yyyy-MM-dd'T'HH:mm:ss”`.
* The **environment properties** file should now have an entry to test if a customer exists. While this is called a protocol, the ME Libraries front end never sends this as a request. Instead it is used internally by the Responder object.
```xml
<entry key="exists-protocol">polaris-api</entry> <!-- or symphony-api etc -->
```
* Make sure to add a **papi.properties** file with the correct configuration. Of
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
	<comment>
		Polaris API settings.
	</comment>
	<entry key="load-dir">c:\metro</entry>
	<entry key="timezone-difference">0</entry>           
    <entry key="host">https://search.prl.ab.ca</entry>
    <entry key="rest-path">/PAPIservice/REST</entry>     
    <entry key="internal-domain">PRL</entry>
    <entry key="staff-access-id">MeLibrary</entry>
    <entry key="staff-password">xxxxxxxxxxxx</entry>
    <entry key="papi-version">7.1</entry>                
    <entry key="http-version">2.0</entry>
    <entry key="api-user-id">MeLibrary</entry>
    <entry key="api-key">xxxxxxxxxxx-xxxxxxxxxx-xxxx-xxxxxxx</entry>
    <entry key="version">v1</entry>
    <entry key="language-id">1033</entry>
    <entry key="app-id">100</entry>
    <entry key="org-id">1</entry>
    <entry key="patron-branch-id">3</entry>
    <entry key="login-branch-id">73</entry>    
    <entry key="login-user-id">385</entry>
    <entry key="login-workstation-id">1</entry>    
    <entry key="delivery-option-id">2</entry>  
    <entry key="ereceipt-option-id">2</entry>  
    <entry key="patron-code-id">7</entry>      
    <entry key="connection-timeout">10</entry>
    <entry key="debug">true</entry>
    <entry key="server-type">production</entry> <!-- default: 'sandbox' -->
</properties>
```
* The **message.properties** file needs to have the following fields.
```xml
<entry key="too-many-tries">Attempt to use the service too often, please try again later.</entry>
```
* If you are using **polaris_sql.properties**, this file now must contain additional entries for key stores.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
	<comment>
		SQL connection settings. MY_SQL or SQL_SERVER.
	</comment>
    <entry key="load-dir">./</entry>
    <entry key="connector-type">SQL_SERVER</entry>
    <entry key="conformance">Polaris 7.1</entry>
    <entry key="host">10.108.1.71</entry>
    <entry key="database">Polaris</entry>
    <entry key="username">mesip</entry>
    <entry key="password">S0m3P@ssW0rD</entry>
    <entry key="patron-code-id">7</entry>
    <entry key="organization-id">73</entry>
    <entry key="creator-id">294</entry>
    <entry key="language-id">1</entry>
    <entry key="delivery-option-id">2</entry>
    <entry key="email-format-id">2</entry>
    <entry key="country-id">2</entry>
    <entry key="free-text-label">Home</entry> <!-- ignored in 7.1 and above -->
    <entry key="address-label-id">3</entry>
    <entry key="user-1">Not in the List</entry>
    <entry key="user-2">NULL</entry>    
    <entry key="user-3">null</entry>
    <entry key="user-4">(none)</entry>
    <entry key="user-5">(none)</entry>
    <!-- These are new for MSSQL server 2019 -->
    <entry key="encrypt">true</entry>
    <entry key="trust-server-certificate">true</entry>
    <entry key="integrated-security"></entry>
    <entry key="trust-store"></entry>
    <entry key="trust-store-password"></entry>
    <entry key="host-name-in-certificate"></entry>
</properties>
```

* Windows Server (2019) is now supported. See Windows instalation notes.




## Version 1.00.XX
* The **environment.properties** file now requires the following tag.
  ```xml
  <entry key="ils-type">[ILS_TYPE]</entry>
  ```
  Where ILS_TYPE can be one of the following `UNKNOWN`, `SYMPHONY`, `HORIZON`, `POLARIS` or `SIRSI_DYNIX`.

* The **sip2.properties** file now requires a new entry as follows.
  ```xml
  <entry key="user-not-found">[Text from 'AF' field of the sip2 response]</entry>
  <!-- typically 'User not found' -->
  ```
  The exact message will depend on your vendor, and can change between versions of the same 
  sip2 server implementation. Use the Perl script `sip2emu.pl` and an *invalid user ID* and
  look for the text in the 'AF' fields for the value for this entry.

* The sip2.properties file now requires a new entry as follows.
```xml
<entry key="user-pin-invalid">[Text from 'AF' field of the sip2 response]</entry>
<!-- typically 'Invalid user PIN[ for station user]' -->
```
  The exact message will depend on your vendor, and can change between versions of the same 
  sip2 server implementation. Use the Perl script `sip2emu.pl` and a valid user ID but an *invalid*
  PIN, and look for the text in the 'AF' fields for the value for this entry.

* You can now specify any of *-protocols in the environment.properties file
  to be 'outage'. For example, you can change the get-protocol from 'sip2' 
  to 'outage', as shown below, if you know your sip2 service is going to be unavailable.
  ```xml
  <entry key="get-protocol">outage|sip2|symphony-api</entry>
  ```

* The current MeCard server is built to run in Java 11, and is tested using OpenJDK or (JRE).

* Support for mssql server 2019 is supported.
  These additional entries are required in the **polaris_sql.properties** file.
```xml
    <entry key="encrypt">true|false</entry>
    <entry key="trust-server-certificate">true|false</entry>
    <entry key="integrated-security">true|false</entry>
    <entry key="trust-store">value</entry>
    <entry key="trust-store-password">value</entry>
    <entry key="host-name-in-certificate">value</entry>
```

# Windows Installation
Installing Procrun (prunsrv.exe and prunmgr.exe)
The prunsrv.exe is from commons-daemon-1.1.0-bin-windows.zip /amd64 (REF: http://www.apache.org/dist/commons/daemon/binaries/windows/ and StackOverflow at: https://stackoverflow.com/questions/15543053/prunsrv-exe-service-not-starting-up)

## Installation Steps
The following steps were used at TRAC to install a new MeCard server under Windows server 2019.

0) Review any **properties** file changes. Recent addtions include changes to **environment**, **sip2**, **polaris_sql**, **papi**. 
1) Have a local user account available on the Windows machine with admin privileges.
2) Download the (Open)JDK 11 (or JRE) from [https://jdk.java.net/java-se-ri/11](https://jdk.java.net/java-se-ri/11). You may also download from Oracle, but you may incur licence fees.
3) Unpack the zip file in the `c:\metro\java` directory.
4) Add a `%JAVA_HOME%=c:\metro\java` to the system environment variables and set it to `c:\Metro\java`.
5) Add `%JAVA_HOME%\bin` to the `%PATH%` system environment variable.
6) Test by opening a fresh command prompt and typing `java -version`.
7) The MeCard server runs as a daemon (in Unix). The equivalent in Windows is a service which uses Apache’s Procrun server which you can download here:  [https://commons.apache.org/proper/commons-daemon/index.html](https://commons.apache.org/proper/commons-daemon/index.html). Information can be found here: [https://commons.apache.org/proper/commons-daemon/procrun.html](https://commons.apache.org/proper/commons-daemon/procrun.html). The current version is commons-daemon-1.3.1-bin-windows.zip. Each version will contain a commons-daemon-x.x.x.jar which must be added to the build in the (NetBeans) IDE to match the version of the prunmgr.exe and prunsrv.exe.
8) Download and unpack the latest mecard_Windows.zip file which will include the `MeCard.jar`, lib files, and 64-bit `prunsrv.exe` (AKA procrun) and `prunmgr.exe` files.
9) Update your config files, and move them to `c:\Metro directory` (until I can figure out how to pass a switch and string as parameters).
10) From a powershell ISE window opened in admin mode, with `Set-ExecutionPolicy -ExecutionPolicy Unrestricted -Scope CurrentUser`.
11) Run [this command](#powerShell_installation_command) from an Admin PowerShell (ISE).
12)  If required, tweak the parameters using the **prunmgr.exe** with: `c:\Metro\windows\prunmgr.exe //ES//Metro`. [See section below](#tweaking-settings-with-prunmgrexe).

# Tweaking Settings with `prunmgr.exe`
Run `c:\metro\windows\prnmgr.exe //ES//Metro`
## General Tab
* Display name: **Metro**
* Description: **MeCard Metro Service**
* Path to executable: **c:\metro\windows\prunsrv.exe //RS//Metro**
* Startup type: **Automatic**
## Log On Tab
* Log on as: [x] This account: **Local Service** or **NTADMIN/service** or browse and find the desired account. 
* Password: **xxxxxxxxx** or whatever it is.
* Confirm password: **xxxxxxxxx** or whatever it is.  
Depending on the account you will need to grant that account full control for log files in `c:\metro\logs`.
## Logging Tab
* Level: **info**
* Log path: **c:\metro\logs**
* Log prefix: **commons-daemon**
* Pid file: **c:\metro\logs\pid.txt**
* Redirect Stdout: **c:\metro\logs\stdout.txt**
* Redirect Stderr: **c:\metro\logs\stderr.txt**  
Check that the intial attempt to install the service didn't create any logs in `c:\windows\logFiles\Apache` directory.
## Java Tab
* [x] Use default (if installed as the default and if not uncheck and set the path to the `%JAVA_HOME%/bin/server/jvm.dll`)
* Java Classpath: **c:\metro\dist\MeCard.jar**
## Startup
* Class: **mecard.MetroService**
* Working Path: **c:\metro**
* Method: **start** (this may be disabled, if so select `jvm` in the **Mode** drop-down, enter **start** in the Method text box, and then return **Mode** to **Java**)
## Shutdown
* Class: **mecard.MetroService**
* Working Path: **c:\metro**
* Method: **stop** (this may be disabled, if so select `jvm` in the **Mode** drop-down, enter **stop** in the Method text box, and then return **Mode** to **Java**)

Click apply and Okay, then start `Metro` service in Windows Services.

# PowerShell Installation Command
PowerShell Command to Install the Metro (AKA MeCard) Service.

```PowerShell
c:\metro\windows\prunsrv.exe //IS//Metro "--Description=MeCard --Jvm=auto --Startup=auto --StartMode=Java --Classpath='c:\metro\dist\MeCard.jar' --StartClass=mecard.MetroService --StartMethod=start --StopMode=Java --StopClass=mecard.MetroService --StopMethod=stop --LogPath='c:\metro\logs' --LogLevel=Info --LogPrefix=metro --StdOutput='c:\metro\logs\Metro-stdout.txt' --StdError='c:\metro\logs\Metro-stderr.txt'"
```


# System Event Logs
There are four places to look for information on issues you may be having. 
```
System event logs
c:\Metro\logs\metro-stdout.txt
c:\Metro\logs\metro-stderr.txt
c:\Metro\logs\commons-daemon.YYYY-MM-DD.log
```

To check the settings and trouble shoot, run:
`c:\metro\windows\prunmgr.exe //ES//Metro`
and set the configuration manually from the prunmgr.exe.

# Unix Installation
Andrew to prepare new tarball for installation by editing the MeCard server version number in the `Makefile` in the `MeCard/` directory on the development VM, then running
`ox:~/MeCard/make dist_unix`

## Ubuntu dependencies
**Note**:
---------
If the system has Java 11 running, but has `MeCard.jar` version is 1.x.xx, you may be able to run the server without updating the libs, which will save you updating `jsvc` as well. For Symphony, the minimum libs are `commons-daemon-x.xx.xx.jar`, `gson-2.2.4.jar`, and `commons-cli-1.2.jar`.

* Install `jsvc` with `apt-get`.
* Install OpenJDK.
* Install make with `apt-get`.
* Red Hat dependencies
* A bare Red Hat VM will need to have `jsvc` installed. Here is a list of tool dependencies.
* Gnu `GCC` (`sudo yum install gcc`).
* `Make` (`sudo yum install make`). 
* Java JDK (not JRE).
* Download `jsvc` src gzipped tarball (at time of writing `commons-daemon-1.2.4-src.tar.gz`) from here.
* Install notes are here. To build the jsvc executable, you need to `cd` into the `commons-daemon-1.2.4-src/src/Native/Unix/` directory and follow notes in the `INSTALL.txt` file.
* The next step requires `$JAVA_HOME` to be known, or better yet set. You can set it with 
```bash
export JAVA_HOME=$(readlink -ze /usr/bin/javac | xargs -0 dirname -z | xargs -0 dirname)
```
* Run `./configure` The `jsvc` executable will be created if all goes well.
* Run sudo ln -s `/home/user/path/to/jsvc` `/usr/bin/jsvc`
* Test with 
```bash
jsvc –help #or other jsvc command.
```

## Generals installation steps

* Create a directory for the MeCard service; something like /home/user/metro which will be referred to as `$METRO_HOME` for the rest of these instructions.
* Download the `Metro_x.xx.xx.tar` ball to the VM.
* Un-tar the `Metro_x.xx.xx.tar` ball in the `$METRO_HOME` directory.
* Edit the *.properties files appropriate to your site, and either copy them to `$METRO_HOME` directory or if you like a cleaner install, create and  move them to a `$METRO_HOME/config` directory. The files in template_config can be your backup.
* Edit the `service.sh` script to suit your environment, noting where the `*.properties` files can be found.
* Edit the `mecard.service` file to suit your environment and follow System Service Installation notes to install and test it.
* Ensure the metro user can use password-less access to the ILS. See Setting up SSH for more information.

## Updating the MeCard service (Linux)

The metro server MeCard.jar can now be run as a service on Linux as follows.

* Backup your existing `${METRO}/dist/MeCard.jar`, and download the latest jar for your version of Java. There is one for Java 8.x and **Java 11.x**. Rename the `MeCard_vN.jar` to `MeCard.jar`.
* Edit the `mecard.service` and `Makefile`.prod file to suit your environment and save the changes over the old `Makefile`.
* Make sure `service.sh` is in the directory you specified as 'WorkingDirectory' in the `mecard.service`.
* Make sure  `service.sh` is executable. You can test by running it on the command line.  /path/to/`service.sh` start and then check for the MeCard as a running process.
* Check for, and stop the metro (MeCard) service using make check and make stop. You may need to kill the process if it is running as root. Using systemctl will prevent this from happening in the future.
* System Service Installation
* Sudo Copy the `mecard.service` file to `/etc/systemd/system`
* If you have an entry in your crontab to restart the service, comment it out.
* Start the service. In Ubuntu use `sudo`, on RedHat `sudo su` to a root shell.
```bash
systemctl daemon-reload
systemctl enable mecard.service
systemctl start mecard
systemctl status mecard
# If systemctl fails to run with too many restarts try:
systemctl reset-failed
# or add 
mecard.service:StartLimitIntervalSec=5 # Secs betwix retries
mecard.service:StartLimitBurst=2 # number of tries 
# within StartLimitIntervalSec.
# back on the cmd line 
systemctl daemon-reload
systemctl start mecard.service
```
 
When the new service is tested and running, remove the commented out `crontab` entry from the above step.

You can still use the `Makefile` included as well as it now contains the handy shortcuts, but requires admin privileges. It contains another rule; status which shows the output of systemd startup and shutdown messages.

For example, if you need to shutdown the server use the following command.
```bash
sudo systemctl stop mecard # or make stop. Both require root
```

## Systemd debugging
* wrong path to script
* script not executable
* no shebang (first line)
* Shell script saved with Windows line endings `\r\n` which looks like `^M` in a text editor like vi(m).
* wrong path in shebang or Windows line ending.
* internal files in your script might be missing access permissions.
* SELinux may be preventing execution of the `ExecStart` parameter; check `/var/log/audit/audit.log` for messages of the form: 
```bash
type=AVC msg=audit([...]): avc:  denied  { execute } or in the output of ausearch -ts recent -m avc -i
```
You have the wrong WorkingDirectory parameter
On RedHat and CENTOS the setroubleshoot explains in plain English why a script or application was blocked from executing. You can also check the `/var/log/audit/audit.log` file. Install with yum install setroubleshoot setools
```bash
sealert -a /var/log/audit/audit.log
#Example: grep httpd /var/log/audit/audit.log | audit2allow -M mypol
# semodule -i mypol.pp
```
See: https://www.serverlab.ca/tutorials/linux/administration-linux/troubleshooting-selinux-centos-red-hat/ for more information on this topic. Feb. 17, 2022.
 
This page also has good information: https://serverfault.com/questions/1032597/selinux-is-preventing-from-execute-access-on-the-file-centos Feb. 17, 2022.
The SELinux restricts binaries that can be used in `ExecStart` to paths that has `system_u:object_r:bin_t:s0` attribute set. Typically those are `/usr/bin` `/usr/sbin` `/usr/libexec` `/usr/local/bin` directories.
You need to move the script into one of this directories or change selinux policy to allow `systemd` to use binaries in the desired location as:
```bash
chcon -R -t bin_t /opt/tomcat/bin/
```

A restorecon will 'unfix' the above better to update the policy e.g.
```bash
semanage fcontext -a -t bin_t "/opt/tomcat/bin(/.*)?" restorecon -r -v /opt/tomcat/bin
```
UPDATE
If java binary is not located in the standard location (custom JVM distribution), then you need to set bin_t label to it as well. For example, it your JVM installed in `/opt/java`, then:
```bash
semanage fcontext -a -t bin_t "/opt/java/bin(/.*)?" restorecon -r -v /opt/java/bin
```
NOTICE: systemd ignores `JAVA_HOME` environment variable if it's not set in unit file.
 
You will have to set permissions for logs and Customers and any 

```bash
[root@azmetro infrastructure]# ls /etc/*release
/etc/os-release /etc/redhat-release /etc/system-release
[root@azmetro infrastructure]# os-release redhat-release system-release
bash: os-release: command not found
[root@azmetro infrastructure]# for i in $(ls /etc/*release); do echo ===$i===; cat $i; done
===/etc/os-release===
NAME="Red Hat Enterprise Linux"
VERSION="8.2 (Ootpa)"
ID="rhel"
ID_LIKE="fedora"
VERSION_ID="8.2"
PLATFORM_ID="platform:el8"
PRETTY_NAME="Red Hat Enterprise Linux 8.2 (Ootpa)"
ANSI_COLOR="0;31"
CPE_NAME="cpe:/o:redhat:enterprise_linux:8.2:GA"
HOME_URL="https://www.redhat.com/"
BUG_REPORT_URL="https://bugzilla.redhat.com/"
 
REDHAT_BUGZILLA_PRODUCT="Red Hat Enterprise Linux 8"
REDHAT_BUGZILLA_PRODUCT_VERSION=8.2
REDHAT_SUPPORT_PRODUCT="Red Hat Enterprise Linux"
REDHAT_SUPPORT_PRODUCT_VERSION="8.2"
===/etc/redhat-release===
Red Hat Enterprise Linux release 8.2 (Ootpa)
===/etc/system-release===
Red Hat Enterprise Linux release 8.2 (Ootpa)
```
 
 
## Setting up SSH
* First log in on the MeCard server as the metro user and generate a pair of authentication keys. Do not enter a passphrase.
 `metro@mecard:~$ ssh-keygen -t rsa`
* Generating public/private rsa key pair.
* Enter file in which to save the key (/home/metro/.ssh/id_rsa):
* Enter passphrase (empty for no passphrase):
* Enter same passphrase again:
* Your identification has been saved in /home/metro/.ssh/id_rsa.
* Your public key has been saved in /home/metro/.ssh/id_rsa.pub.
```bash
The key fingerprint is:
SHA256:7Z/S6vwmiM+1L8pVtGGu+MfUfq3C8J+VgzUxA23tkWg ils@A
The key's randomart image is:
+---[RSA 2048]----+
|             .o o|
|             E.=.|
|            = .=.|
|         . + o  =|
|        S . +. o |
|         o.o. + o|
|       ...=B o oo|
|      .o.=+oO..o+|
|       .=o*X=o+o |
+----[SHA256]-----+
```
Now use ssh to create a directory `~/.ssh` as sirsi on the ILS. (The directory may already exist, which is fine):
```bash
metro@mecard:~$ ssh sirsi@ils mkdir -p .ssh
sirsi@ils's password:
```
Finally append metro's new public key to `sirsi@ils:.ssh/authorized_keys` and enter sirsi's password one last time:
```bash
metro@mecard:~> cat .ssh/id_rsa.pub | ssh sirsi@ils 'cat >> .ssh/authorized_keys'
sirsi@ils's password:
```
From now on you can log into the ILS as sirsi from mecard as metro without password:
```bash
metro@mecard:~$ ssh sirsi@ils
```

**Note**: Depending on your version of SSH you might also have to do the following changes:
Put the public key in .ssh/authorized_keys2 Change the permissions of .ssh to 700 Change the permissions of .ssh/authorized_keys2 to 640
SSH Environment
The user shells are restricted by default. To let the ssh shell know where the ILS API tools can be found, set up a /home/sirsi/.ssh/environment file with the following.
[ -r "/home/sirsi/.bashrc" ] && \. "/home/sirsi/.bashrc"
This reads the sirsi user’s bashrc and sets the variables from that for the SSH user.

Firewall
You may have to open the port on the VM’s firewall like so.
```bash
sudo systemctl stop firewalld
sudo firewall-cmd --add-port=2004/tcp
sudo firewall-cmd --runtime-to-permanent
sudo systemctl restart firewalld
```

# Known Issues
* Google has flagged the `setup.exe` as malware. The development team has asked for a review of the application, but until that time the exe may have to be sent by other means. February 9, 2022.
## Windows
* The `-c c:\path\to\configs` switch doesn't seem to work as expected. I have experimented with several different ways of passing the parameters in prunmgr, but none have panned out. To fix just copy your config properties file into the c:\Metro directory. The MeCard server looks there for properties files by default.
## Ubuntu
* When using Java 11 you may have an issue with jsvc complaining that it can’t find the JVM environment because jsvc can’t find a jvm in `/usr/bin/default-java`. The solution from StackOverflow.   

_I experienced the issue with jsvc version 1.0.6, which is the one you get if you run apt install jsvc on Ubuntu. To check the version installed run:_
```bash
apt list --installed
```
_To fix this issue or use a JVM in an arbitrary location in the file system, replace the `-home=<jvm_dir>` switch with `-XXaltjvm=<jvm_dir>` in the `service.sh` that is referenced in `mecard.service` as detailed below. Versions of `jsvc` after 1.2.0 may not need this fix anymore, but at the time of writing this I have not tested it. (December 17, 2020)_.

* **Update** July 29, 2021 - On systems that are using Open-JDK already, changes to `service.sh` is not  not necessary if you are migrating to Ubuntu 18.04, but will be required when moving to 20.04. To test, do the following.
```bash
/usr/lib/jvm/default-java/bin/java --version
```
If you get the following you don’t need to adjust `service.sh`.
```
openjdk 11.0.11 2021-04-20
OpenJDK Runtime Environment (build 11.0.11+9-Ubuntu-0ubuntu2.18.04)
OpenJDK 64-Bit Server VM (build 11.0.11+9-Ubuntu-0ubuntu2.18.04, mixed mode, sharing)
```
If you get something like the following: 
`-bash: /usr/lib/jvm/default-java/bin/java: No such file or directory`
proceed with the following.
```bash
sudo mkdir /usr/lib/jvm/java-11-openjdk-amd64/lib/amd64
sudo ln -s /usr/lib/jvm/java-11-openjdk-amd64/lib/server /usr/lib/jvm/java-11-openjdk-amd64/lib/amd64/
# The changes in the `service.sh` file are as follows:
# In `service.sh`, change the executable as follows:
JAVA_HOME=/usr/lib/jvm/default-java becomes JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/lib/amd64
$EXEC -home=$JAVA_HOME -XXaltjvm=$JAVA_HOME -cp $CLASS_PATH -user $USER -outfile $LOG_OUT -errfile $LOG_ERR -pidfile $PID $1 $CLASS $ARGS
```

# Repository Information
Source code can be found on [Git](https://github.com/Edmonton-Public-Library/Metro).
You can find updated scripts and configs mentioned in the above instructions in the [Google Drive](https://drive.google.com/drive/folders/0B0kDNQ8_cx7MNElyNzVzRnkzSk0?resourcekey=0-jKgsxeNaxvSonIGtHuTI3A) shared subdirectory under upates/Systemctl folder.

## Dependencies
* commons-cli-1.2.jar
* commons-codec-1.8.jar
* commons-daemon-1.3.1.jar
* gson-2.2.4.jar
* mssql-jdbc-10.2.1.jre11.jar
* mysql-connector-java-5.1.31-bin.jar

# Notes
## Protocol
The protocol for metro is a simple request response message system, where each message is a string of pipe-delimited fields. Once a socket is open to a metro service it will acknowlege with a code <code>XK0|</code> signaling that it is ready for a request, or if there was a socket error on the server you may get a <code>XE0|</code>. The code <code>XX0|</code> terminates the session.
As soon as the client contacts a mecard service it will receive an acknowlege
 "XX0|" = TERMINATE
 "XK0|" = ACKNOWLEDGE
 "XE0|" = ERROR

All fields are separated with a delimiter
 DELIMITER   = "|"
 DEFAULT_FIELD = "X"

### Query Types
Sending receiving requests from Metro requires some knowlege of the communication protocol. The protocol is broken down into two types of activities standard to all network communication: request, or query, and response. Requests are innumerated below:
* "QA0" = GET_STATUS
* "QB0" = GET_CUSTOMER
* "QC0" = CREATE_CUSTOMER
* "QD0" = UPDATE_CUSTOMER
* "QN0" = NULL
NULL is a special command type that has no effect.

### Response Types
* "RA9" = ERROR // Command was received but failed to execute either it was malformed, empty (null), or not supported.
* "RA0" = INIT // all responses start in this state, but once the constructor is finished the state changes to BUSY.
* "RA1" = OK  // occurs if a query was successful but there is nothing to return, as with get status of the ILS.
* "RA2" = BUSY // Initial working state of a query, testing a response during execution would return this value.
* "RA3" = UNAVAILABLE // occurs if the SIP2 server timesout, or the ILS is unavailable.
* "RA4" = SUCCESS // occurs if the query was successful.
* "RA5" = FAIL // occurs if the request failed, ie: the customer's account couldn't be found.
* "RA6" = UNAUTHORIZED // occurs if the client sends the wrong API key, or if the user id and password don't match.
