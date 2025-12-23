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
        
        // Determine loader type and path
        String loaderType = "classpath";
        String loaderClass = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";
        String loaderPath = null;
        
        if (resourceLoaderPath != null) {
            String path = resourceLoaderPath;
            if (servletContext != null) {
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
                String realPath = servletContext.getRealPath(path);
                // Use file loader if real path is available
                if (realPath != null) {
                    loaderType = "file";
                    loaderClass = "org.apache.velocity.runtime.resource.loader.FileResourceLoader";
                    loaderPath = realPath;
                }
            }
        }
        
        // Set loader configuration
        props.setProperty("resource.loaders", loaderType);
        props.setProperty("resource.loader." + loaderType + ".class", loaderClass);
        if (loaderPath != null) {
            props.setProperty("resource.loader." + loaderType + ".path", loaderPath);
        }
        
        // Add any custom properties
        if (velocityProperties != null) {
            props.putAll(velocityProperties);
        }
        
        velocityEngine.init(props);
        return velocityEngine;
    }
}
