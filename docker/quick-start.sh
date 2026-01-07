#!/bin/bash
# Quick start script for TreeBASE Docker development environment

set -e

echo "========================================="
echo "TreeBASE Docker Quick Start"
echo "========================================="
echo ""

# Check if Docker is running
if ! docker info &> /dev/null; then
    echo "ERROR: Docker is not running"
    echo "Please start Docker Desktop or the Docker service"
    exit 1
fi

# Validate setup
echo "Validating Docker setup..."
if [ -f "./docker/validate-setup.sh" ]; then
    ./docker/validate-setup.sh
    if [ $? -ne 0 ]; then
        echo ""
        echo "Please fix the validation errors before continuing."
        exit 1
    fi
else
    echo "Warning: validate-setup.sh not found, skipping validation"
fi

echo ""
echo "========================================="
echo "Starting TreeBASE Development Environment"
echo "========================================="
echo ""

# Check for required SQL files
echo "Checking for database initialization files..."
if [ ! -f "treebase-core/src/main/resources/TBASE2_POSTGRES_CREATION.sql" ]; then
    echo "ERROR: Missing treebase-core/src/main/resources/TBASE2_POSTGRES_CREATION.sql"
    echo "This file is required for database initialization."
    exit 1
fi

if [ ! -f "treebase-core/src/main/resources/initTreebase.sql" ]; then
    echo "WARNING: Missing treebase-core/src/main/resources/initTreebase.sql"
    echo "Database may not be fully initialized."
fi

echo "This will:"
echo "  1. Start PostgreSQL database"
echo "  2. Build the TreeBASE web application (first time only)"
echo "  3. Start Tomcat with JSP hot-reload enabled"
echo ""
echo "First-time setup takes 5-10 minutes."
echo "Subsequent starts are much faster (30 seconds)."
echo ""
read -p "Press Enter to continue or Ctrl+C to cancel..."

echo ""
echo "Starting services..."
docker compose --profile development up

echo ""
echo "========================================="
echo "Development environment stopped"
echo ""
echo "To restart: docker compose --profile development up"
echo "To clean up: docker compose down -v"
echo "========================================="
