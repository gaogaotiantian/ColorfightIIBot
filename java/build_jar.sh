#!/bin/sh

set -e

mvn clean install
mv target/ColorfightIIBot-1.0-SNAPSHOT-jar-with-dependencies.jar ColorfightII.jar
mvn clean
