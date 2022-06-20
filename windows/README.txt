# Installing Procrun (prunsrv.exe and prunmgr.exe)
The prunsrv.exe is from commons-daemon-1.1.0-bin-windows.zip /amd64 (REF: http://www.apache.org/dist/commons/daemon/binaries/windows/ and StackOverflow at: https://stackoverflow.com/questions/15543053/prunsrv-exe-service-not-starting-up)

## Installation Steps
The following steps were used at TRAC to install a new MeCard server under Windows server 2019.

1) Have a local user account available on the Windows machine with admin privileges.
2) Download the (Open)JDK 11 (or JRE) from https://jdk.java.net/java-se-ri/11. You may also download from Oracle, but you may incur licence fees.
3) Unpack the zip file in the c:\metro\java directory.
4) Add a %JAVA_HOME%=c:\metro\java to the system environment variables and set it to c:\Metro\java.
5) Add %JAVA_HOME%\bin to the %PATH% system environment variable.
6) Test by opening a command prompt and typing java - -version.
7) The MeCard server runs as a daemon (in Unix). The equivalent in Windows is a service which uses Apache’s Procrun server which you can download here:  https://commons.apache.org/proper/commons-daemon/index.html. Information can be found here: https://commons.apache.org/proper/commons-daemon/procrun.html. The current version is commons-daemon-1.3.1-bin-windows.zip. Each version will contain a commons-daemon-x.x.x.jar which must be added to the build in the (NetBeans) IDE to match the version of the prunmgr.exe and prunsrv.exe.
8) Download and unpack the latest mecard_Windows.zip file which will include the MeCard.jar, lib files, and 64-bit prunsrv.exe (AKA procrun) and prunmgr.exe files.
9) Update your config files, and move them to c:\Metro directory (until I can figure out how to pass a switch and string as parameters).
10)From a powershell ISE window opened in admin mode, with Set-ExecutionPolicy -ExecutionPolicy Unrestricted -Scope CurrentUser.
11) Run from PowerShell (ISE): c:\metro\windows\prunsrv.exe //IS//Metro "--Description=MeCard --Jvm=auto --Startup=auto --StartMode=Java --Classpath='c:\metro\dist\MeCard.jar' --StartClass=mecard.MetroService --StartMethod=start --StopMode=Java --StopClass=mecard.MetroService --StopMethod=stop --LogPath='c:\metro\logs' --LogLevel=Info --LogPrefix=metro --StdOutput='c:\metro\logs\Metro-stdout.txt' --StdError='c:\metro\logs\Metro-stderr.txt'"

That should install the service, but it may not run from the get-go, so to tweak the parameters use the prunmgr with: c:\Metro\windows\prunmgr.exe //ES//Metro

# System Event Logs
There are four places to look for information on issues you may be having. 
```PowerShell
System event logs
c:\Metro\logs\metro-stdout.txt
c:\Metro\logs\metro-stderr.txt
c:\Metro\logs\commons-daemon.YYYY-MM-DD.log
```

To check the settings and trouble shoot, run:
`c:\metro\windows\prunmgr.exe //ES//Metro`
and set the configuration manually from the prunmgr.exe.

# Issues
* The '-c c:\path\to\configs' switch doesn't seem to work as expected. I have experimented with several different ways of passing the parameters in prunmgr, but none have panned out. To fix just copy your config properties file into the c:\Metro directory. The MeCard server looks there for properties files by default.

Andrew June 17, 2022

