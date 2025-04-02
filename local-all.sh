#!/bin/bash

trap "echo 'Stopping all services...'; pkill -P $$; exit 0" SIGINT SIGTERM EXIT

# Build all and skip tests until everything is up and running
./gradlew build bootJar -x test

echo "################ Run everything ################"
export SPRING_PROFILES_ACTIVE=local

java -jar rococo-auth/build/libs/rococo-auth-1.0.0.jar &
java -jar rococo-userdata/build/libs/rococo-userdata-1.0.0.jar &
java -jar rococo-painting/build/libs/rococo-painting-1.0.0.jar &
java -jar rococo-artist/build/libs/rococo-artist-1.0.0.jar &
java -jar rococo-museum/build/libs/rococo-museum-1.0.0.jar &
java -jar rococo-painting/build/libs/rococo-painting-1.0.0.jar &
java -jar rococo-gateway/build/libs/rococo-gateway-1.0.0.jar &

wait