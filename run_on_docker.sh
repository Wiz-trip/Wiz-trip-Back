#!/bin/bash

chmod +x ./gradlew && \
./gradlew clean build -x test && \
docker-compose down

# 삭제할 Docker 이미지 이름
IMAGE_NAME="wiz-trip-backend"

# 이미지가 존재하는지 확인
if docker images -q "$IMAGE_NAME" | grep -q .; then
    echo "$IMAGE_NAME 이미지가 존재합니다. 삭제 중..."
    docker rmi "$IMAGE_NAME"
    echo "$IMAGE_NAME 이미지가 삭제되었습니다."
else
    echo "$IMAGE_NAME 이미지가 존재하지 않습니다."
fi

docker build -t wiz-trip-backend . && \
docker-compose up -d