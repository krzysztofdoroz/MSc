#!/bin/bash

ACTIVEMQ_HOME=/home/krzysztof/apache-activemq-5.4.0

ACTIVEMQ_BIN=$ACTIVEMQ_HOME/bin

echo 'starting active mq...'

cd $ACTIVEMQ_BIN/
./activemq start
 
