#!/usr/bin/env bash
########################################################################
# Metro check_metro_service.sh
#
# Regularly checks if the service is running, emails admin on restart.
#    Copyright (C) 2013,2014  Edmonton Public Library
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
# This script needs to be modified, but once done, can also be cron'ed
# to continuously check to make sure the server is running.
# For more information please visit wiki.melibraries.ca or mail anisbet@epl.ca 
#
########################################################################
addressees=name@domain.ca
METRO_DIR=/home/anisbet/MeCard
PORT=2004
STATUS_FILE=$METRO_DIR/logs/status.txt
SERVICE=$METRO_DIR/unix/systemctl/service.sh
MAIL=/usr/bin/mailx
NETSTAT=/bin/netstat
TIME_NOW=$(date +'%Y-%m-%d %H:%M:%S')
if [ -x "$NETSTAT" ]; then 
	$NETSTAT -an | grep "$PORT" > $STATUS_FILE
else
	echo "**error, $NETSTAT is not installed correctly!"
	exit 1
fi
if [ ! -s "$STATUS_FILE" ]; then
	echo "[$TIME_NOW] MeCard service is down."
	if [ -x "$MAIL" ]; then
		echo "Reporting that at $TIME_NOW MeCard service was found stopped. Attempting restart." | $MAIL -s "Metro Server Outage [$TIME_NOW]" $addressees
	fi
	if [ -x "$SERVICE" ]; then
		$SERVICE start 
		# rm $STATUS_FILE
		echo "[$TIME_NOW] started."
	else
		echo "**error, can't execute $SERVICE!"
		exit 1
	fi
fi

