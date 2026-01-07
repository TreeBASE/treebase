# Docker Deployment for TreeBASE

This directory contains Docker configuration for running TreeBASE in containers.

## Quick Start for Development (JSP Editing)

To run TreeBASE with **live JSP editing** capabilities:

```bash
# Start the development environment
docker compose --profile development up

# The application will be available at:
# http://localhost:8080/treebase-web/
```

**Edit JSP files locally** in `treebase-web/src/main/webapp/` and simply refresh your browser to see changes!

### How It Works

The development setup uses Docker volumes to mount your local JSP files directly into the running Tomcat container:

1. JSP files from `./treebase-web/src/main/webapp/` are mounted to `/usr/local/tomcat/webapps/treebase-web/`
2. Tomcat automatically detects changes to JSP files and recompiles them on the next request
3. No rebuild or restart required - just edit and refresh!

### What Can You Edit Live?

- ✅ **JSP files** - All `.jsp` files in `treebase-web/src/main/webapp/`
- ✅ **Static resources** - CSS, JavaScript, images
- ✅ **HTML templates** - Any HTML content
- ❌ **Java classes** - Require rebuild (use `docker compose restart web-dev`)
- ❌ **Configuration** - Require restart

## Production Deployment

For a production-like deployment:

```bash
# Build and start the production environment
docker compose --profile production up --build
```

This creates a fully self-contained image with the WAR file built inside.

## Architecture

### Development Mode (`docker compose --profile development`)

- Uses `Dockerfile.dev` 
- Mounts local source code into container
- Builds WAR on first run using Maven
- Volume-mounts JSP files for live editing
- Includes development tools (Maven, PostgreSQL client)

### Production Mode (`docker compose --profile production`)

- Uses `Dockerfile` (multi-stage build)
- Builds WAR during image creation
- Self-contained image with no external dependencies
- Optimized for deployment

## Services

### `postgres`
- PostgreSQL 15 database
- Pre-initialized with TreeBASE schema
- Accessible on `localhost:5432`
- Credentials: `treebase/treebase`
- **Note**: Database initialization creates a `postgres` role to support legacy schema files that reference this role as the owner

### `00-init-roles.sql`
Pre-initialization script that:
- Creates the `postgres` role if it doesn't exist (for schema compatibility)
- Grants the role to the `treebase` user
- Runs before schema creation to prevent ownership errors

### `web-dev` (development)
- Tomcat 9 with JDK 17
- Live JSP editing enabled
- Includes build tools
- Port: `8080`

### `web` (production)
- Tomcat 9 with JDK 17
- Optimized runtime-only image
- Port: `8080`

## Configuration Files

### `context.xml`
Located in `docker/context.xml`, this file configures:
- Database connection (JNDI)
- Mesquite folder location
- Site URL
- SMTP settings

### `entrypoint-dev.sh`
Development container entrypoint that:
- Waits for PostgreSQL
- Builds the WAR if needed
- Extracts compiled classes
- Mounts JSP files
- Starts Tomcat

## Common Tasks

### Rebuild the Application
```bash
# Stop containers
docker compose --profile development down

# Remove volumes to force rebuild
docker compose --profile development down -v

# Start fresh
docker compose --profile development up
```

### View Logs
```bash
# All services
docker compose --profile development logs -f

# Just the web application
docker compose --profile development logs -f web-dev
```

### Access Database
```bash
# Using docker exec
docker exec -it treebase-postgres psql -U treebase -d treebase

# Or from your host (if psql is installed)
psql -h localhost -U treebase -d treebase
```

### Execute Commands in Container
```bash
# Access web container shell
docker exec -it treebase-web-dev bash

# Rebuild Maven project inside container
docker exec -it treebase-web-dev bash -c "cd /app && mvn clean package -Dmaven.test.skip=true"
```

## Customization

### Change Database Credentials

Edit `docker compose.yml` and `docker/context.xml` to update:
- `POSTGRES_USER`, `POSTGRES_PASSWORD` in compose file
- `username`, `password` in context.xml

### Add Mail Catcher (for testing emails)

Add to `docker compose.yml`:

```yaml
  mailcatcher:
    image: schickling/mailcatcher
    ports:
      - "1080:1080"
      - "1025:1025"
```

Update `docker/context.xml` SMTP host to `mailcatcher`.

### Change Tomcat Port

Edit the `ports` section in `docker compose.yml`:

```yaml
    ports:
      - "9090:8080"  # Access on http://localhost:9090
```

## Troubleshooting

### Container won't start
```bash
# Check logs
docker compose --profile development logs

# Verify PostgreSQL is healthy
docker compose ps
```

### Changes not appearing
- For JSP files: Ensure you're editing files in `treebase-web/src/main/webapp/`
- For Java files: Restart the container: `docker compose --profile development restart web-dev`
- Clear browser cache

### Out of memory
Edit `docker compose.yml` and increase the `-Xmx` value in `CATALINA_OPTS`.

### Database connection issues
- Ensure PostgreSQL is running: `docker compose ps postgres`
- Check credentials in `docker/context.xml` match `docker compose.yml`

## File Structure

```
.
├── Dockerfile              # Production multi-stage build
├── Dockerfile.dev          # Development build with tools
├── docker compose.yml      # Orchestration configuration
├── .dockerignore          # Files excluded from build context
└── docker/
    ├── README.md          # This file
    ├── context.xml        # Tomcat JNDI configuration
    └── entrypoint-dev.sh  # Development startup script
```

## Performance Notes

### First Start
The development environment builds the entire project on first start, which can take several minutes depending on:
- Download speed (Maven dependencies)
- CPU speed (compilation)
- Disk speed (I/O operations)

Subsequent starts are much faster as Maven dependencies are cached.

### JSP Compilation
JSP files are compiled on first access after changes. The first page load after editing may be slightly slower, but subsequent requests are fast.

## Security Note

The default configuration uses simple credentials (`treebase/treebase`) suitable for development only. **Do not use these credentials in production!**

For production:
1. Use strong passwords
2. Use environment variables or secrets management
3. Enable SSL/TLS
4. Restrict database access
5. Review and harden Tomcat configuration
