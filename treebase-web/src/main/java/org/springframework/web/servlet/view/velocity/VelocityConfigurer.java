package org.springframework.web.servlet.view.velocity;

import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Custom VelocityConfigurer for Spring 5.x compatibility.
 * This class provides Velocity template engine configuration that was removed from Spring 5.x.
 */
public class VelocityConfigurer implements InitializingBean, ResourceLoaderAware, ServletContextAware {

    private VelocityEngine velocityEngine;
    private String resourceLoaderPath;
    private Properties velocityProperties;
    private ResourceLoader resourceLoader;
    private ServletContext servletContext;

    public void setResourceLoaderPath(String resourceLoaderPath) {
        this.resourceLoaderPath = resourceLoaderPath;
    }

    public void setVelocityProperties(Properties velocityProperties) {
        this.velocityProperties = velocityProperties;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public VelocityEngine getVelocityEngine() {
        return this.velocityEngine;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.velocityEngine = createVelocityEngine();
    }

    protected VelocityEngine createVelocityEngine() throws Exception {
        VelocityEngine velocityEngine = new VelocityEngine();
        
        Properties props = new Properties();
        
        if (resourceLoaderPath != null) {
            // Configure file resource loader for webapp paths
            props.setProperty("resource.loaders", "file");
            props.setProperty("resource.loader.file.class",
                "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
            
            String path = resourceLoaderPath;
            if (servletContext != null) {
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
                String realPath = servletContext.getRealPath(path);
                // Handle case where getRealPath returns null (e.g., running from WAR)
                if (realPath != null) {
                    path = realPath;
                } else {
                    // Fallback to classpath loader if real path cannot be determined
                    props.setProperty("resource.loaders", "classpath");
                    props.setProperty("resource.loader.classpath.class",
                        "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
                }
            }
            
            if (props.getProperty("resource.loaders").equals("file")) {
                props.setProperty("resource.loader.file.path", path);
            }
        } else {
            // Set default classpath loader when no path is specified
            props.setProperty("resource.loaders", "classpath");
            props.setProperty("resource.loader.classpath.class", 
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        }
        
        // Add any custom properties
        if (velocityProperties != null) {
            props.putAll(velocityProperties);
        }
        
        velocityEngine.init(props);
        return velocityEngine;
    }
}
