#!/bin/bash

echo "################ Run rococo-artist ################"
export SPRING_PROFILES_ACTIVE=local
java -jar rococo-artist/build/libs/rococo-artist-1.0.0.jar
