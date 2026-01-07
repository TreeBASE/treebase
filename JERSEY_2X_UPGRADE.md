# Jersey 2.x Upgrade - Solution to Deployment Failures

## Critical Discovery

The deployment failures were caused by **Jersey 1.x's internal repackaged ASM 3.x** that cannot read Java 17 bytecode.

### The Problem

Jersey 1.19.4 (and all 1.x versions) bundle their own copy of ASM 3.x (from 2009) as `jersey.repackaged.org.objectweb.asm.*`. This causes:

1. **Cannot be overridden**: External ASM 9.2 dependencies don't help - Jersey uses its internal copy
2. **Java 17 incompatible**: ASM 3.x predates Java 17 (2021) by 12 years
3. **Fatal error**: `IllegalArgumentException` when scanning Java 17-compiled classes

### Error Observed
```
java.lang.IllegalArgumentException
at jersey.repackaged.org.objectweb.asm.ClassReader.<init>(ClassReader.java:170)
at com.sun.jersey.spi.scanning.AnnotationScannerListener.onProcess(...)
```

---

## Solution: Jersey 2.41 Upgrade

### Why Jersey 2.x?

| Aspect | Jersey 1.x | Jersey 2.x |
|--------|-----------|-----------|
| ASM | Repackaged 3.x (2009) | External 9.x (2021+) |
| Java 17 | ❌ Fails | ✅ Works |
| Maintenance | EOL 2018 | Active |
| Can Override ASM | ❌ No | ✅ Yes |

---

## Changes Made

### 1. Maven Dependencies

**Before (Jersey 1.x):**
```xml
<dependency>
    <groupId>com.sun.jersey</groupId>
    <artifactId>jersey-server</artifactId>
    <version>1.19.4</version>
</dependency>
```

**After (Jersey 2.x):**
```xml
<dependency>
    <groupId>org.glassfish.jersey.core</groupId>
    <artifactId>jersey-server</artifactId>
    <version>2.41</version>
</dependency>
<dependency>
    <groupId>org.glassfish.jersey.containers</groupId>
    <artifactId>jersey-container-servlet</artifactId>
    <version>2.41</version>
</dependency>
<dependency>
    <groupId>org.glassfish.jersey.inject</groupId>
    <artifactId>jersey-hk2</artifactId>
    <version>2.41</version>
</dependency>
<dependency>
    <groupId>org.glassfish.jersey.media</groupId>
    <artifactId>jersey-media-multipart</artifactId>
    <version>2.41</version>
</dependency>
```

### 2. Servlet Configuration (web.xml)

**Before:**
```xml
<servlet-class>com.sun.jersey.spi.container.servlet.ServletContainer</servlet-class>
<init-param>
    <param-name>com.sun.jersey.config.property.packages</param-name>
    ...
</init-param>
```

**After:**
```xml
<servlet-class>org.glassfish.jersey.servlet.ServletContainer</servlet-class>
<init-param>
    <param-name>jersey.config.server.provider.packages</param-name>
    ...
</init-param>
```

### 3. Code Updates

**Before:**
```java
import com.sun.jersey.multipart.*;
```

**After:**
```java
import org.glassfish.jersey.media.multipart.*;
```

---

## API Compatibility

### What Stayed the Same ✅
- JAX-RS annotations (`@Path`, `@GET`, `@POST`, `@Produces`, etc.)
- REST endpoint logic
- HTTP semantics
- Request/response handling

### What Changed
- Package names: `com.sun.jersey.*` → `org.glassfish.jersey.*`
- Configuration properties
- Servlet class name
- Dependency injection (now uses HK2)

---

## Build Verification

✅ **treebase-core**: Builds successfully  
✅ **treebase-web**: Builds with Jersey 2.41  
✅ **WAR contents**: Verified Jersey 2.41 artifacts  
✅ **No conflicts**: Clean dependency tree  
✅ **ASM 9.2**: Present and compatible  

---

## Benefits

1. **Java 17 Compatible**: Native support for modern bytecode
2. **No Repackaged ASM**: Uses external, upgradeable ASM
3. **Actively Maintained**: Security updates, bug fixes
4. **Modern JAX-RS**: Supports JAX-RS 2.x features
5. **Better Documentation**: Active community support

---

## Migration Effort

- **Files changed**: 3 (pom.xml, web.xml, DryadImportor.java)
- **Lines changed**: ~50 total
- **Breaking changes**: Minimal (package names only)
- **Risk level**: Low (JAX-RS API unchanged)

---

## Testing Checklist

- [  ] Tomcat starts without SEVERE errors
- [  ] HandshakingServlet initializes
- [  ] REST API endpoints respond
- [  ] Multipart uploads work
- [  ] No ASM-related exceptions

---

## References

- [Jersey 2.41 Release](https://eclipse-ee4j.github.io/jersey/)
- [Migration Guide 1.x → 2.x](https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/migration.html)
- [ASM 9.x Documentation](https://asm.ow2.io/)
