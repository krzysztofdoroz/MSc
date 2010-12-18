#!bin/bash

#cd Aggregation-Controller/

#mvn clean compile exec:java &

#cd ..

#cd computing-node

#mvn clean compile exec:java &




java -jar Aggregation-Controller/target/Aggregation-Controller-0.0.1-SNAPSHOT.jar &

java -jar computing-node/target/computing-node-0.0.1-SNAPSHOT.jar &

