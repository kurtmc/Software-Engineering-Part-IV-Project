#!/bin/bash

git checkout incomplete

mkdir exportedSimpleTest

cp -r save_test.sh .classpath .project SimpleTest.eml SimpleTest.userlibraries src test exportedSimpleTest/

mv exportedSimpleTest ../

git checkout master
