#!/bin/bash

# Start SonarQube
docker-compose -f ./sonarqube/docker-compose.yml up -d

# Generate SR_VERSION
export SR_VERSION="$(date +%s)_$(git rev-parse --abbrev-ref HEAD)"
echo $SR_VERSION

# Build the project
mvn clean install

# Run SonarQube analysis
mvn clean verify sonar:sonar \
  -Dsonar.login=sqa_5632e7f7632bfd140f3c0789b7a1da365ee5b6f7
