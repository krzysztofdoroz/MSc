#/bin/bash

name='SNAPSHOT'

for i in `ps aux | grep $name | awk '{print $2}'`
do
  echo 'killing process wit PID:'$i
  kill -9 $i
done


