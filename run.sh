#!/bin/bash

# set -o xtrace
set -e

shopt -s globstar

./build.sh
echo ""
echo "============================================"
java -cp build aoc2024.Runner "$@"
echo "============================================"
