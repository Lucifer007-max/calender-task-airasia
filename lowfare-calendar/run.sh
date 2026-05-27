#!/bin/bash

echo "Stopping containers..."
docker compose down

echo "Removing target folder..."
rm -rf target

echo "Building project..."
./mvnw clean package -DskipTests

echo "Building Docker images..."
docker compose build --no-cache

echo "Starting containers..."
docker compose up

echo "Done ✅"