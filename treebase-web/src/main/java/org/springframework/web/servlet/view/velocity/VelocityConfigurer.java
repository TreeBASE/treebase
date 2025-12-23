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
        
        // Set default properties - use string literals instead of constants
        props.setProperty("resource.loaders", "webapp");
        props.setProperty("resource.loader.webapp.class", 
            "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        
        if (resourceLoaderPath != null) {
            // Configure file resource loader for webapp paths
            props.setProperty("resource.loaders", "file");
            props.setProperty("resource.loader.file.class",
                "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
            
            String path = resourceLoaderPath;
            if (servletContext != null && !path.startsWith("/")) {
                path = "/" + path;
            }
            if (servletContext != null) {
                path = servletContext.getRealPath(path);
            }
            props.setProperty("resource.loader.file.path", path);
        }
        
        // Add any custom properties
        if (velocityProperties != null) {
            props.putAll(velocityProperties);
        }
        
        velocityEngine.init(props);
        return velocityEngine;
    }
}
