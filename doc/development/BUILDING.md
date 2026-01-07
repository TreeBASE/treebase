# Building TreeBASE

This document describes how to compile and package the TreeBASE artifacts.

## Prerequisites

### Java 17 LTS

Java 17 is required for building TreeBASE. Verify your installation:

```bash
$ java -version
openjdk version "17.0.9" 2023-10-17
OpenJDK Runtime Environment (build 17.0.9+9-Ubuntu-120.04)
OpenJDK 64-Bit Server VM (build 17.0.9+9-Ubuntu-120.04, mixed mode, sharing)
```

#### Installing Java 17

**Linux (Ubuntu 20.04 LTS or later):**
```bash
sudo apt-get update
sudo apt-get install openjdk-17-jdk
```

**Windows and macOS:**
Download from [Adoptium Temurin](https://adoptium.net/) or [Oracle JDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

### Maven

Ensure Maven is installed:

```bash
$ mvn -V
Apache Maven 3.8.7
Maven home: /usr/share/maven
Java version: 17.0.9, vendor: Ubuntu
Java home: /usr/lib/jvm/java-17-openjdk-amd64
```

**Note:** If you get a message about `JAVA_HOME` not being set, reinstall the full JDK (not just JRE):
```bash
sudo apt install openjdk-17-jdk
```

If Maven is not installed:
```bash
sudo apt install maven
```

### Git

```bash
$ git --version
git version 2.7.4
```

Install if needed:
```bash
sudo apt install git
```

## Building the Artifacts

### 1. Clone the Repository

```bash
git clone https://github.com/TreeBASE/treebase.git
cd treebase
```

### 2. Compile

Compile all modules using the `compiler:compile` Maven goal:

```bash
mvn compiler:compile
```

Successful build output:
```
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO] 
[INFO] Treebase ........................................... SUCCESS [  0.396 s]
[INFO] treebase-core ...................................... SUCCESS [  3.234 s]
[INFO] treebase-web ....................................... SUCCESS [ 11.620 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

### 3. Configure Database Connection

Before packaging, copy and configure the database connection files:

1. **Core JDBC configuration:**
   ```bash
   cp treebase-core/src/main/resources/jdbc.properties.example \
      treebase-core/src/main/resources/jdbc.properties
   ```
   
   Edit `jdbc.properties` with your database credentials.

2. **Web application context:**
   ```bash
   cp treebase-web/src/main/webapp/META-INF/context.xml.example \
      treebase-web/src/main/webapp/META-INF/context.xml
   ```
   
   Edit `context.xml` with your database credentials and Mesquite folder location.

**Note:** Current configuration values are stored in the private repository TreeBASE/treebase-config.

### 4. Package the WAR

Create the deployable WAR file:

```bash
mvn package -Dmaven.test.skip=true
```

Successful packaging output:
```
[INFO] Packaging webapp
[INFO] Assembling webapp [treebase-web] in [/path/to/treebase/treebase-web/target/treebase-web]
[INFO] Building war: /path/to/treebase/treebase-web/target/treebase-web.war
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

## Build Artifacts

The build produces:

### treebase-web.war (~59MB)

Located at: `treebase-web/target/treebase-web.war`

This WAR contains:
- Compiled classes from `treebase-web`
- Compiled classes from `treebase-core` (bundled as JAR in `WEB-INF/lib/`)
- All dependency JARs
- Configuration files (context.xml, jdbc.properties)
- Web assets (HTML, JSP, CSS, JavaScript)

### Project Structure

The build operates on these subprojects:

- **[treebase-core](../../treebase-core)** - ORM API for PostgreSQL database access via Hibernate
- **[treebase-web](../../treebase-web)** - MVC web application with JSP/HTML GUI
- **[oai-pmh_data_provider](../../oai-pmh_data_provider)** - OAI-PMH interface functionality

## Next Steps

After building, see [DEPLOYING.md](DEPLOYING.md) for deployment instructions.
