#!/bin/sh

dockerd-entrypoint.sh &
sleep 10
./compose-elastic/start.sh
./compose-postgres/start.sh
java -jar app.jar