#!/bin/bash

echo "################ Run rococo-gateway ################"
export SPRING_PROFILES_ACTIVE=local
java -jar rococo-userdata/build/libs/rococo-userdata-1.0.0.jar
