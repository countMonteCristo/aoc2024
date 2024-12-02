#!/bin/bash

set -e
set -x

PROJECT_ROOT="src/main/java/aoc2024"
SOURCE_ROOT="${PROJECT_ROOT}/days"
MAIN_RESOURCES_ROOT="src/main/resources"
TEST_RESOURCES_ROOT="src/test/resources"

DAY=$1
DAYXX=$(printf "%02d" "$DAY")
DAY_NAME="Day${DAYXX}"

DAY_FILE="${DAY_NAME}.java"

cp "${SOURCE_ROOT}/DayXX.java" "${SOURCE_ROOT}/${DAY_FILE}"
touch "${MAIN_RESOURCES_ROOT}/day${DAYXX}.txt"
touch "${TEST_RESOURCES_ROOT}/day${DAYXX}_test1.txt"

what="XX"
to="${DAYXX}"
sed -i "s@$what@$to@g" "${SOURCE_ROOT}/${DAY_FILE}"
