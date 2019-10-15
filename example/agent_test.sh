#!/bin/bash
# for loop will go to each of the dr machines and check the uptime

for i in dr00 dr01 dr02 dr03 dr04 dr05 dr06 dr07 dr08 dr09 
do
	echo -------- ${i}
	ssh ${i} uptime
done
