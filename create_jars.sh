#!/bin/bash

cd data-source/
mvn clean install

cd ../GenericGeneticAlgorithm/

mvn clean install 

cd ../MPTGeneticAlgorithm/

mvn clean install

cd ../Aggregation-Controller/

mvn clean install


cd ../computing-node

mvn clean install

