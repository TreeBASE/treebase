TreeBASE
========

TreeBASE, the community repository of published estimates of phylogeny, is an initiative whose 
[history](https://treebase.org/treebase-web/about.html) dates back to the early 1990s. In its current iteration it is an open source 
project implemented in Java 1.5 and released under a BSD license. This is its source code repository. It is organized in a number of 
subprojects, which are described below.

### Essential subprojects for TreeBASE v.2

The following are subprojects that jointly comprise the TreeBASE v.2 database bindings, web application, and browser widgets 
(tree viewer and news feed).

- [treebase-core](treebase-core) - Java 1.5 API to access the underlying relational database based on PostgreSQL
- [treebase-phylowidget](treebase-phylowidget) - Java 1.5 browser applet for tree viewing and editing
- [treebase-web](treebase-web) - Java 1.5 MVC web application that is the TreeBASE (v.2) GUI
- [treebase_feed](treebase_feed) - PHP feed with newest studies, to embed in the GUI

### Experiments, non-essential subprojects

The following subprojects are not needed for the web application.

- [treebase-notebook](treebase-notebook) - "Hello World"-level code experiment in Java 1.5
- [treebase-derivatives](treebase-derivatives) - code experiments in Perl that build on the TB2 data model
- [treebase-curation](treebase-curation) - a data repository that was used for migrating data from TB1 to TB2

### Additional files

- [pom.xml](pom.xml) - file for maven that operates on `treebase-core` and `treebase-web`
- [.gitignore](.gitignore) - filters out certain files from committing to `git`
- [.travis.yml](.travis.yml) - intended for setting up continuous integration using Travis, but does nothing
- [LICENSE.txt](LICENSE.txt) - the BSD license, which applies to all artefacts in this project
- [INSTALL.md](INSTALL.md) - installation instructions for server with web application and database
- [LOADING.md](LOADING.md) - instructions for loading data files (nexus+metadata) from TreeBASE v.1
- [README.md](README.md) - this file

Documentation roadmap
---------------------

1. [Technology](https://treebase.org/treebase-web/technology.html) - gives a bird's eye view of the software stack for the 
   TreeBASE web application.
2. [naturalis/role_treebase](https://github.com/naturalis/role_treebase) - tools for provisioning a web application server.
3. [INSTALL.md](INSTALL.md) - contains installation instructions for the web application on a server.
4. [Data access](https://treebase.org/treebase-web/urlAPI.html) - describes APIs by which TreeBASE data can be accessed and retrieved.
5. [supertreebase](http://github.com/TreeBASE/supertreebase) - pipelines that access the API.
6. [LOADING.md](LOADING.md) - describes how to import TreeBASE v.1 files.

