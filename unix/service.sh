#!/usr/bin/bash

# Setup variables
EXEC=/usr/local/apache-tomcat/commons-daemon/bin/jsvc
JAVA_HOME=/usr/local/apache-tomcat/jdk1.7.0_25
CLASS_PATH="/usr/local/apache-tomat/commons-daemon/lib/commons-daemon-1.0.15.jar":"/s/sirsi/mecard/dist/MeCard.jar"
#PID=/var/run/metro.pid
PID=/tmp/metro.pid
CLASS=mecard.MetroService
USER=sirsi
PID=/tmp/metro.pid
LOG_OUT=/tmp/metro.out
LOG_ERR=/tmp/metro.err

# -c tells the service where the config files are located. Valid values are "" or "-c <path>".
#ARGS="-c ../"
ARGS=""

do_exec()
{
    $EXEC -home "$JAVA_HOME" -cp $CLASS_PATH -user $USER -outfile $LOG_OUT -errfile $LOG_ERR -pidfile $PID $1 $CLASS $ARGS
}

case "$1" in
    start)
        do_exec
            ;;
    stop)
        do_exec "-stop"
            ;;
    restart)
        if [ -f "$PID" ]; then
            do_exec "-stop"
            do_exec
        else
            echo "service not running, will do nothing"
            exit 1
        fi
            ;;
    *)
        echo "usage: $0 {start|stop|restart}" >&2
        exit 3
        ;;
esac
