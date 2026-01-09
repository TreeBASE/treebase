# TreeBASE Documentation

This directory contains the organized documentation for TreeBASE.

## API Documentation

- **[API.md](API.md)** - PhyloWS web service API documentation
- **[OAI-PMH.md](OAI-PMH.md)** - OAI-PMH metadata harvesting interface documentation

## Directory Structure

### [development/](development/)

Documentation for developers working on TreeBASE:

- **[BUILDING.md](development/BUILDING.md)** - How to compile and package TreeBASE artifacts
- **[DEPLOYING.md](development/DEPLOYING.md)** - How to deploy TreeBASE to a Tomcat server

### [technical-notes/](technical-notes/)

In-depth technical documentation on specific components and implementations:

- **[DWR.md](technical-notes/DWR.md)** - Direct Web Remoting integration and Spring 5 compatibility
- **[UPGRADES.md](technical-notes/UPGRADES.md)** - Major dependency and framework upgrades (SLF4J, Jersey, JUnit)

### [archive/](archive/)

Historical documentation that is no longer relevant for current deployments:

- **[INSTALL.md](archive/INSTALL.md)** - Original data loading environment setup (deprecated)
- **[LOADING.md](archive/LOADING.md)** - TreeBASE v.1 to v.2 data migration procedures (deprecated)

## Quick Links

### I want to...

- **Use the PhyloWS API** → [API.md](API.md)
- **Use the OAI-PMH interface** → [OAI-PMH.md](OAI-PMH.md)
- **Build TreeBASE from source** → [development/BUILDING.md](development/BUILDING.md)
- **Deploy TreeBASE to a server** → [development/DEPLOYING.md](development/DEPLOYING.md)
- **Understand DWR integration** → [technical-notes/DWR.md](technical-notes/DWR.md)
- **Learn about recent upgrades** → [technical-notes/UPGRADES.md](technical-notes/UPGRADES.md)
- **Find historical data loading info** → [archive/](archive/)

## Contributing to Documentation

When adding new documentation:

1. Place development guides in `development/`
2. Place technical implementation details in `technical-notes/`
3. Move obsolete documentation to `archive/` with a deprecation notice
4. Update this README with links to new documents
5. Update the main [README.md](../README.md) if the documentation is widely relevant

## Related Resources

- [TreeBASE Wiki](https://github.com/TreeBASE/treebase/wiki/Documentation) - High-level documentation
- [TreeBASE Website](https://treebase.org) - Public-facing application
- [GitHub Issues](https://github.com/TreeBASE/treebase/issues) - Bug reports and feature requests
