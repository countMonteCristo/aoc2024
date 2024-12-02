#!/bin/bash

set -o xtrace
set -e

shopt -s globstar

javac -Xdiags:verbose -d build ./src/**/*.java
