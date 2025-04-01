#!/bin/bash

chmod +x postgres/script/init-database.sh

docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)

docker volume rm rococo_zi_pgdata

docker compose up -d

# Build all and skip e2e tests until everything is up and running
./gradlew build bootJar -x :rococo-e2e-test:test
