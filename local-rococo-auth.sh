#!/bin/bash

echo "################ Run rococo-auth ################"
export SPRING_PROFILES_ACTIVE=local
java -jar rococo-auth/build/libs/rococo-auth-1.0.0.jar
