#!/bin/sh

set -e

javac -cp .:ColorfightIIBot-1.0-SNAPSHOT-jar-with-dependencies.jar example_ai.java
java -cp .:ColorfightIIBot-1.0-SNAPSHOT-jar-with-dependencies.jar example_ai
