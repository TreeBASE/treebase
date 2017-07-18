TreeBASE
========

This is the source code repository of [TreeBASE](http://treebase.org), the community repository of published estimates of phylogeny. 
This source code repository is organized in a number of subprojects, which are described below.

### Essential subprojects for TreeBASE v.2

The following are subprojects that jointly comprise the TreeBASE v.2 database bindings, web application, and browser widgets 
(tree viewer and news feed).

- [treebase-core](treebase-core) - Java API to access the underlying data model, i.e. a relational database based on PostgreSQL
- [treebase-phylowidget](treebase-phylowidget) - Java browser applet for tree viewing and editing
- [treebase-web](treebase-web) - Java MVC web application that is the TreeBASE (v.2) GUI
- [treebase_feed](treebase_feed) - PHP feed with newest studies, to embed in the GUI

### Experiments, non-essential subprojects

- [treebase-notebook](treebase-notebook) code experiment in Java
- [treebase-derivatives](treebase-derivatives) code experiments in Perl that build on the TB2 data model
- [treebase-curation](treebase-curation) a data repository that was used for migrating data from TB1 to TB2

### External links

The following external links should be of additional use to understand the project:

- [Technology](https://treebase.org/treebase-web/technology.html) describes the software stack for the TreeBASE web application.
- [Data access](https://treebase.org/treebase-web/urlAPI.html) lists ways in which TreeBASE data can be accessed and retrieved.

