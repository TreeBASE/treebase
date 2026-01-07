# DWR (Direct Web Remoting) Integration

This document describes the DWR integration in TreeBASE and its Spring 5 compatibility implementation.

## Overview

DWR (Direct Web Remoting) is a Java library that enables AJAX functionality by allowing JavaScript in the browser to call Java methods on the server. Remote calls appear as local JavaScript function calls.

## DWR Usage in TreeBASE

TreeBASE uses DWR for interactive features:

1. **Person Email Autocomplete** - `RemotePersonService.findCompleteEmailAddress()`
2. **Software Name Autocomplete** - `RemoteSoftwareNameService.findCompleteSoftwareName()`
3. **Journal Name Autocomplete** - `RemoteJournalNameService.findCompleteJournalName()`
4. **Algorithm Suggestions** - `RemoteUniqueOtherAlgorithmService.findAllUniqueOtherAlgorithmDescriptions()`
5. **File Upload Progress** - `RemoteAjaxProgressListener.getStatus()`

## Spring 5 Compatibility

### The Challenge

DWR 3.0.2-RELEASE was released before Spring 5.0 (September 2017) and uses Spring APIs that changed in Spring 5.x. The main issue is how DWR's `DwrSpringServlet` retrieves the Spring `WebApplicationContext`.

### The Solution

A Spring 5-compatible wrapper servlet was created: `org.cipres.treebase.web.dwr.Spring5CompatibleDwrServlet`

This servlet:
- Extends `org.directwebremoting.spring.DwrSpringServlet`
- Uses Spring 5-compatible methods to retrieve the `WebApplicationContext`
- Ensures proper initialization order

## Configuration

### Maven Dependency

In `treebase-web/pom.xml`:

```xml
<dependency>
    <groupId>org.directwebremoting</groupId>
    <artifactId>dwr</artifactId>
    <version>3.0.2-RELEASE</version>
</dependency>
```

### Servlet Configuration

In `WEB-INF/web.xml`:

```xml
<servlet>
    <servlet-name>dwr</servlet-name>
    <servlet-class>org.cipres.treebase.web.dwr.Spring5CompatibleDwrServlet</servlet-class>
    <init-param>
        <param-name>allowGetForSafariButMakeForgeryEasier</param-name>
        <param-value>true</param-value>
    </init-param>
    <init-param>
        <param-name>debug</param-name>
        <param-value>true</param-value>
    </init-param>
</servlet>

<servlet-mapping>
    <servlet-name>dwr</servlet-name>
    <url-pattern>/dwr/*</url-pattern>
</servlet-mapping>
```

### Spring Bean Configuration

In `WEB-INF/applicationContext.xml`:

```xml
<beans xmlns:dwr="http://www.directwebremoting.org/schema/spring-dwr"
       xsi:schemaLocation="...
       http://www.directwebremoting.org/schema/spring-dwr
       http://www.directwebremoting.org/schema/spring-dwr-3.0.xsd">

    <bean id="remotePersonService" class="org.cipres.treebase.dao.admin.PersonDAO">
        <dwr:remote javascript="RemotePersonService">
            <dwr:include method="findCompleteEmailAddress" />
        </dwr:remote>
        <property name="sessionFactory"><ref bean="sessionFactory"></ref></property>
    </bean>
    
    <!-- Additional remote services configured similarly -->
</beans>
```

### DWR Configuration

In `WEB-INF/dwr.xml`:

```xml
<!DOCTYPE dwr PUBLIC
    "-//GetAhead Limited//DTD Direct Web Remoting 3.0//EN"
    "http://getahead.org/dwr/dwr30.dtd">
<dwr>
    <allow>
        <!-- Additional DWR configurations can be added here -->
    </allow>
</dwr>
```

## JavaScript Usage

In JSP templates:

```javascript
// Include DWR JavaScript libraries
<script src="<c:url value='/dwr/interface/RemotePersonService.js'/>"></script>
<script src="<c:url value='/dwr/engine.js'/>"></script>
<script src="<c:url value='/dwr/util.js'/>"></script>

// Call remote methods
function updateList(autocompleter, token) {
    RemotePersonService.findCompleteEmailAddress(token, function(data) {
        autocompleter.setChoices(data)
    });
}
```

## Testing DWR

To test if DWR is working:

1. Start the application
2. Navigate to: `http://your-server/treebase-web/dwr/`
3. You should see the DWR index page listing all exposed methods
4. Click on any service to test it interactively

## Troubleshooting

### No Spring WebApplicationContext Found

**Cause:** Spring context not initialized before DWR

**Solution:** Ensure `StartupListener` (extends `ContextLoaderListener`) is configured in `web.xml` and Spring context files load properly.

### DWR JavaScript Errors

**Causes:**
- URL pattern `/dwr/*` not correctly mapped
- JavaScript includes in wrong order

**Solutions:**
- Verify servlet mapping in `web.xml`
- Check browser console for detailed errors
- Enable DWR debug mode during development

### Method Not Found Errors

**Causes:**
- Method not public in DAO class
- Missing or incorrect `<dwr:include>` configuration
- Bean not properly injected

**Solutions:**
- Verify method is public
- Check `<dwr:include method="methodName" />` configuration
- Ensure bean has required dependencies (e.g., sessionFactory)

## References

- [DWR Documentation](http://directwebremoting.org/dwr/index.html)
- [DWR 3.0 Documentation](http://directwebremoting.org/dwr/documentation/index.html)
- [Spring Framework 5 Documentation](https://docs.spring.io/spring-framework/docs/5.3.x/reference/html/)
