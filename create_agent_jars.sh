#!/bin/bash

cd data-source/
mvn clean install

cd ../generic-computing-node/
mvn clean install

cd ../GenericGeneticAlgorithm/

mvn clean install 

cd ../Aggregation-Controller/

mvn clean install


cd ../node-agent

mvn clean install

