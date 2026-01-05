# DWR (Direct Web Remoting) Integration with Spring 5

## Overview

This document describes the DWR integration in TreeBASE and how it has been made compatible with Spring 5.

## What is DWR?

DWR (Direct Web Remoting) is a Java library that enables AJAX functionality by allowing JavaScript in a web browser to call Java methods running on a web server, making the remote call appear like a local JavaScript function call.

## DWR in TreeBASE

TreeBASE uses DWR to provide autocomplete and AJAX progress tracking functionality for:

1. **Person Email Autocomplete** - `RemotePersonService.findCompleteEmailAddress()`
2. **Software Name Autocomplete** - `RemoteSoftwareNameService.findCompleteSoftwareName()`
3. **Journal Name Autocomplete** - `RemoteJournalNameService.findCompleteJournalName()`
4. **Algorithm Suggestions** - `RemoteUniqueOtherAlgorithmService.findAllUniqueOtherAlgorithmDescriptions()`
5. **File Upload Progress** - `RemoteAjaxProgressListener.getStatus()`

## Spring 5 Compatibility

### The Challenge

DWR 3.0.2-RELEASE was released before Spring 5.0 (September 2017) and uses some Spring APIs that were changed in Spring 5.x. The main issue is in how DWR's `DwrSpringServlet` retrieves the Spring `WebApplicationContext`.

### The Solution

We created a Spring 5-compatible wrapper servlet: `org.cipres.treebase.web.dwr.Spring5CompatibleDwrServlet`

This servlet:
- Extends `org.directwebremoting.spring.DwrSpringServlet`
- Uses Spring 5-compatible methods to retrieve the `WebApplicationContext`
- Ensures proper initialization order between Spring and DWR

### Configuration

#### 1. Maven Dependency (treebase-web/pom.xml)
```xml
<dependency>
    <groupId>org.directwebremoting</groupId>
    <artifactId>dwr</artifactId>
    <version>3.0.2-RELEASE</version>
</dependency>
```

#### 2. Servlet Configuration (WEB-INF/web.xml)
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

#### 3. Spring Bean Configuration (WEB-INF/applicationContext.xml)
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

#### 4. DWR Configuration (WEB-INF/dwr.xml)
The dwr.xml file uses the DWR 3.0 DTD:
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

In JSP templates, DWR is used through generated JavaScript interfaces:

```javascript
// Include DWR JavaScript libraries
<script type="text/javascript" src="<c:url value='/dwr/interface/RemotePersonService.js'/>"></script>
<script type="text/javascript" src="<c:url value='/dwr/engine.js'/>"></script>
<script type="text/javascript" src="<c:url value='/dwr/util.js'/>"></script>

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

### Common Issues

1. **"No Spring WebApplicationContext found"**
   - Ensure `StartupListener` (which extends `ContextLoaderListener`) is configured in web.xml
   - Check that Spring context files are properly loaded

2. **DWR JavaScript errors**
   - Verify that `/dwr/*` URL pattern is correctly mapped
   - Check browser console for detailed error messages
   - Ensure DWR debug mode is enabled during development

3. **Method not found errors**
   - Verify the method is public in the DAO class
   - Check that `<dwr:include method="methodName" />` is correctly configured
   - Ensure the bean is properly injected with sessionFactory if needed

## References

- DWR Documentation: http://directwebremoting.org/dwr/index.html
- DWR 3.0 Documentation: http://directwebremoting.org/dwr/documentation/index.html
- Spring Framework 5 Documentation: https://docs.spring.io/spring-framework/docs/5.3.x/reference/html/
