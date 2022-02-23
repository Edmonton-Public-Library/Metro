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
netstat -an | grep 2004 &> /dev/null
result=$?
if [ $result -gt 0 ]; then
	echo "[`date`] metro is down."
	echo "Metro server has stopped, restarting." | /usr/bin/mailx -s "Metro Server Outage"  $addressees
	/home/ilsdev/metro/service.sh start
fi
