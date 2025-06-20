#!/bin/bash
source ./docker.properties
export COMPOSE_PROFILES=test
export PROFILE=docker
export PREFIX="${IMAGE_PREFIX}"

export ALLURE_DOCKER_API=http://allure:5050/
export HEAD_COMMIT_MESSAGE="local build"
export ARCH=$(uname -m)

# Default values
BROWSER="chrome"      # Default browser is Chrome
export BROWSER_TYPE=$BROWSER

echo '### Java version ###'
java --version

docker compose down
docker_containers=$(docker ps -a -q)
docker_images=$(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'rococo')

if [ ! -z "$docker_containers" ]; then
  echo "### Stop containers: $docker_containers ###"
  docker stop $docker_containers
  docker rm $docker_containers
fi

if [ ! -z "$docker_images" ]; then
  echo "### Remove images: $docker_images ###"
  docker rmi $docker_images
fi

echo '### Java version ###'
java --version
bash ./gradlew clean
bash ./gradlew jibDockerBuild -x :rococo-e2e-tests:test

docker pull selenoid/vnc_chrome:127.0
docker compose up -d
docker ps

echo ""
echo "To attach to rococo-e2e container run:"
echo "docker attach rococo-e2e"
echo ""
echo ""
echo ""
echo ""

