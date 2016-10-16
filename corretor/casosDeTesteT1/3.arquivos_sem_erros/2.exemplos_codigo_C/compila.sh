#!/bin/bash
#
# compila todos os programas

for i in *.c; do
	echo $i
	gcc $i -o $i.out
done

