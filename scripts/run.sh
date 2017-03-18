#!/usr/bin/env bash
$IGNITE_HOME/bin/ignite.sh $IGNITE_HOME/examples/config/example-ignite.xml &
java -jar /opt/test-ignite.jar
