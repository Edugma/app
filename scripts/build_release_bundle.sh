#!/bin/bash

# Determine the directory where the script is located
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Set the path to the .env file relative to the script location
ENV_FILE="$SCRIPT_DIR/../.env"

# Check if the .env file exists
if [ ! -f "$ENV_FILE" ]; then
  echo ".env file not found at $ENV_FILE! Exiting..."
  exit 1
fi

# If the file is found, continue with loading the environment variables
export $(grep -v '^#' "$ENV_FILE" | xargs)

# Run the Gradle task with the environment variables
./gradlew :android:app:bundleRelease -Pbuildkonfig.flavor=release
