########################################################################
# Metro Makefile
#
# This make file automates common metro server functions.
#    Copyright (C) 2013  Edmonton Public Library
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
########################################################################

HOME=/home/ilsdev/metro
LOG=${HOME}/logs
SERVICE=${HOME}/service.sh
start:
	${SERVICE} start
stop:
	${SERVICE} stop
clean:
	-rm ${LOG}/metro.out
	-rm ${LOG}/metro.err
check:
	####################
	-cat ${LOG}/metro.out
	####################
	-cat ${LOG}/metro.err
	####################
	ps aux | grep metro
	-netstat -an | grep 2004
outage:
	cp ./environment.properties ./environment.properties.original
	cp ./environment.properties.outage ./environment.properties
restore:
	cp ./environment.properties.original ./environment.properties