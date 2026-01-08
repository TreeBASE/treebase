#!/bin/bash
set -e

echo "========================================"
echo "TreeBASE Development Container Starting"
echo "========================================"

# Wait for PostgreSQL to be ready
echo "Waiting for PostgreSQL to be ready..."
until pg_isready -h postgres -U treebase; do
  echo "PostgreSQL is unavailable - sleeping"
  sleep 2
done
echo "PostgreSQL is ready!"

# Check if we need to build the project
if [ ! -f "/app/treebase-web/target/treebase-web.war" ]; then
  echo "Building TreeBASE web application..."
  cd /app
  
  # Create config files from examples if they don't exist
  if [ ! -f "treebase-core/src/main/resources/jdbc.properties" ]; then
    echo "Creating jdbc.properties from example..."
    cp treebase-core/src/main/resources/jdbc.properties.example \
       treebase-core/src/main/resources/jdbc.properties
  fi
  
  if [ ! -f "treebase-web/src/main/webapp/META-INF/context.xml" ]; then
    echo "Creating context.xml from example..."
    cp treebase-web/src/main/webapp/META-INF/context.xml.example \
       treebase-web/src/main/webapp/META-INF/context.xml
  fi
  
  # Build the project
  mvn clean package -Dmaven.test.skip=true -B
  echo "Build completed!"
else
  echo "WAR file already exists, skipping build..."
fi

# Pre-expand WAR file completely before starting Tomcat
# This prevents race conditions that cause ClassNotFoundException for JSP classes
# and "absolute uri cannot be resolved" errors for JSTL taglibs
echo "Pre-expanding WAR file to webapp directory..."
mkdir -p /usr/local/tomcat/webapps/treebase-web
cd /usr/local/tomcat/webapps/treebase-web

# Extract the entire WAR file first to ensure all JARs and TLDs are in place
if ! unzip -q -o /app/treebase-web/target/treebase-web.war; then
    echo "ERROR: Failed to extract WAR file"
    echo "Check if WAR file exists and is valid."
    exit 1
fi
echo "WAR file extracted successfully."

echo "Setup complete! Starting Tomcat..."
echo "========================================"
echo "JSP files are mounted from: ./treebase-web/src/main/webapp"
echo "Edit JSP files locally and refresh browser to see changes!"
echo "========================================"

# Execute the CMD
exec "$@"
