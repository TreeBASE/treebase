# TreeBASE

TreeBASE, the community repository of published estimates of phylogeny, is an initiative whose 
[history](https://treebase.org/treebase-web/about.html) dates back to the early 1990s. In its current iteration it is an open source 
project implemented in Java 17 LTS and released under a BSD license. This is its source code repository. It is organized in a number of 
subprojects, which are described below. For more high-level documentation about TreeBASE, visit the 
[wiki](https://github.com/TreeBASE/treebase/wiki/Documentation).

## Essential Subprojects for TreeBASE v.2

The following are subprojects that jointly comprise the TreeBASE v.2 MVC application. The application is organized as a data **Model** 
that persists on a PostgreSQL database via Hibernate, multiple **Views** (namely, a JSP/HTML GUI, an oai-pmh web service interface, a 
tree viewing and editing applet, and a news feed), linked together by a **Controller** API in Java 17 LTS, which uses the Spring framework.

- **[treebase-core](treebase-core)** - Java 17 LTS ORM API to access the underlying relational database based on PostgreSQL
- **[treebase-phylowidget](treebase-phylowidget)** - Java 17 LTS browser applet for tree viewing and editing
- **[treebase-web](treebase-web)** - Java 17 LTS MVC web application for the JSP/HTML GUI
- **[treebase_feed](treebase_feed)** - PHP feed with newest studies, to embed in the JSP/HTML GUI
- **[oai-pmh_data_provider](oai-pmh_data_provider)** - additional Java 17 LTS MVC functionality for OAI-PMH interface

## Development

### Getting Started

- **[Docker Deployment](DOCKER.md)** - **Recommended**: Run TreeBASE with Docker for rapid JSP development
- **[Building TreeBASE](doc/development/BUILDING.md)** - Instructions for compiling and packaging the WAR files
- **[Deploying TreeBASE](doc/development/DEPLOYING.md)** - Instructions for deploying on a Tomcat server

### Technical Documentation

- **[DWR Integration](doc/technical-notes/DWR.md)** - Direct Web Remoting AJAX functionality and Spring 5 compatibility
- **[Dependency Upgrades](doc/technical-notes/UPGRADES.md)** - SLF4J, Jersey 2.x, and JUnit 4 migration details

### Archived Documentation

- **[Data Loading](doc/archive/)** - Historical documentation for TreeBASE v.1 to v.2 migration (deprecated)

## Additional Files

- **[pom.xml](pom.xml)** - Maven build file for `treebase-core`, `treebase-web` and `oai-pmh_data_provider`
- **[.github/workflows](.github/workflows)** - CI/CD workflows for building and testing
- **[.gitignore](.gitignore)** - Git ignore patterns
- **[LICENSE.txt](LICENSE.txt)** - BSD license

## Quick Start

### Using Docker (Recommended for UI Development)

```bash
# Clone the repository
git clone https://github.com/TreeBASE/treebase.git
cd treebase

# Start with Docker (includes database and auto-build)
docker compose --profile development up

# Access at http://localhost:8080/treebase-web/
# Edit JSP files in treebase-web/src/main/webapp/ and refresh browser!
```

See **[DOCKER.md](DOCKER.md)** for complete Docker documentation.

### Manual Build

```bash
# Requires Java 17 and Maven
mvn clean compile

# Package for deployment
mvn package -Dmaven.test.skip=true
```

See the [development documentation](doc/development/) for detailed instructions.

## License

This project is released under a BSD license. See [LICENSE.txt](LICENSE.txt) for details.
