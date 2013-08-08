sREM This bat file stops the metro service.

set SERVICE_NAME=Metro
set PR_INSTALL=C:\metro\windows\prunsrv.exe
 
REM Service log configuration
set PR_LOGPREFIX=metro
set PR_LOGPATH=c:\metro\logs
set PR_STDOUTPUT=%PR_LOGPATH%\stdout.txt
set PR_STDERROR=%PR_LOGPATH%\stderr.txt
set PR_LOGLEVEL=Info
 
REM Path to java installation. Set this if you have more than one Java
REM version installed.
REM set PR_JVM=C:\Program Files (x86)\Java\jdk1.7.0_15\jre\bin\client\jvm.dll
set PR_CLASSPATH=c:\mecard\dist\MeCard.jar
 
REM Startup configuration
set PR_STARTUP=manual
set PR_STARTMODE=jvm
set PR_STARTCLASS=mecard.MetroService
REM set PR_STARTMETHOD=start
 
REM Shutdown configuration
set PR_STOPMODE=jvm
set PR_STOPCLASS=mecard.MetroService
set PR_STOPMETHOD=stop

REM Stop service
C:\metro\prunsrv.exe //SS//%SERVICE_NAME%
