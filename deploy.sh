#!/bin/bash

rm -r sandbox/

mkdir sandbox

number_of_computing_nodes=2
dir='node-'

cp computing-node/target/computing-node-0.0.1-SNAPSHOT.jar sandbox/

cp Aggregation-Controller/target/Aggregation-Controller-0.0.1-SNAPSHOT.jar sandbox/

cd sandbox/

#$number_of_computing_nodes

for i in $(seq 1 1 $number_of_computing_nodes)
do
	echo 'creating dir: ' $i
	mkdir $dir$i
	cp computing-node-0.0.1-SNAPSHOT.jar $dir$i/ 
done
 
java -jar Aggregation-Controller-0.0.1-SNAPSHOT.jar &

for i in $(seq 1 1 $number_of_computing_nodes)
do
	echo 'firing up: ' $i
	cd $dir$i
	java -jar computing-node-0.0.1-SNAPSHOT.jar & 
	cd ..
done


