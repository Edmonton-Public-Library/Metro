#!/bin/bash
##############################################################
# Checks for process keeps track of restarts, and warns if the 
# process could not be restarted after 'MAX_RESTART_COUNT' 
# attempts.
# Copyright (c) 2022 Andrew Nisbet 
##############################################################
WORKING_DIR=/home/anisbet/Dev/pswatcher
PROCESS_NAME=test.sh
RESTART_CMD=$WORKING_DIR/$PROCESS_NAME
RESTART_COUNT=0
MAX_RESTART_COUNT=3
LOG_FILE=$WORKING_DIR/pswatcher.log
NOTIFICATIONS=$WORKING_DIR/notified
PROCESS_STATUS_FILE=$WORKING_DIR/process

## Functions
# Logs messages to STDERR and $LOG file.
# param:  Log file name. The file is expected to be a fully qualified path or the output
#         will be directed to a file in the directory the script's running directory.
# param:  Message to put in the file.
# param:  (Optional) name of a operation that called this function.
logit()
{
    local message="$1"
    local time=$(date +"%Y-%m-%d %H:%M:%S")
    echo -e "[$time] $message" >>$LOG_FILE
}

# Keep track of each start or restart attempt.
# Param: message string of helpful diagnostic information.
mark_notified()
{
    local time=$(date +"%Y-%m-%d %H:%M:%S")
    local message="$1"
    echo -e "[$time] $message" >>$NOTIFICATIONS 
}


## Check for working directory
if [ ! -d "$WORKING_DIR" ]; then
    logit "working directory $WORKING_DIR is invalid."
    exit 1
fi 

# Check for process and make sure not to capture the grep process.
# Feel free to change this to some other check.
ps aux | grep $PROCESS_NAME | grep -v grep >$PROCESS_STATUS_FILE

# If there's something in it, good. Remove any notifications file.
if [ -s "$PROCESS_STATUS_FILE" ]; then
    [[ -s "$NOTIFICATIONS" ]] && rm $NOTIFICATIONS
else
    # If the process isn't running check how many attempts have been made.
    if [ -f "$NOTIFICATIONS" ]; then
        RESTART_COUNT=$(wc -l $NOTIFICATIONS | awk '{printf "%d", $0}')
        if [ "$RESTART_COUNT" -le "$MAX_RESTART_COUNT" ]; then
            logit "attempting re-start of $PROCESS_NAME"
            # Re-start it in background
            $RESTART_CMD &
            mark_notified "last attempt to restart $PROCESS_NAME"
        fi
    else
        logit "starting $PROCESS_NAME"
        # Start it in background
        $RESTART_CMD &
        mark_notified "started $PROCESS_NAME"
    fi
fi

# Tidy up for the next scheduled run.
[[ -f "$PROCESS_STATUS_FILE" ]] && rm $PROCESS_STATUS_FILE
