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
  -Dsonar.login=sqa_b4b04c081f2a5d6f3357c5e97b5ccba94fd82847
