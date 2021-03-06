#!/bin/bash

rm -r sandbox/

mkdir sandbox
mkdir sandbox/data

number_of_computing_nodes=8
dir='node-'

cp computing-node/target/computing-node-0.0.1-SNAPSHOT.jar sandbox/

cp Aggregation-Controller/target/Aggregation-Controller-0.0.1-SNAPSHOT.jar sandbox/

cp /home/krzysztof/MSc/data-source/kghm.data sandbox/data/
cp /home/krzysztof/MSc/data-source/tpsa.data sandbox/data/
cp /home/krzysztof/MSc/data-source/pko.data sandbox/data/
cp /home/krzysztof/MSc/R_scripts/output/kghm_standard_deviation sandbox/data/ 
cp /home/krzysztof/MSc/R_scripts/output/tpsa_standard_deviation sandbox/data/
cp /home/krzysztof/MSc/R_scripts/output/pko_standard_deviation sandbox/data/
cp /home/krzysztof/MSc/R_scripts/output/kghm-pko_correlation_coeff sandbox/data/
cp /home/krzysztof/MSc/R_scripts/output/tpsa-pko_correlation_coeff sandbox/data/
cp /home/krzysztof/MSc/R_scripts/output/kghm-tpsa_correlation_coeff sandbox/data/
cp /home/krzysztof/MSc/R_scripts/output/kghm-wig20_cov sandbox/data/
cp /home/krzysztof/MSc/R_scripts/output/pko-wig20_cov sandbox/data/
cp /home/krzysztof/MSc/R_scripts/output/tpsa-wig20_cov sandbox/data/
cp /home/krzysztof/MSc/R_scripts/output/wig20_var sandbox/data/

cd sandbox/

#$number_of_computing_nodes

for i in $(seq 1 1 $number_of_computing_nodes)
do
	echo 'creating dir: ' $i
	mkdir $dir$i
	cp -R data/ $dir$i
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


