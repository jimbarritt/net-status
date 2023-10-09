#!/bin/sh
set -e
SERVICE_NAME=net-status

PID_PATH=./target

RUN_AS=$(whoami)
PATH_TO_JAR=./target/net-status-0.1.0-SNAPSHOT-standalone.jar
PID_PATH_NAME=${PID_PATH}/${SERVICE_NAME}.pid
LOG_DIR=./target/logs


mkdir -p ${LOG_DIR}

LOG_OUT=${LOG_DIR}/net-status.system.log
LOG_ERR=${LOG_DIR}/net-status.error.log
LOG_FILE=${LOG_DIR}/net-status.log

case $1 in
    start)
        echo "Starting $SERVICE_NAME ..."
        if [ ! -d ${LOG_DIR} ]; then
	    mkdir ${LOG_DIR}
        fi
        
        if [ ! -f $PID_PATH_NAME ]; then
	    nohup java -cp ${PATH_TO_JAR} net_status.core -l ${LOG_FILE} >> ${LOG_OUT} 2>> ${LOG_ERR} < /dev/null &
            echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started."
        else
            echo "$SERVICE_NAME is already running."
        fi
        ;;
    stop)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stopping ..."
            kill $PID;
            echo "$SERVICE_NAME stopped."
            rm $PID_PATH_NAME
        else
            echo "$SERVICE_NAME is not running."
        fi
        ;;
    restart)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stopping ...";
            kill $PID;
            echo "$SERVICE_NAME stopped.";
            rm $PID_PATH_NAME
            echo "$SERVICE_NAME starting."
            nohup java -jar $PATH_TO_JAR /tmp 2>> /dev/null >> /dev/null &
            echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started."
        else
            echo "$SERVICE_NAME is not running."
        fi
        ;;
    status)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME)
            ps aux | grep net-status | grep -v grep
        else
            echo "No server running"
        fi
        ;;
    *)
        echo "\nYou have not provided a command, please do so\n"
esac
