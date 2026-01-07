# Testing Guide for Docker Deployment

This document describes how to test the TreeBASE Docker deployment.

## Prerequisites

Before testing, run the validation script:

```bash
./docker/validate-setup.sh
```

All checks should pass before proceeding.

## Test 1: Validate Docker Configuration

### Objective
Verify all Docker files are syntactically correct.

### Steps
```bash
# Validate docker-compose.yml
docker compose config --quiet

# Check Dockerfile syntax (production)
docker build -f Dockerfile --target builder -t test . --no-cache --progress=plain 2>&1 | head -100

# Check Dockerfile.dev syntax
docker build -f Dockerfile.dev -t test-dev . --no-cache --progress=plain 2>&1 | head -100
```

### Expected Result
- No syntax errors
- Docker starts downloading base images
- Build process begins successfully

## Test 2: Production Build (Full)

### Objective
Build the complete production Docker image.

### Steps
```bash
# Build production image
docker compose --profile production build

# Start production services
docker compose --profile production up -d

# Wait for services to start (may take 2-5 minutes on first run)
docker compose logs -f web

# Check application is accessible
curl http://localhost:8080/treebase-web/ 2>&1 | grep -i "treebase"

# Stop services
docker compose down
```

### Expected Result
- Build completes without errors
- WAR file is created and deployed
- Tomcat starts successfully
- Application responds on port 8080
- Database connection is established

### Common Issues
- **Out of memory**: Increase Docker memory allocation
- **Build timeout**: Increase timeout in Docker settings
- **Port conflict**: Change port in docker-compose.yml

## Test 3: Development Build with JSP Hot-Reload

### Objective
Verify development environment with live JSP editing.

### Steps

1. **Start development environment:**
   ```bash
   docker compose --profile development up -d
   ```

2. **Wait for build to complete:**
   ```bash
   # Monitor logs
   docker compose logs -f web-dev
   
   # Wait for message: "Server startup in..."
   ```

3. **Test application access:**
   ```bash
   # Check home page
   curl http://localhost:8080/treebase-web/ 2>&1 | grep -i "treebase"
   
   # Or open in browser
   open http://localhost:8080/treebase-web/  # macOS
   xdg-open http://localhost:8080/treebase-web/  # Linux
   ```

4. **Test JSP hot-reload:**
   ```bash
   # Find a JSP file to edit
   ls -la treebase-web/src/main/webapp/*.jsp
   
   # Make a visible change (add a comment at the top)
   echo "<!-- DOCKER TEST MODIFICATION -->" | cat - treebase-web/src/main/webapp/index.jsp > /tmp/index.jsp.new
   mv /tmp/index.jsp.new treebase-web/src/main/webapp/index.jsp
   
   # Wait a moment for file system sync
   sleep 2
   
   # Check if change is reflected
   curl http://localhost:8080/treebase-web/ 2>&1 | grep "DOCKER TEST MODIFICATION"
   
   # Should output the comment we added
   ```

5. **Verify database connection:**
   ```bash
   # Connect to database
   docker exec -it treebase-postgres psql -U treebase -d treebase -c "\dt"
   
   # Should list database tables
   ```

6. **Test container restart:**
   ```bash
   # Restart web container
   docker compose restart web-dev
   
   # Wait for restart
   sleep 10
   
   # Verify still works
   curl http://localhost:8080/treebase-web/ 2>&1 | grep -i "treebase"
   ```

7. **Cleanup:**
   ```bash
   # Stop and remove containers
   docker compose down
   
   # Optional: Remove volumes (database data)
   docker compose down -v
   ```

### Expected Results
- Development environment builds successfully (5-10 minutes first time)
- Application starts and responds on port 8080
- JSP file changes are immediately reflected in browser
- Database connection works
- Container can be restarted without issues

### Common Issues

#### JSP changes not appearing
- **Solution**: Hard refresh browser (Ctrl+F5)
- **Solution**: Check file permissions are readable
- **Solution**: Verify you edited file in correct location

#### Build fails
- **Solution**: Check Docker has enough memory (4GB minimum)
- **Solution**: Clear volumes: `docker compose down -v`
- **Solution**: Check internet connection (Maven downloads dependencies)

#### Port 8080 already in use
- **Solution**: Stop other service using port 8080
- **Solution**: Change port in docker-compose.yml

## Test 4: Volume Persistence

### Objective
Verify database data persists across container restarts.

### Steps
```bash
# Start services
docker compose --profile development up -d

# Wait for startup
sleep 30

# Create test data
docker exec -it treebase-postgres psql -U treebase -d treebase -c "CREATE TABLE test_table (id INTEGER);"

# Verify table exists
docker exec -it treebase-postgres psql -U treebase -d treebase -c "\dt test_table"

# Restart services (NOT down -v)
docker compose restart

# Wait for restart
sleep 30

# Verify table still exists
docker exec -it treebase-postgres psql -U treebase -d treebase -c "\dt test_table"

# Cleanup
docker compose down
```

### Expected Result
- Test table persists across restart
- Database data is maintained in volume

## Test 5: Quick Start Script

### Objective
Verify the quick-start script works correctly.

### Steps
```bash
# Run quick start script
./docker/quick-start.sh

# Follow prompts
# Press Ctrl+C after services start
```

### Expected Result
- Validation runs successfully
- Services start without errors
- Script provides clear instructions

## Performance Benchmarks

### Build Times (Reference)
- **First build**: 5-10 minutes (downloads ~500MB dependencies)
- **Subsequent builds**: 30 seconds - 2 minutes (cached)
- **Container start**: 10-30 seconds
- **JSP compile**: 1-2 seconds per file

### Resource Usage (Typical)
- **Memory**: 1-2GB for web container, 200-500MB for database
- **Disk**: ~1GB for images, ~500MB for volumes
- **CPU**: High during build, low during runtime

## Troubleshooting Commands

### View all containers
```bash
docker ps -a
```

### View volumes
```bash
docker volume ls
```

### Inspect a container
```bash
docker inspect treebase-web-dev
```

### Check container logs
```bash
docker logs treebase-web-dev
docker logs treebase-postgres
```

### Remove everything (fresh start)
```bash
docker compose down -v
docker system prune -a
```

### Check disk usage
```bash
docker system df
```

## Automated Testing

For CI/CD integration, create a test script:

```bash
#!/bin/bash
set -e

# Start services in background
docker compose --profile production up -d

# Wait for startup
sleep 60

# Test application responds
curl -f http://localhost:8080/treebase-web/ || exit 1

# Test database
docker exec treebase-postgres psql -U treebase -d treebase -c "SELECT 1" || exit 1

# Cleanup
docker compose down

echo "All tests passed!"
```

## Reporting Issues

When reporting Docker-related issues, include:

1. **Docker version**: `docker --version`
2. **Docker Compose version**: `docker compose version`
3. **Operating system**: `uname -a` or equivalent
4. **Error logs**: `docker compose logs`
5. **Container status**: `docker ps -a`
6. **Disk space**: `df -h`
7. **Memory**: `free -h` (Linux) or Activity Monitor (macOS)

## Next Steps

After successful testing:

1. Review the [DOCKER.md](../DOCKER.md) for usage instructions
2. Explore the application at http://localhost:8080/treebase-web/
3. Edit JSP files and iterate on the UI
4. Refer to [docker/README.md](README.md) for advanced configuration
