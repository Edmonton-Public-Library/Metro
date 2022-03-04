#!/bin/bash
###############################################################################
#
# Starts and stops the MeCard (metro) service. 
# Used by systemctl
# Author: Andrew Nisbet andrew.nisbet@epl.case
# Date:   Mon Oct  5 11:53:02 MDT 2020
#   Copyright (C) 2020  Andrew Nisbet, Edmonton Public Library
# The Edmonton Public Library respectfully acknowledges that we sit on
# Treaty 6 territory, traditional lands of First Nations and Metis people.
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
# MA 02110-1301, USA.
#
###############################################################################
# Setup variables
EXEC_JSVC=/usr/bin/jsvc
MECARD_HOME="/home/its/metro"
# This is meant to fix a Debian bug found in kernal release 
# 4.4.0-81-generic #104-Ubuntu SMP Wed Jun 14 08:17:06 UTC 2017 
# that causes jsvc to fail on startup.
export JSVC_EXTRA_OPTS="$JSVC_EXTRA_OPTS -Xss1280k"
export JAVA_HOME=$(readlink -ze /usr/bin/javac | xargs -0 dirname -z | xargs -0 dirname)
CLASS_PATH="$MECARD_HOME/dist/lib/commons-daemon-1.0.15.jar":"$MECARD_HOME/dist/MeCard.jar"
WDPATH=$MECARD_HOME/logs
PID=$WDPATH/metro.pid
CLASS=mecard.MetroService
USER=its
LOG_OUT=$WDPATH/metro.out
LOG_ERR=$WDPATH/metro.err
## Server arguments.
# -c tells the service where the config files are located. 
# Valid values are "" or "-c <path>" where path can be absolute or relative
# to the MeCard.jar. Example: ARGS="-c ../"
ARGS="-c $MECARD_HOME/config"

# Sends a command to the daemon process.
# param:  String of 'start', 'stop', 'restart'
# return: none.
do_exec()
{
    $EXEC_JSVC -home "$JAVA_HOME" -cp $CLASS_PATH -user $USER -outfile $LOG_OUT -errfile $LOG_ERR -pidfile $PID $1 $CLASS $ARGS
}

case "$1" in
    start)
        do_exec
            ;;
    stop)
        do_exec "-stop"
        if [ -f $PID ]; then
            rm $PID
        fi
    ;;
    restart)
        if [ -f "$PID" ]; then
            do_exec "-stop"
            if [ -f $PID ]; then
                rm $PID
            fi
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
