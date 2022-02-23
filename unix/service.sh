#!/bin/bash

# Setup variables
EXEC=/usr/bin/jsvc
JAVA_HOME=/usr/lib/jvm/java-7-openjdk
CLASS_PATH="/home/ilsdev/metro/dist/lib/commons-daemon-1.0.15.jar":"/home/ilsdev/metro/dist/MeCard.jar"
WDPATH=/home/ilsdev/metro/logs
PID=$WDPATH/metro.pid
CLASS=mecard.MetroService
USER=ilsdev
LOG_OUT=$WDPATH/metro.out
LOG_ERR=$WDPATH/metro.err

# -c tells the service where the config files are located. Valid values are "" or "-c <path>".
#ARGS="-c ../"
ARGS="-c /home/ilsdev/metro/config"

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
