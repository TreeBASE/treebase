# TreeBASE Tomcat Deployment Pipeline - Root Cause Analysis & Fix Proposal

**Date**: 2026-01-07  
**Issue**: Repeated deployment failures in GitHub Actions workflow  
**Failed Run**: https://github.com/TreeBASE/treebase/actions/runs/20792115797

## Executive Summary

The Tomcat deployment pipeline has fundamental architectural problems stemming from incompatible library versions for Java 17 and Spring 5.x. The current "band-aid" approach of manually removing JARs post-deployment is masking deeper Maven dependency management issues.

## Root Cause Analysis

### Problem #1: SLF4J Version Incompatibility (CRITICAL)

**Error**:
```
java.lang.NoSuchMethodError: 'void org.slf4j.spi.LocationAwareLogger.log(org.slf4j.Marker, java.lang.String, int, java.lang.String, java.lang.Throwable)'
at org.apache.commons.logging.impl.SLF4JLocationAwareLog.error(SLF4JLocationAwareLog.java:173)
at org.springframework.web.context.ContextLoader.initWebApplicationContext(ContextLoader.java:299)
```

**Root Cause**:
- Spring 5.3.26 includes `spring-jcl` module which provides commons-logging API compatibility
- `spring-jcl` internally bridges to SLF4J 1.7.x API (`LocationAwareLogger` interface)
- Project uses SLF4J 2.0.16 (parent POM line 169-171)
- SLF4J 2.0.x redesigned `LocationAwareLogger` interface (breaking change)
- This creates API mismatch when Spring's bridge tries to call SLF4J 1.7.x methods on SLF4J 2.0.x classes

**Why Current "Fix" Doesn't Work**:
- Removing `jcl104-over-slf4j-1.4.2.jar` from WEB-INF/lib doesn't fix the underlying issue
- `spring-jcl` is still trying to use SLF4J 2.0.x with a 1.7.x-compatible API
- The error occurs during Spring context initialization, before JAR removal even matters

### Problem #2: Jersey 1.x + ASM 9.x Incompatibility (CRITICAL)

**Error**:
```
java.lang.IncompatibleClassChangeError: class com.sun.jersey.spi.scanning.AnnotationScannerListener$AnnotatedClassVisitor 
can not implement org.objectweb.asm.ClassVisitor, because it is not an interface
```

**Root Cause**:
- Jersey 1.1.5 (released ~2009) was compiled against ASM 3.x
- Jersey's internal class `AnnotationScannerListener$AnnotatedClassVisitor` implements ASM 3.x's `ClassVisitor`
- ASM 9.2 is required for Java 17 bytecode support (project uses Java 17)
- In ASM 9.x, `ClassVisitor` changed from interface to abstract class
- This is a binary incompatibility - Jersey 1.x bytecode expects interface, finds class instead

**Why This Occurs**:
- After first startup failure and restart, the application makes it further in initialization
- Jersey servlet (`HandshakingServlet`) tries to scan for JAX-RS resources using PackagesResourceConfig
- Jersey's resource scanner uses ASM to read bytecode annotations
- When Jersey tries to load its ASM-using classes, it fails due to ASM API mismatch

### Problem #3: Convoluted Deployment Pipeline

**Current Approach**:
1. Build WARs with Maven
2. Deploy to Tomcat
3. Start Tomcat (fails)
4. Stop Tomcat
5. Manually remove JARs from exploded WAR directories
6. Restart Tomcat (fails again with different error)

**Issues**:
- Masks root causes instead of fixing them
- Brittle shell script logic (`rm -fv *jcl*slf4j*.jar`)
- No proper dependency management
- Fails to address fundamental library incompatibilities
- Difficult to maintain and debug

## Proposed Solutions

### Solution A: Downgrade SLF4J + Upgrade Jersey (RECOMMENDED)

**Rationale**: Minimal risk, maximum compatibility

**Changes Required**:

1. **Downgrade SLF4J 2.0.16 → 1.7.36** (Spring 5.x compatible)
   ```xml
   <!-- Parent pom.xml -->
   <dependency>
       <groupId>org.slf4j</groupId>
       <artifactId>slf4j-api</artifactId>
       <version>1.7.36</version>  <!-- was 2.0.16 -->
   </dependency>
   ```

2. **Update Log4j SLF4J bridge to 1.7.x compatible version**
   ```xml
   <!-- Parent pom.xml -->
   <dependency>
       <groupId>org.apache.logging.log4j</groupId>
       <artifactId>log4j-slf4j-impl</artifactId>  <!-- not log4j-slf4j2-impl -->
       <version>2.24.3</version>
   </dependency>
   ```

3. **Upgrade Jersey 1.1.5 → 1.19.4** (last Jersey 1.x release, Java 7+ compatible)
   ```xml
   <!-- treebase-web/pom.xml -->
   <dependency>
       <groupId>com.sun.jersey</groupId>
       <artifactId>jersey-server</artifactId>
       <version>1.19.4</version>  <!-- was 1.1.5 -->
   </dependency>
   <!-- Repeat for jersey-client, jersey-bundle, jersey-multipart -->
   ```

4. **Keep ASM 9.2** - Jersey 1.19.x can work with ASM 5.x/9.x via asm-commons
   ```xml
   <!-- treebase-web/pom.xml - Add -->
   <dependency>
       <groupId>org.ow2.asm</groupId>
       <artifactId>asm-commons</artifactId>
       <version>9.2</version>  <!-- Jersey 1.19.x compatibility -->
   </dependency>
   ```

5. **Simplify deployment workflow**
   - Remove JAR manipulation logic
   - Single Tomcat startup
   - Let Maven dependency management handle conflicts

**Pros**:
- ✅ Fixes both critical errors
- ✅ Minimal code changes (just version numbers)
- ✅ Maintains Spring 5.x compatibility
- ✅ No API changes required
- ✅ Low risk

**Cons**:
- ⚠️ Jersey 1.x is EOL (end-of-life since 2018)
- ⚠️ SLF4J 1.7.x is maintenance mode
- ⚠️ Not future-proof

**Estimated Effort**: 4-8 hours (testing + validation)

### Solution B: Upgrade to Jersey 2.x (FUTURE-PROOF)

**Rationale**: Modern stack, better long-term maintenance

**Changes Required**:

1. **Downgrade SLF4J** (same as Solution A)

2. **Upgrade Jersey 1.x → 2.41**
   ```xml
   <dependency>
       <groupId>org.glassfish.jersey.core</groupId>  <!-- new groupId -->
       <artifactId>jersey-server</artifactId>
       <version>2.41</version>
   </dependency>
   <dependency>
       <groupId>org.glassfish.jersey.containers</groupId>
       <artifactId>jersey-container-servlet</artifactId>
       <version>2.41</version>
   </dependency>
   ```

3. **Update REST API code** (if upgrading to Jersey 2.x)
   - Jersey 2.x uses different package names (`javax.ws.rs.*` → `jakarta.ws.rs.*` or stays with `javax.ws.rs.*` depending on version)
   - Change `PackagesResourceConfig` → `ResourceConfig`
   - Update servlet configuration in web.xml

4. **Remove ASM dependency** - Jersey 2.x doesn't directly expose ASM

**Pros**:
- ✅ Actively maintained
- ✅ Better Java 17 support
- ✅ Modern JAX-RS 2.x features
- ✅ No ASM version conflicts

**Cons**:
- ⚠️ Requires code changes in REST API classes
- ⚠️ Higher risk of breaking changes
- ⚠️ More testing required

**Estimated Effort**: 16-24 hours (code changes + testing)

### Solution C: Full Stack Upgrade (IDEAL but HIGH RISK)

**Rationale**: Modernize entire stack for Java 17

**Changes Required**:

1. **Spring 5.3.26 → 6.x**
   - Requires Jakarta EE (javax.* → jakarta.*)
   - Major refactoring

2. **Jersey 2.x → 3.x** (Jakarta EE)

3. **Keep SLF4J 2.0.16**

4. **Hibernate 5.6.15 → 6.x**

**Pros**:
- ✅ Fully modern stack
- ✅ Best long-term solution
- ✅ Native Java 17 support

**Cons**:
- ❌ Massive refactoring effort
- ❌ javax.* → jakarta.* namespace migration
- ❌ High risk of breaking changes
- ❌ Extensive testing required

**Estimated Effort**: 80-120 hours (major refactoring)

## Recommendation

**Implement Solution A immediately** to fix the deployment failures:
1. Quick fix with minimal risk
2. Gets pipeline working again
3. Buys time for proper modernization

**Plan for Solution B in next quarter**:
1. Scheduled upgrade to Jersey 2.x
2. Proper testing cycle
3. Better long-term maintenance

**Consider Solution C as 2-year roadmap item**:
1. Part of broader Java 17 / Jakarta EE migration
2. Requires dedicated sprint(s)
3. Risk-managed rollout

## Deployment Workflow Simplification

**Current (Problematic)**:
```yaml
- Deploy WARs
- Start Tomcat
- Stop Tomcat
- Remove conflicting JARs manually
- Restart Tomcat
- Check logs for SEVERE errors
```

**Proposed (Clean)**:
```yaml
- Deploy WARs
- Start Tomcat
- Check application health endpoints
```

The key is fixing dependencies in Maven, not patching at deployment time.

## Action Items

1. **Immediate** (This PR):
   - [ ] Downgrade SLF4J 2.0.16 → 1.7.36
   - [ ] Update log4j-slf4j2-impl → log4j-slf4j-impl
   - [ ] Upgrade Jersey 1.1.5 → 1.19.4
   - [ ] Add asm-commons 9.2 dependency
   - [ ] Remove JAR manipulation from workflow
   - [ ] Test deployment

2. **Short-term** (Next sprint):
   - [ ] Create comprehensive dependency matrix
   - [ ] Document all library versions and compatibility
   - [ ] Add dependency vulnerability scanning

3. **Medium-term** (Next quarter):
   - [ ] Plan Jersey 2.x upgrade
   - [ ] Evaluate Spring 6.x migration path
   - [ ] Modernize logging configuration

## References

- Spring 5.3.x SLF4J Compatibility: https://docs.spring.io/spring-framework/reference/5.3/core/beans/annotation-config/classpath-scanning.html
- SLF4J 2.0 Migration: https://www.slf4j.org/faq.html#changesInVersion200
- Jersey 1.x → 2.x Migration: https://jersey.github.io/documentation/latest/migration.html
- ASM Compatibility: https://asm.ow2.io/
