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
#          0.3 - Create tested, now to add update.
#          0.2 - Dev.
#
########################################################################
### Site specific values
SSH_SERVER="sirsi@edpl-t.library.ualberta.ca"  # ILS site specific
# This value is used to compile the customer data into a form consumable by the 'here-doc'.
ILS="SOLARIS"                  # Type of unix the ILS is running, site specific, see below.
LIBRARY="EPLMNA"               # Default library for customer, site specific
# The error log can go anywhere and be named anything, but this makes sense.
ERROR="/home/ils/metro/logs/loaduser.log"
# The password file can be named anything and can be located anywhere
# but it should only include the password and no other lines or characters.
PASSWORD_FILE="/home/ils/metro/config/loaduser_ssh.txt"
###
# This script is able to load customers on the ILS even if the ILS doesn't 
# have a public SSH key for this server. You can also specify special variables
# to be populated after login by setting them after the ssh and sshpass commands.
# Example: 
# export LD_LIBRARY_PATH=:/s/sirsi/Unicorn/Oracle_client/10.2.0.3:/s/sirsi/Unicorn/Oracle_client/10.2.0.3:/usr/local/sirsi/lib64/:/s/sirsi/Unicorn/Oracle_client/10.2.0.2
# Any variable that needs to be set can be defined this way.
# The ME Server calls the create with "CREATE" or "UPDATE", but can be extended.
# See: mecard.requestbuilder.SymphonyRequestBuilder for definition.
API_SWITCHES="-aU -bU -mc"     # Create user.
USER="ADMIN|PCGUI-DISP"        # Load user for Symphony logging, site specific
DATE=$(date +%Y-%m-%d_%_H:%M:%S)
echo "=== $DATE" >>$ERROR
PASSWORD=$(cat "$PASSWORD_FILE" 2>>$ERROR)

# Loads customer data from file and executes it via SSH adding variables as
# required.
# param:  flat file of customer data.
# return: 0 if everything went well and 1 otherwise.
load_customer()
{
        if [ -z "$1" ]; then
                echo "*** error empty argument." >>$ERROR
                return
        fi

        # Save customer ID for error log.
        local user_id=$(grep USER_ID "$1" | awk 'NF>1{print $NF}' | sed -e 's/^..//')
        echo "user_id: $user_id" >>$ERROR
        # API to use, default is to update if exists and create if not.
        # Add a trailing '\' to each line - except the last - in this
        # way we can echo all the flat data from one variable.
        if [ "$ILS" == "REDHAT" ]; then
                local customer=$(cat "$1" | sed -e '$ ! s/$/\n\\/')    # Required at Shortgrass.
        elif [ "$ILS" == "SOLARIS" ]; then
                local customer=$(cat "$1" | sed -e '$ ! s/$/\\/')      # Ubuntu at EPL.
        else
                local customer=$(cat "$1" | sed -e '$ ! s/$/\n\\/')    # Default to modern unix.
        fi
        # If there is no password then we assume the ILS has a public key for this machine.
        if [ -z "$PASSWORD" ]; then
                ssh -t "$SSH_SERVER" << EOSSH 2>>$ERROR
echo "$customer" | loadflatuser $API_SWITCHES -n -y$LIBRARY -l"$USER"
exit
EOSSH
        else # We use this to authenticate automatically without typing the password.
                sshpass -p"$PASSWORD" ssh -t "$SSH_SERVER" << EOSSH 2>>$ERROR
echo "$customer" | loadflatuser $API_SWITCHES -n -y$LIBRARY -l"$USER"
exit
EOSSH
        fi
}
if [ -z "$2" ]; then
        echo "*WARN expected argument 'CREATE' or 'UPDATE', but got none. Defaulting to $API_SWITCHES" >>$ERROR
else
        case "$2" in
        UPDATE)
                echo "UPDATE selected.\n" >>$ERROR
                API_SWITCHES="-aR -bR -mu"
                ;;
        CREATE)
                echo "CREATE selected.\n" >>$ERROR
                API_SWITCHES="-aU -bU -mc"
                ;;
	*)
		echo "*WARN expected argument 'CREATE' or 'UPDATE', but got '$2'. Defaulting to $API_SWITCHES" >>$ERROR
                API_SWITCHES="-aR -bR -mu"
		;;
        esac
fi
if [ -s "$1" ]; then
        result=$(load_customer "$1")
        if [ -z "$result" ]; then
                echo "*** error occurred while creating or updating customer account."  >>$ERROR
        else
                echo "user_key: $result successfully loaded." >>$ERROR
        fi
else
        echo "*** error argument entered is either empty, not a file, or the file was not found." >>$ERROR
fi
# EOF

