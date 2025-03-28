#!/bin/bash

chmod +x postgres/script/init-database.sh

docker volume rm rococo_zi_pgdata
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)
docker compose up -d

gradle build bootJar

echo "################ Setup completed ################"
echo "# Run each command in separate terminal"
echo "# ./local-rococo-artist.sh"
echo "# ./local-rococo-museum.sh"
echo "# ./local-rococo-gateway.sh"
echo "# ./local-rococo-userdata.sh"