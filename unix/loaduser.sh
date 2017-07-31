#!/bin/bash
########################################################################
#
# Importer for systems that use SSH for loading customers.
#    Copyright (C) 2017  Andrew Nisbet
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
# Author:  Andrew Nisbet, Edmonton Public Library
# Rev:
#          0.0 - Dev.
#
########################################################################
# SSH_SERVER="sirsi@10.110.2.4"  # ILS within the Shortgrass network.
SSH_SERVER="sirsi@edpl-t.library.ualberta.ca"  # Test system at EPL.
USER="ADMIN|PCGUI-DISP"
LIBRARY="EPLMNA"
TMP_DIR="/tmp"

# Tests if a user exists in the ILS.
# param:  flat file of user information. File must exist prior to calling.
# return: 1 if the user has an account with that user id, and 0 if they don't.
user_not_exist()
{
	local flat="$1"
	local user_id=$(grep USER_ID "$flat" | awk 'NF>1{print $NF}' | sed -e 's/^..//')
	# If we failed to grab the user id, then return 1, which means they exist, the
	# caller will likely try to update the account which will fail if it doesn't exist
	# which in this case is what we want.
	if [ -z "$user_id" ]; then
		printf "** error couldn't read the user's id from the file '%s'\n" "$flat" >&2
		return 2
	fi
	local tmp_file="$TMP_DIR/tmp.$$"
	# sshpass -p"YOUR PASSWORD" ssh -t -t "$SSH_SERVER" << EOSSH >$tmp_file
	# export LD_LIBRARY_PATH=:/s/sirsi/Unicorn/Oracle_client/10.2.0.3:/s/sirsi/Unicorn/Oracle_client/10.2.0.3:/usr/local/sirsi/lib64/:/s/sirsi/Unicorn/Oracle_client/10.2.0.2
	ssh -t -t "$SSH_SERVER" << EOSSH >$tmp_file
echo "$user_id" | seluser -iB
exit
EOSSH
	grep "error number 111" "$tmp_file" 2>&1 > /dev/null
	local status=$?
	rm "$tmp_file"
	echo $status
}

# Loads customer data from file and executes it via SSH adding variables as
# required.
# param:  flat file of customer data.
# return: 0 if everything went well and 1 otherwise.
load_customer()
{
	if [ -z "$1" ]; then
		printf "*** error empty file name argument.\n" >&2
		return
	fi
	# TODO: Add check if this is a load or an update.
	local user_test=$(user_not_exist "$1")
	# API to use, default is to update. If there's a problem we just output file name.
	# Will return the number of lines, characters and words in the flat on error.
	local API="wc" 
	if [ -z "$user_test" ]; then
		printf "** error, caller didn't return. Is the process hung?\n" >&2
		return 1
	elif [ "$user_test" -eq 2 ]; then
		printf "** error, failed to parse input file, is it a flat file?\n" >&2
		return 1
	elif [ "$user_test" -eq 1 ]; then
		printf "user exists marking for update.\n" >&2
		# Create the user.
		API="loadflatuser -aR -bR -mu -n -y$LIBRARY"
	elif [ "$user_test" -eq 0 ]; then
		printf "loading user.\n" >&2
		# Update customer
		API="loadflatuser -aU -bU -mc -n -y$LIBRARY"
	else
		printf "** error, can't determine if this is an update or create.\n" >&2
		mv "$1" "$1.fail" # Save the file as a fail file for admin to check and load later.
		return 1
	fi
	# Add a trailing '\' to each line - except the last - in this way we can echo all the flat data from one variable.
	local customer=$(cat "$1" | sed -e '$ ! s/$/\\/')
	# sshpass -p"YOUR PASSWORD" ssh -t -t "$SSH_SERVER" << EOSSH >$tmp_file
	# export LD_LIBRARY_PATH=:/s/sirsi/Unicorn/Oracle_client/10.2.0.3:/s/sirsi/Unicorn/Oracle_client/10.2.0.3:/usr/local/sirsi/lib64/:/s/sirsi/Unicorn/Oracle_client/10.2.0.2
	ssh -t -t "$SSH_SERVER" << EOSSH
echo "$customer" | $API -l"$USER"
exit
EOSSH
}  # The output is interpreted by the caller (ME server).


FLAT_FILE=$1
if [ -s "$1" ]; then
	result=$(load_customer "$FLAT_FILE")
	if [ -z "$result" ]; then
		printf "*** error occured while loading customer account.\n" >&2
	else
		printf "%s\n" "$result"
	fi
else
	printf "*** error argument entered is either empty, not a file, or the file was not found.\n" >&2
fi
# EOF
