#!/bin/bash

# Make sure the wait script is executable
chmod +x wait-for-service.sh

# Shut down any previous containers, remove volumes & orphans
docker-compose down -v --remove-orphans

# Build and start all containers
docker-compose up --build
