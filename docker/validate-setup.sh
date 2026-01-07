#!/bin/bash
# Validation script for TreeBASE Docker setup
# This script checks prerequisites and validates the Docker configuration

set -e

echo "========================================="
echo "TreeBASE Docker Setup Validation"
echo "========================================="
echo ""

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check functions
check_command() {
    if command -v $1 &> /dev/null; then
        echo -e "${GREEN}✓${NC} $1 is installed"
        if [ ! -z "$2" ]; then
            VERSION=$($1 $2 2>&1 | head -1)
            echo "  Version: $VERSION"
        fi
        return 0
    else
        echo -e "${RED}✗${NC} $1 is not installed"
        return 1
    fi
}

check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✓${NC} Found: $1"
        return 0
    else
        echo -e "${RED}✗${NC} Missing: $1"
        return 1
    fi
}

check_dir() {
    if [ -d "$1" ]; then
        echo -e "${GREEN}✓${NC} Found directory: $1"
        return 0
    else
        echo -e "${RED}✗${NC} Missing directory: $1"
        return 1
    fi
}

ERRORS=0

# Check Docker
echo "Checking Docker installation..."
if check_command docker "--version"; then
    # Check if Docker daemon is running
    if docker info &> /dev/null; then
        echo -e "${GREEN}✓${NC} Docker daemon is running"
    else
        echo -e "${RED}✗${NC} Docker daemon is not running"
        echo "  Please start Docker Desktop or the Docker service"
        ERRORS=$((ERRORS+1))
    fi
else
    echo -e "${RED}!${NC} Please install Docker: https://docs.docker.com/get-docker/"
    ERRORS=$((ERRORS+1))
fi
echo ""

# Check Docker Compose
echo "Checking Docker Compose..."
if docker compose version &> /dev/null; then
    VERSION=$(docker compose version)
    echo -e "${GREEN}✓${NC} Docker Compose is available"
    echo "  Version: $VERSION"
else
    echo -e "${YELLOW}!${NC} Docker Compose v2 not found, checking for v1..."
    if check_command docker-compose "--version"; then
        echo -e "${YELLOW}!${NC} Using legacy docker-compose (v1)"
        echo "  Consider upgrading to Docker with integrated Compose v2"
    else
        echo -e "${RED}✗${NC} Docker Compose not found"
        ERRORS=$((ERRORS+1))
    fi
fi
echo ""

# Check required files
echo "Checking Docker configuration files..."
check_file "Dockerfile" || ERRORS=$((ERRORS+1))
check_file "Dockerfile.dev" || ERRORS=$((ERRORS+1))
check_file "docker-compose.yml" || ERRORS=$((ERRORS+1))
check_file ".dockerignore" || ERRORS=$((ERRORS+1))
check_file "docker/context.xml" || ERRORS=$((ERRORS+1))
check_file "docker/entrypoint-dev.sh" || ERRORS=$((ERRORS+1))
echo ""

# Check source directories
echo "Checking source code structure..."
check_dir "treebase-core/src" || ERRORS=$((ERRORS+1))
check_dir "treebase-web/src" || ERRORS=$((ERRORS+1))
check_dir "treebase-web/src/main/webapp" || ERRORS=$((ERRORS+1))
echo ""

# Check database initialization files
echo "Checking database initialization files..."
check_file "docker/00-init-roles.sql" || {
    echo -e "${YELLOW}!${NC} Database role initialization file not found"
    ERRORS=$((ERRORS+1))
}
check_file "treebase-core/src/main/resources/TBASE2_POSTGRES_CREATION.sql" || {
    echo -e "${YELLOW}!${NC} Database schema file not found - database may not initialize properly"
    ERRORS=$((ERRORS+1))
}
if ! check_file "treebase-core/src/main/resources/initTreebase.sql"; then
    echo -e "${YELLOW}!${NC} Database init file not found - database may not initialize properly"
    ERRORS=$((ERRORS+1))
fi
echo ""

# Check configuration examples
echo "Checking configuration examples..."
check_file "treebase-core/src/main/resources/jdbc.properties.example" || ERRORS=$((ERRORS+1))
check_file "treebase-web/src/main/webapp/META-INF/context.xml.example" || ERRORS=$((ERRORS+1))
echo ""

# Validate docker-compose.yml syntax
echo "Validating docker-compose.yml syntax..."
if docker compose config > /dev/null 2>&1; then
    echo -e "${GREEN}✓${NC} docker-compose.yml syntax is valid"
else
    echo -e "${RED}✗${NC} docker-compose.yml has syntax errors"
    docker compose config
    ERRORS=$((ERRORS+1))
fi
echo ""

# Check system resources
echo "Checking system resources..."
if [ "$(uname)" == "Darwin" ]; then
    # macOS
    TOTAL_MEM=$(sysctl -n hw.memsize)
    TOTAL_MEM_GB=$((TOTAL_MEM / 1024 / 1024 / 1024))
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
    # Linux
    TOTAL_MEM=$(grep MemTotal /proc/meminfo | awk '{print $2}')
    TOTAL_MEM_GB=$((TOTAL_MEM / 1024 / 1024))
else
    TOTAL_MEM_GB=0
fi

if [ $TOTAL_MEM_GB -ge 4 ]; then
    echo -e "${GREEN}✓${NC} System has ${TOTAL_MEM_GB}GB RAM (minimum 4GB required)"
else
    echo -e "${YELLOW}!${NC} System has ${TOTAL_MEM_GB}GB RAM (minimum 4GB recommended)"
    echo "  You may experience performance issues"
fi

# Check disk space
DISK_SPACE=$(df -h . | tail -1 | awk '{print $4}')
echo -e "${GREEN}✓${NC} Available disk space: $DISK_SPACE"
echo ""

# Summary
echo "========================================="
if [ $ERRORS -eq 0 ]; then
    echo -e "${GREEN}✓ All checks passed!${NC}"
    echo ""
    echo "You can now run:"
    echo "  docker compose --profile development up"
    echo ""
    echo "Or for production:"
    echo "  docker compose --profile production up --build"
else
    echo -e "${RED}✗ Found $ERRORS error(s)${NC}"
    echo ""
    echo "Please fix the errors above before running Docker."
    exit 1
fi
echo "========================================="
