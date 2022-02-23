#!/usr/bin/env bash
##################################################################
#
# What:   Checks for Failed users.
# Why:    Report if customers have failed. Then you can go fix the account.
# How:    Check for a 'touch' file, if none, touch one. If file
#         check the last mod time of the file. Find all files in 
#         Customers with mod times less than the touch file.
#
##################################################################

CUSTOMER_DIR=/home/ilsdev/metro/logs
ADDRESSES="anisbet@epl.ca"
SUBJECT="Failed Me Card customer report."
# We cd into the directory and look for this file later.
TOUCH_FILE=./._fail_users_.txt
LAST_RUN=0
LAST_RUN_DATE=""

# Test for the customer directory and cd there.
if [ -e $CUSTOMER_DIR ]
then
	cd $CUSTOMER_DIR
else
	echo "**error: invalid configuration. $CUSTOMER_DIR doesn't exist."
	exit 1
fi

# If we have run before we left a touch file here. The last modified 
# time stamp is used to determine when we last ran, so we can look for
# newer files.
if [ -e $TOUCH_FILE ]
then
	LAST_RUN=`stat -c %Y $TOUCH_FILE`
	LAST_RUN_DATE=`stat -c %y $TOUCH_FILE`
else
	echo "no $TOUCH_FILE found, will check again next time."
	# Touch the file so the next time it runs we can compare which files were added after we run now.
	touch $TOUCH_FILE
	exit 1;
fi

if ls *.fail >/dev/null
then
	declare -a customerFiles=(`ls *.fail`)
	failedCustomerCount=0

	## now loop through the above array
	for file in "${customerFiles[@]}"
	do
		myFileTime=`stat -c %Y $file`
		if [ "$LAST_RUN" -lt "$myFileTime" ]
		then
			# echo "Found a younger file: $file"
			failedCustomerCount=$[$failedCustomerCount +1]
		fi
	done

	if [ $failedCustomerCount -gt 0 ]
	then
		echo "$failedCustomerCount customer(s) failed. Last check: $LAST_RUN_DATE." 
		echo "$failedCustomerCount customer(s) failed. Last check: $LAST_RUN_DATE." | mailx -s "$SUBJECT" "$ADDRESSES"
	fi
fi # No failed customers found.
# Touch the file so the next time it runs we can compare which files were added after we run now.
touch $TOUCH_FILE