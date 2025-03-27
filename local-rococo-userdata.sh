#!/bin/bash

echo "################ Run rococo-userdata ################"
export SPRING_PROFILES_ACTIVE=local
java -jar rococo-userdata/build/libs/rococo-userdata-1.0.0.jar
