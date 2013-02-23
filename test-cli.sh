#!/bin/bash
#
# Simple script to test Java-based execution of Spoon. You must have assembled
# the jar prior to running this script (i.e., mvn clean verify).
mvn clean verify -DskipTests=true

set -e

APK=`\ls app/target/*.apk`
TEST_APK=`\ls integration-tests/target/*.apk`

java -jar spoon-*-jar-with-dependencies.jar --apk "$APK" --test-apk "$TEST_APK" --output target

open target/index.html
