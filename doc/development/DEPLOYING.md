# Deploying TreeBASE

This document describes how to deploy the TreeBASE web application to a Tomcat servlet container.

## Prerequisites

Before deployment, ensure you have:

1. **Built the WAR file** - Follow the instructions in [BUILDING.md](BUILDING.md)
2. **PostgreSQL database** - Running and populated with data
3. **Tomcat 7 or later** - Servlet container installed
4. **Headless Mesquite** - For phylogenetic tree processing
5. **Apple JARs** - Required by Mesquite (even on non-Mac platforms):
   - `MRJToolkit.jar`
   - `ui.jar`

## Deployment Setup

### 1. Install Required Artifacts

For the Naturalis deployment, artifacts are managed in the [treebase-artifact](https://github.com/naturalis/treebase-artifact) repository:

```bash
sudo su
cd /usr/local/src
git clone https://github.com/naturalis/treebase-artifact
```

This provides:
- WAR files in `/usr/local/src/treebase-artifact/`
- Apple JARs in `/usr/local/src/treebase-artifact/mesquite/apple/`
- Headless Mesquite in `/usr/local/src/treebase-artifact/mesquite/`

### 2. Deploy WAR Files

Copy the WAR files to Tomcat's webapps directory:

**Ubuntu 16.04 LTS with Tomcat 7:**
```bash
cp /usr/local/src/treebase-artifact/treebase-web.war /var/lib/tomcat7/webapps/
cp /usr/local/src/treebase-artifact/data_provider_web.war /var/lib/tomcat7/webapps/
```

### 3. Install PostgreSQL JDBC Driver

Place the PostgreSQL JDBC driver in Tomcat's lib directory:

```bash
cd /usr/share/tomcat7/lib
wget https://jdbc.postgresql.org/download/postgresql-42.1.3.jar
```

## Tomcat Configuration

### Required JVM Arguments

TreeBASE requires specific JVM arguments for proper operation:

1. **Apple JARs in classpath** - For Mesquite support
2. **Increased memory** - Changed from `-Xmx128m` to `-Xmx512m`
3. **JSP construct compatibility** - `-Dorg.apache.el.parser.SKIP_IDENTIFIER_CHECK=true` for Tomcat 7

### Tomcat Startup Script

```bash
TOM=/usr/share/tomcat7/bin
APPLE=/usr/local/src/treebase-artifact/mesquite/apple

/usr/lib/jvm/default-java/bin/java \
    -Djava.util.logging.config.file=/var/lib/tomcat7/conf/logging.properties \
    -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager \
    -Djava.awt.headless=true -Xmx512m -XX:+UseConcMarkSweepGC \
    -Djava.endorsed.dirs=/usr/share/tomcat7/endorsed \
    -classpath $TOM/bootstrap.jar:$TOM/tomcat-juli.jar:$APPLE/MRJToolkit.jar:$APPLE/ui.jar \
    -Dcatalina.base=/var/lib/tomcat7 \
    -Dcatalina.home=/usr/share/tomcat7 \
    -Djava.io.tmpdir=/tmp/tomcat7-tomcat7-tmp \
    -Dorg.apache.el.parser.SKIP_IDENTIFIER_CHECK=true \
    org.apache.catalina.startup.Bootstrap start
```

## Verification

After deployment, verify the application is running:

1. **Check Tomcat logs** for startup errors
2. **Access the web interface** at `http://your-server/treebase-web`
3. **Test the OAI-PMH interface** at `http://your-server/data_provider_web`

## DWR (Direct Web Remoting) Integration

TreeBASE uses DWR for AJAX functionality including:
- Person email autocomplete
- Software/journal name autocomplete
- Algorithm suggestions
- File upload progress tracking

DWR is configured to work with Spring 5 using a custom compatibility wrapper. For details, see the [Technical Notes](../technical-notes/DWR.md).

## Troubleshooting

### Common Issues

**WAR not expanding:**
- Check Tomcat permissions
- Verify sufficient disk space
- Check Tomcat logs for errors

**Database connection errors:**
- Verify PostgreSQL is running
- Check credentials in `context.xml`
- Ensure JDBC driver is in Tomcat lib

**Mesquite errors:**
- Verify Apple JARs are in classpath
- Check Mesquite folder path in `context.xml`
- Ensure proper permissions on Mesquite directory

**Memory errors:**
- Increase `-Xmx` value (current: 512m)
- Monitor heap usage
- Consider `-XX:MaxPermSize` for older Java versions

## Alternative Deployments

This documentation focuses on the Naturalis deployment. For other environments:

1. Adjust paths to match your server layout
2. Use appropriate Tomcat version and directories
3. Configure database credentials for your PostgreSQL instance
4. Ensure Java 17 LTS is available

## Related Documentation

- [Building TreeBASE](BUILDING.md) - How to build the WAR files
- [Technical Notes](../technical-notes/) - Implementation details and troubleshooting
