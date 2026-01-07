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

# Extract WEB-INF to shared volumes for the running application
echo "Extracting WEB-INF from WAR file..."
cd /tmp
unzip -q /app/treebase-web/target/treebase-web.war "WEB-INF/*" || true

# Copy compiled classes and libraries to mounted volumes
if [ -d "WEB-INF/classes" ]; then
  cp -r WEB-INF/classes/* /usr/local/tomcat/webapps/treebase-web/WEB-INF/classes/ 2>/dev/null || true
fi

if [ -d "WEB-INF/lib" ]; then
  cp -r WEB-INF/lib/* /usr/local/tomcat/webapps/treebase-web/WEB-INF/lib/ 2>/dev/null || true
fi

# Copy other WEB-INF resources
if [ -d "WEB-INF" ]; then
  # Copy XML configs and other non-classes, non-lib files
  find WEB-INF -type f ! -path "WEB-INF/classes/*" ! -path "WEB-INF/lib/*" \
    -exec cp --parents {} /usr/local/tomcat/webapps/treebase-web/ \;
fi

echo "Setup complete! Starting Tomcat..."
echo "========================================"
echo "JSP files are mounted from: ./treebase-web/src/main/webapp"
echo "Edit JSP files locally and refresh browser to see changes!"
echo "========================================"

# Execute the CMD
exec "$@"
