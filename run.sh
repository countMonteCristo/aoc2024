#!/bin/bash

# set -o xtrace
set -e

shopt -s globstar

./build.sh

echo ""

java -cp bin aoc2024.Runner "$@"
