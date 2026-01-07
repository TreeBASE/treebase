# Docker Deployment Guide

This guide explains how to run TreeBASE using Docker with support for rapid JSP development.

## Prerequisites

- [Docker](https://docs.docker.com/get-docker/) 20.10 or later
- [Docker Compose](https://docs.docker.com/compose/install/) 1.29 or later
- At least 4GB of free RAM
- At least 10GB of free disk space

## Quick Start - Development Mode with JSP Hot-Reload

This is the **recommended approach for UI development** as it allows you to edit JSP files locally and see changes immediately:

```bash
# Start the development environment
docker-compose --profile development up

# Wait for the build to complete (first time takes 5-10 minutes)
# Once you see "Tomcat started", access the application at:
# http://localhost:8080/treebase-web/
```

### Editing JSP Files

1. **Edit any JSP file** in `treebase-web/src/main/webapp/` using your favorite editor
2. **Save the file**
3. **Refresh your browser** - that's it!

For example, to modify the homepage:
```bash
# Edit the index page
vim treebase-web/src/main/webapp/index.jsp

# Or use your preferred editor
code treebase-web/src/main/webapp/index.jsp
```

The changes will be reflected immediately without rebuilding or restarting Docker containers.

### What Can You Edit Live?

✅ **JSP files** - Instant refresh on save  
✅ **CSS files** - Instant refresh  
✅ **JavaScript files** - Instant refresh  
✅ **Images and static resources** - Instant refresh  
❌ **Java source files** - Requires container restart

To rebuild after Java changes:
```bash
docker-compose --profile development restart web-dev
```

## Production Mode

For a production-like deployment with a fully self-contained image:

```bash
# Build and start production environment
docker-compose --profile production up --build
```

This creates an optimized Docker image with the WAR file built inside.

## Architecture Overview

### Development Setup
- Mounts local `treebase-web/src/main/webapp/` directory as a Docker volume
- Tomcat automatically recompiles JSP files when they change
- Includes Maven and build tools inside container
- PostgreSQL database runs in a separate container

### Production Setup  
- Multi-stage Docker build
- WAR file compiled during image creation
- No external file dependencies
- Optimized runtime-only image

## Common Commands

### Start Services
```bash
# Development mode (with JSP hot-reload)
docker-compose --profile development up

# Production mode
docker-compose --profile production up

# Run in background (detached mode)
docker-compose --profile development up -d
```

### Stop Services
```bash
docker-compose down

# Also remove volumes (database data will be lost!)
docker-compose down -v
```

### View Logs
```bash
# All services
docker-compose --profile development logs -f

# Just web application
docker-compose --profile development logs -f web-dev

# Just database
docker-compose logs -f postgres
```

### Rebuild Application
```bash
# Clean rebuild (development mode)
docker-compose --profile development down -v
docker-compose --profile development up --build

# Rebuild production image
docker-compose --profile production build --no-cache
```

### Access Database
```bash
# Connect to PostgreSQL
docker exec -it treebase-postgres psql -U treebase -d treebase

# From host (if you have psql installed)
psql -h localhost -U treebase -d treebase
# Password: treebase
```

### Shell Access
```bash
# Access web container
docker exec -it treebase-web-dev bash

# Access database container  
docker exec -it treebase-postgres bash
```

## File Structure

```
.
├── Dockerfile              # Production build (multi-stage)
├── Dockerfile.dev          # Development build with tools
├── docker-compose.yml      # Service orchestration
├── .dockerignore          # Files excluded from build
├── DOCKER.md              # This file
└── docker/
    ├── README.md          # Detailed documentation
    ├── context.xml        # Tomcat JNDI configuration
    └── entrypoint-dev.sh  # Development startup script
```

## Configuration

### Database Settings

Default credentials (development only):
- Host: `localhost:5432`
- Database: `treebase`
- Username: `treebase`
- Password: `treebase`

To change credentials, edit:
- `docker-compose.yml` - environment variables for PostgreSQL
- `docker/context.xml` - JDBC connection settings

### Application Settings

Configuration is in `docker/context.xml`:
- Database connection (JNDI DataSource)
- Mesquite folder location  
- Site URL
- SMTP settings for email

### Port Mapping

By default, the application runs on port 8080. To use a different port:

```yaml
# In docker-compose.yml, change:
    ports:
      - "9090:8080"  # Use port 9090 instead
```

## Troubleshooting

### Problem: Container fails to start

**Solution:**
```bash
# Check logs for errors
docker-compose --profile development logs

# Verify Docker is running
docker ps

# Check available resources
docker system df
```

### Problem: JSP changes not appearing

**Solutions:**
1. Hard refresh browser (Ctrl+F5 or Cmd+Shift+R)
2. Clear browser cache
3. Verify you're editing the correct file in `treebase-web/src/main/webapp/`
4. Check file permissions (should be readable)

### Problem: Database connection failed

**Solutions:**
```bash
# Verify PostgreSQL is running
docker-compose ps postgres

# Check PostgreSQL logs
docker-compose logs postgres

# Verify credentials match in docker-compose.yml and docker/context.xml
```

### Problem: Out of memory

**Solutions:**
- Increase Docker memory allocation (Docker Desktop → Settings → Resources)
- Increase JVM heap size in `docker-compose.yml`:
  ```yaml
  environment:
    CATALINA_OPTS: "-Xmx1024m ..."  # Increase from 512m
  ```

### Problem: Build fails downloading dependencies

**Solution:**
```bash
# Clear Maven cache and retry
docker-compose --profile development down -v
docker-compose --profile development up --build
```

### Problem: Port already in use

**Solution:**
```bash
# Find what's using port 8080
lsof -i :8080  # macOS/Linux
netstat -ano | findstr :8080  # Windows

# Either:
# 1. Stop the other service, or
# 2. Change port in docker-compose.yml
```

## Performance Tips

### First Run
The development environment builds the entire project on first start. This can take 5-10 minutes depending on:
- Internet speed (Maven downloads ~200MB of dependencies)
- CPU speed (compilation)
- Disk I/O

Subsequent starts are much faster (30 seconds) as dependencies are cached.

### Maven Dependency Caching
The `maven-repo` volume persists Maven dependencies between container restarts, significantly speeding up rebuilds.

### JSP Compilation Performance
JSP files are compiled on-demand. First access after editing may take 1-2 seconds, but subsequent requests are instantaneous.

## Security Considerations

⚠️ **This configuration is for DEVELOPMENT ONLY**

For production deployments:
1. **Change all default passwords**
2. Use environment variables for sensitive data
3. Enable SSL/TLS
4. Restrict database network access
5. Review and harden Tomcat security settings
6. Update all dependencies to latest secure versions
7. Don't expose database port to public network

## Advanced Usage

### Custom Database Initialization

Place SQL scripts in the database init directory:
```bash
# Add to docker-compose.yml volumes:
      - ./my-init-script.sql:/docker-entrypoint-initdb.d/03-custom.sql
```

### Using a Different PostgreSQL Version
```yaml
# In docker-compose.yml:
  postgres:
    image: postgres:14  # or postgres:13, etc.
```

### Multi-stage Development
Run database separately for multiple projects:
```bash
# Start only database
docker-compose up postgres

# Use from multiple TreeBASE checkouts
```

## Getting Help

- See `docker/README.md` for detailed documentation
- Check GitHub issues: https://github.com/TreeBASE/treebase/issues
- Review build documentation: `doc/development/BUILDING.md`
- Review deployment docs: `doc/development/DEPLOYING.md`

## Related Documentation

- [Building TreeBASE](doc/development/BUILDING.md) - Manual build instructions
- [Deploying TreeBASE](doc/development/DEPLOYING.md) - Traditional Tomcat deployment
- [Docker README](docker/README.md) - Detailed Docker documentation
