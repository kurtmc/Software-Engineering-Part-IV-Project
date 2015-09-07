#!/bin/bash

NAME=$1

if [ -z $NAME ]; then
	echo "Must provide a name"
	exit 1
fi

CURRENT=$(basename $(pwd))

cd ..

zip -r ${NAME}.zip $CURRENT

echo "Saved to ${NAME}.zip"
