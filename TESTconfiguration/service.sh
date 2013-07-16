#!/bin/bash

# Setup variables
EXEC=/usr/bin/jsvc
JAVA_HOME=/usr/lib/jvm/java-7-openjdk-i386
CLASS_PATH="/home/metro/Dropbox/development/MeCard/commons-daemon-1.0.15.jar":"/home/metro/Dropbox/development/MeCard/dist/MeCard.jar"
CLASS=mecard.MetroService
# -c tells the service where the config files are located. Valid values are "" or "-c <path>".
#ARGS="-c ../"
ARGS=""
USER=metro
PID=/tmp/metro.pid
LOG_OUT=/tmp/metro.out
LOG_ERR=/tmp/metro.err

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
