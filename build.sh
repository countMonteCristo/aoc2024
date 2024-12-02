#!/bin/bash

set -o xtrace
set -e

shopt -s globstar

mkdir -p bin
javac -Xlint -Xdiags:verbose -d bin ./src/**/*.java
