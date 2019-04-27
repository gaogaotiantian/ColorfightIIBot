#!/bin/sh

set -e

javac -cp .:ColorfightII.jar example_ai.java
java -cp .:ColorfightII.jar example_ai
