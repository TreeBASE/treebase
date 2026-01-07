# Dependency and Framework Upgrades

This document describes major dependency upgrades and fixes implemented in TreeBASE to support Java 17 and modern frameworks.

## Table of Contents

1. [SLF4J and Logging Framework Compatibility](#slf4j-and-logging-framework-compatibility)
2. [Jersey 2.x Upgrade](#jersey-2x-upgrade)
3. [JUnit 4 Migration](#junit-4-migration)

---

## SLF4J and Logging Framework Compatibility

### Problem

Spring 5.3.26 includes `spring-jcl` module which provides commons-logging API compatibility by bridging to SLF4J **1.7.x** API. The project initially used SLF4J 2.0.16, which has breaking API changes from 1.7.x.

**Error observed:**
```
java.lang.NoSuchMethodError: 'void org.slf4j.spi.LocationAwareLogger.log(...)'
at org.apache.commons.logging.impl.SLF4JLocationAwareLog.error(...)
at org.springframework.web.context.ContextLoader.initWebApplicationContext(...)
```

### Solution

Downgraded SLF4J from 2.0.16 to 1.7.36 for Spring 5.x compatibility.

**Changes in `pom.xml`:**

```xml
<!-- BEFORE -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.16</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-slf4j2-impl</artifactId>
    <version>2.24.3</version>
</dependency>

<!-- AFTER -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.7.36</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-slf4j-impl</artifactId>
    <version>2.24.3</version>
</dependency>
```

### Benefits

- ✅ Compatible with Spring 5.x's logging bridge
- ✅ No method signature mismatches
- ✅ Clean application startup

### Future Considerations

- SLF4J 1.7.x is in maintenance mode
- Consider upgrading to Spring 6.x (requires Jakarta EE migration) to use SLF4J 2.x
- See roadmap in [deployment fix analysis](#deployment-pipeline-analysis) below

---

## Jersey 2.x Upgrade

### Problem

Jersey 1.x versions bundle their own repackaged ASM 3.x as `jersey.repackaged.org.objectweb.asm.*`, which:

1. Cannot be overridden by external ASM dependencies
2. Cannot read Java 17 bytecode (ASM 3.x predates Java 17 by 12 years)
3. Causes fatal `IllegalArgumentException` during JAX-RS resource scanning

**Error observed:**
```
java.lang.IllegalArgumentException
at jersey.repackaged.org.objectweb.asm.ClassReader.<init>(ClassReader.java:170)
at com.sun.jersey.spi.scanning.AnnotationScannerListener.onProcess(...)
```

### Solution

Upgraded from Jersey 1.19.4 to Jersey 2.41, which uses external ASM 9.x compatible with Java 17.

**Changes in `treebase-web/pom.xml`:**

```xml
<!-- BEFORE: Jersey 1.x -->
<dependency>
    <groupId>com.sun.jersey</groupId>
    <artifactId>jersey-server</artifactId>
    <version>1.19.4</version>
</dependency>

<!-- AFTER: Jersey 2.x -->
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

**Changes in `web.xml`:**

```xml
<!-- BEFORE -->
<servlet-class>com.sun.jersey.spi.container.servlet.ServletContainer</servlet-class>
<init-param>
    <param-name>com.sun.jersey.config.property.packages</param-name>
    ...
</init-param>

<!-- AFTER -->
<servlet-class>org.glassfish.jersey.servlet.ServletContainer</servlet-class>
<init-param>
    <param-name>jersey.config.server.provider.packages</param-name>
    ...
</init-param>
```

**Code changes:**

Updated import statements in Java files:

```java
// BEFORE
import com.sun.jersey.multipart.*;

// AFTER
import org.glassfish.jersey.media.multipart.*;
```

### API Compatibility

**What stayed the same:**
- JAX-RS annotations (`@Path`, `@GET`, `@POST`, `@Produces`, etc.)
- REST endpoint logic
- HTTP semantics
- Request/response handling

**What changed:**
- Package names: `com.sun.jersey.*` → `org.glassfish.jersey.*`
- Configuration property names
- Servlet class name
- Dependency injection framework (now uses HK2)

### Benefits

| Aspect | Jersey 1.x | Jersey 2.x |
|--------|-----------|-----------|
| ASM | Repackaged 3.x (2009) | External 9.x (2021+) |
| Java 17 | ❌ Fails | ✅ Works |
| Maintenance | EOL 2018 | Active |
| Can Override ASM | ❌ No | ✅ Yes |

### Migration Effort

- **Files changed:** 3 (pom.xml, web.xml, DryadImporter.java)
- **Lines changed:** ~50 total
- **Breaking changes:** Minimal (package names only)
- **Risk level:** Low (JAX-RS API unchanged)

---

## JUnit 4 Migration

### Problem

Tests were using JUnit 3 style with deprecated Spring test base classes like `AbstractTransactionalDataSourceSpringContextTests`, which don't support modern Spring versions.

### Solution

Migrated to JUnit 4 with Spring 3.2.18.RELEASE test support using annotations.

**Changes in `pom.xml`:**

```xml
<!-- Spring Framework - Modular Dependencies -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
    <version>3.2.18.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>3.2.18.RELEASE</version>
</dependency>
<!-- Additional spring-* modules -->

<!-- JUnit 4 -->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.13.2</version>
</dependency>

<!-- Maven Surefire for test execution -->
<dependency>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0-M5</version>
</dependency>
```

**Test base class modernization:**

```java
// BEFORE: JUnit 3 style
public abstract class AbstractDAOTest 
    extends AbstractTransactionalDataSourceSpringContextTests {
    // ...
}

// AFTER: JUnit 4 style
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = CoreServiceLauncher.getSpringConfigurations())
@TransactionConfiguration(defaultRollback = true)
@Transactional
public abstract class AbstractDAOTest {
    
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    
    @Autowired
    protected PlatformTransactionManager transactionManager;
    
    @Autowired
    protected ApplicationContext applicationContext;
}
```

**Test method updates:**

```java
// BEFORE: JUnit 3 style
public void testSomething() {
    // test code
}

// AFTER: JUnit 4 style
@Test
public void testSomething() {
    // test code
}
```

### Benefits

- ✅ Modern Spring test annotations
- ✅ Automatic transaction rollback by default
- ✅ Dependency injection via `@Autowired`
- ✅ No deprecated APIs
- ✅ Better IDE support

### Compatibility

- **Java:** 1.7+
- **Spring:** 3.2.18.RELEASE (modular dependencies)
- **JUnit:** 4.13.2
- **Hibernate:** 3.x compatible

---

## Deployment Pipeline Analysis

### Historical Context

The deployment pipeline previously had complex workarounds involving:
1. Starting Tomcat
2. Stopping Tomcat
3. Manually removing conflicting JARs with shell scripts
4. Restarting Tomcat

This approach masked underlying dependency management issues rather than fixing them.

### Root Causes Fixed

All three issues above stemmed from dependency version incompatibilities:

1. **SLF4J 2.0.16 incompatible with Spring 5.x** → Fixed by downgrading to 1.7.36
2. **Jersey 1.x's internal ASM 3.x can't read Java 17 bytecode** → Fixed by upgrading to Jersey 2.41
3. **Conflicting transitive dependencies** → Fixed by proper Maven exclusions

### Current State

Deployment workflow is now clean and simple:
```yaml
- Build WARs with Maven
- Deploy to Tomcat
- Start Tomcat
- Verify application health
```

All dependency management is handled in Maven POMs, not at deployment time.

### Future Upgrade Paths

**Short-term (Stable):**
- Current approach using Spring 5.x, Jersey 2.x, SLF4J 1.7.x
- Low risk, proven compatibility
- Maintenance mode libraries but stable

**Medium-term (Recommended):**
- Continue with Jersey 2.x
- Plan for Spring 6.x migration
- Requires Jakarta EE namespace migration (javax.* → jakarta.*)

**Long-term (Modern Stack):**
- Spring 6.x
- Jersey 3.x (Jakarta EE)
- SLF4J 2.x
- Full Java 17 ecosystem alignment

### Estimated Efforts

- **Current fixes:** Complete ✅
- **Spring 6.x migration:** 40-60 hours (namespace changes, testing)
- **Jersey 3.x upgrade:** 8-16 hours (with Spring 6.x)
- **Full modernization:** 80-120 hours (combined effort)

---

## References

- [Spring Framework 5.3.x Documentation](https://docs.spring.io/spring-framework/docs/5.3.x/reference/html/)
- [SLF4J 2.0 Migration Guide](https://www.slf4j.org/faq.html#changesInVersion200)
- [Jersey Migration Guide (1.x → 2.x)](https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/migration.html)
- [JUnit 4 Documentation](https://junit.org/junit4/)
- [ASM Documentation](https://asm.ow2.io/)
