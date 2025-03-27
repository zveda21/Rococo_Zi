#!/bin/bash

echo "################ Run rococo-gateway ################"
export SPRING_PROFILES_ACTIVE=local
java -jar rococo-gateway/build/libs/rococo-gateway-1.0.0.jar

