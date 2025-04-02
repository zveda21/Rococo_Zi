#!/bin/bash

chmod +x postgres/script/init-database.sh

docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)

docker volume rm rococo_zi_pgdata

docker compose up rococo-all-db kafka zookeeper -d

# Build all and skip e2e tests until everything is up and running
./gradlew build bootJar -x :rococo-e2e-test:test

echo -e "\e[1;32m"
cat << "EOF"
 _______   ______   .__   __.  _______
|       \ /  __  \  |  \ |  | |   ____|
|  .--.  |  |  |  | |   \|  | |  |__
|  |  |  |  |  |  | |  . `  | |   __|
|  '--'  |  `--'  | |  |\   | |  |____
|_______/ \______/  |__| \__| |_______|

✅ Docker setup completed.
🚀 Setup and build completed successfully! 🚀
➡️ Run the 'local-all.sh' script to start all the services.

EOF
echo -e "\e[0m"
