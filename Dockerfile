# Multi-stage Dockerfile for TreeBASE web application
# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy Maven project files
COPY pom.xml .
COPY treebase-core/pom.xml treebase-core/
COPY treebase-web/pom.xml treebase-web/
COPY oai-pmh_data_provider/pom.xml oai-pmh_data_provider/
COPY oai-pmh_data_provider/data_provider_web/pom.xml oai-pmh_data_provider/data_provider_web/

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B || true

# Copy source code
COPY treebase-core/src treebase-core/src
COPY treebase-web/src treebase-web/src
COPY treebase-web/lib treebase-web/lib
COPY oai-pmh_data_provider oai-pmh_data_provider

# Copy configuration examples and create actual config files
COPY treebase-core/src/main/resources/jdbc.properties.example treebase-core/src/main/resources/jdbc.properties
COPY treebase-web/src/main/webapp/META-INF/context.xml.example treebase-web/src/main/webapp/META-INF/context.xml

# Build the WAR file
RUN mvn clean package -Dmaven.test.skip=true -B

# Stage 2: Runtime with Tomcat
FROM tomcat:9-jdk17

# Install PostgreSQL client utilities (optional, for debugging) and unzip for WAR extraction
RUN apt-get update && \
    apt-get install -y postgresql-client unzip && \
    rm -rf /var/lib/apt/lists/*

# Remove default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Pre-expand WAR file during image build (not at runtime)
# This prevents race conditions where Tomcat starts serving requests before
# the WAR is fully expanded, which causes intermittent ClassNotFoundException
# for compiled JSP classes and JSTL taglibs
COPY --from=builder /build/treebase-web/target/treebase-web.war /tmp/treebase-web.war
RUN mkdir -p /usr/local/tomcat/webapps/treebase-web && \
    cd /usr/local/tomcat/webapps/treebase-web && \
    unzip -q /tmp/treebase-web.war && \
    rm /tmp/treebase-web.war

# Download and install PostgreSQL JDBC driver
RUN curl -o /usr/local/tomcat/lib/postgresql.jar \
    https://jdbc.postgresql.org/download/postgresql-42.7.7.jar

# Create a directory for Mesquite (placeholder)
RUN mkdir -p /usr/local/mesquite

# Set environment variables for Tomcat
# Java 17 compatibility flags based on GitHub Actions workflow
ENV CATALINA_OPTS="-Djava.awt.headless=true \
    -Xmx512m \
    -XX:+UseG1GC \
    -Dorg.apache.el.parser.SKIP_IDENTIFIER_CHECK=true \
    --add-opens java.base/java.lang=ALL-UNNAMED \
    --add-opens java.base/java.util=ALL-UNNAMED \
    --add-opens java.base/java.lang.reflect=ALL-UNNAMED"

# Expose Tomcat port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/treebase-web/ || exit 1

# Start Tomcat
CMD ["catalina.sh", "run"]
