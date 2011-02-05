#/bin/bash

echo $1
echo $2

echo $#

cut -d , -f 5 $1 > $2


