#!/bin/sh

dockerd-entrypoint.sh &
sleep 10
./compose-elastic/start.sh
./compose-postgres/start.sh
./compose-kafka/start.sh
./compose-vault/start.sh
java -jar app.jar