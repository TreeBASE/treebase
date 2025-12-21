![](https://camo.githubusercontent.com/63278dffb48666603fa1121e17a8d3fe0131ecd2ec49b9eee59e3495646dd7fd/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f74657374732d3136372532307061737365642532432532303131362532306661696c65642d637269746963616c)

TreeBASE
========

TreeBASE, the community repository of published estimates of phylogeny, is an initiative whose 
[history](https://treebase.org/treebase-web/about.html) dates back to the early 1990s. In its current iteration it is an open source 
project implemented in Java 1.8 and released under a BSD license. This is its source code repository. It is organized in a number of 
subprojects, which are described below. For more high-level documentation about TreeBASE, visit the 
[wiki](https://github.com/TreeBASE/treebase/wiki/Documentation).

### Essential subprojects for TreeBASE v.2

The following are subprojects that jointly comprise the TreeBASE v.2 MVC application. The application is organized as a data **Model** 
that persists on a PostgreSQL database via Hibernate, multiple **Views** (namely, a JSP/HTML GUI, an oai-pmh web service interface, a 
tree viewing and editing applet, and a news feed), linked together by a **Controller** API in Java 1.8, which uses the Spring framework.

- [treebase-core](treebase-core) - Java 1.8 ORM API to access the underlying relational database based on PostgreSQL
- [treebase-phylowidget](treebase-phylowidget) - Java 1.8 browser applet for tree viewing and editing
- [treebase-web](treebase-web) - Java 1.8 MVC web application for the JSP/HTML GUI
- [treebase_feed](treebase_feed) - PHP feed with newest studies, to embed in the JSP/HTML GUI
- [oai-pmh_data_provider](oai-pmh_data_provider) - additional Java 1.8 MVC functionality for OAI-PMH interface

### Additional files in this directory

- [pom.xml](pom.xml) - file for maven that operates on `treebase-core` and `treebase-web`
- [.gitignore](.gitignore) - filters out certain files from being committed to `git`
- [.travis.yml](.travis.yml) - intended for setting up continuous integration using Travis, but does nothing
- [LICENSE.txt](LICENSE.txt) - the BSD license, which applies to all artefacts in this project
- [BUILDING.md](BUILDING.md) - **build instructions for the core artifacts, uses `pom.xml`**
- [DEPLOYING.md](DEPLOYING.md) - **instructions for deploying TreeBASE compiled artifacts on a web server**
- [INSTALL.md](INSTALL.md) - installation instructions to bootstrap an application for data loading (deprecated)
- [LOADING.md](LOADING.md) - instructions for loading data files (nexus+metadata) from TreeBASE v.1 (deprecated)
- [README.md](README.md) - this file
