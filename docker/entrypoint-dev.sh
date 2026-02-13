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

# Run database migrations
echo "Running database migrations..."
if [ -f "/app/docker/03-migration-hibernate-sequence.sql" ]; then
  PGPASSWORD=treebase psql -h postgres -U treebase -d treebase -f /app/docker/03-migration-hibernate-sequence.sql || echo "Warning: Migration may have failed"
else
  echo "Migration script not found at /app/docker/03-migration-hibernate-sequence.sql"
fi
echo "Database migrations complete!"

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
if ! unzip -q /app/treebase-web/target/treebase-web.war "WEB-INF/*"; then
    echo "WARNING: Failed to extract WEB-INF from WAR file"
    echo "This may cause runtime issues. Check if WAR file exists and is valid."
    # Continue anyway as the webapp directory might be mounted
fi

# Copy compiled classes and libraries to mounted volumes
if [ -d "WEB-INF/classes" ]; then
  echo "Copying compiled classes..."
  mkdir -p /usr/local/tomcat/webapps/treebase-web/WEB-INF/classes
  cp -r WEB-INF/classes/* /usr/local/tomcat/webapps/treebase-web/WEB-INF/classes/ || echo "Warning: Some class files may not have copied"
fi

if [ -d "WEB-INF/lib" ]; then
  echo "Copying libraries..."
  mkdir -p /usr/local/tomcat/webapps/treebase-web/WEB-INF/lib
  cp -r WEB-INF/lib/* /usr/local/tomcat/webapps/treebase-web/WEB-INF/lib/ || echo "Warning: Some library files may not have copied"
fi

# Copy other WEB-INF resources
if [ -d "WEB-INF" ]; then
  echo "Copying WEB-INF configuration files..."
  # Copy XML configs and other non-classes, non-lib files
  find WEB-INF -type f ! -path "WEB-INF/classes/*" ! -path "WEB-INF/lib/*" -print0 | while IFS= read -r -d '' file; do
    target_dir="/usr/local/tomcat/webapps/treebase-web/$(dirname "$file")"
    mkdir -p "$target_dir"
    cp "$file" "$target_dir/" || echo "Warning: Failed to copy $file"
  done
fi

# Copy DTD files to /tmp/dtd for validator configuration
# The validator XML files reference ./dtd/validator_1_3_0.dtd which gets resolved
# relative to the working directory (/tmp) during Spring initialization
if [ -d "WEB-INF/dtd" ]; then
  echo "Copying DTD files to /tmp/dtd for validator..."
  mkdir -p /tmp/dtd
  cp -r WEB-INF/dtd/* /tmp/dtd/
  ls -la /tmp/dtd/
fi

# Copy Mesquite library files from the mounted source directory
# The treebase-core/lib folder contains the headless Mesquite distribution with:
# - mesquite/ - Mesquite core classes
# - headless/ - Headless AWT implementation  
# - com/apple/ - Apple API stubs (required by Mesquite even on non-Mac platforms)
# - Other supporting libraries
if [ -d "/app/treebase-core/lib" ]; then
  echo "Copying Mesquite library to /usr/local/mesquite..."
  cp -r /app/treebase-core/lib/* /usr/local/mesquite/
  echo "Mesquite library installed."
else
  echo "WARNING: Mesquite library not found at /app/treebase-core/lib"
  echo "Nexus file parsing may not work correctly."
fi

echo "Setup complete! Starting Tomcat..."
echo "========================================"
echo "JSP files are mounted from: ./treebase-web/src/main/webapp"
echo "Edit JSP files locally and refresh browser to see changes!"
echo "========================================"

# Execute the CMD
exec catalina.sh run
