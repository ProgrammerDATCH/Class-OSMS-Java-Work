#!/bin/bash

# Stop script on first error
set -e

# Configuration
if [ -z "$CATALINA_HOME" ]; then
    if [ -d "/usr/local/opt/tomcat/libexec" ]; then
        export CATALINA_HOME="/usr/local/opt/tomcat/libexec"
    elif [ -d "/usr/local/Cellar/tomcat/11.0.6/libexec" ]; then
        export CATALINA_HOME="/usr/local/Cellar/tomcat/11.0.6/libexec"
    else
        echo "Error: Cannot find Tomcat installation"
        echo "Please install Tomcat using: brew install tomcat"
        echo "Or set CATALINA_HOME environment variable manually"
        exit 1
    fi
fi

# Add Tomcat's bin directory to PATH if not already there
export PATH="$CATALINA_HOME/bin:$PATH"

APP_NAME="osms"
VERSION="1.0-SNAPSHOT"
WAR_FILE="target/${APP_NAME}-${VERSION}.war"
DEPLOY_PATH="$CATALINA_HOME/webapps"

echo "=== Environment Setup ==="
echo "CATALINA_HOME: $CATALINA_HOME"
echo "Java version:"
java -version
echo "Maven version:"
mvn --version | head -n 1

echo "=== Building OSMS Application ==="
# Clean and package the application
mvn clean package

echo "=== Deploying to Tomcat ==="
# Check if Tomcat directory exists
if [ ! -d "$CATALINA_HOME" ]; then
    echo "Error: Tomcat directory not found at $CATALINA_HOME"
    echo "Please check your Tomcat installation"
    exit 1
fi

# Create webapps directory if it doesn't exist
if [ ! -d "$DEPLOY_PATH" ]; then
    echo "Creating webapps directory..."
    mkdir -p "$DEPLOY_PATH"
fi

# Stop Tomcat if it's running
if [ -f "$CATALINA_HOME/bin/shutdown.sh" ]; then
    echo "Stopping Tomcat..."
    "$CATALINA_HOME/bin/shutdown.sh" || true
    sleep 5
fi

# Remove old deployment
echo "Removing old deployment..."
rm -rf "$DEPLOY_PATH/$APP_NAME"
rm -f "$DEPLOY_PATH/$APP_NAME.war"
rm -rf "$DEPLOY_PATH/ROOT"
rm -f "$DEPLOY_PATH/ROOT.war"

# Copy new WAR file as ROOT.war to make it the default application
echo "Copying new WAR file..."
cp "$WAR_FILE" "$DEPLOY_PATH/ROOT.war"

# Start Tomcat
echo "Starting Tomcat..."
"$CATALINA_HOME/bin/startup.sh"

echo "=== Deployment Complete ==="
echo "Application will be available at: http://localhost:8080/"
echo "Waiting for deployment to finish..."
sleep 5

# Tail the logs to see deployment progress
echo "=== Tomcat Logs ==="
if [ -f "$CATALINA_HOME/logs/catalina.out" ]; then
    tail -f "$CATALINA_HOME/logs/catalina.out"
else
    echo "Warning: Log file not found at $CATALINA_HOME/logs/catalina.out"
    echo "Please check Tomcat logs manually"
fi 