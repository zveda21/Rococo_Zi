#!/bin/bash

chmod +x postgres/script/init-database.sh

docker compose down
docker compose up -d

gradle build bootJar

echo "################ Setup completed ################"
echo "# Run three commands in separate terminals"
echo "# ./local-rococo-artist.sh"
echo "# ./local-rococo-gateway.sh"
echo "# ./local-rococo-userdata.sh"