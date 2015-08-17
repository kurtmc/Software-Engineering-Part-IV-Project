#!/bin/bash

git checkout incomplete

mkdir exportedSimpleTest

cp -r .classpath .project SimpleTest.eml SimpleTest.userlibraries src exportedSimpleTest/

git checkout master
