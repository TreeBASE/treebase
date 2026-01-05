package org.cipres.treebase.web.dwr;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.directwebremoting.spring.DwrSpringServlet;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Spring 5-compatible wrapper for DwrSpringServlet.
 * 
 * This servlet ensures proper initialization of DWR with Spring 5.x by
 * using the correct WebApplicationContext retrieval methods that are
 * compatible with Spring 5 API changes.
 * 
 * The main issue with DWR 3.0.2-RELEASE and Spring 5 is that DWR may use
 * deprecated methods to access the Spring application context. This wrapper
 * ensures the context is properly retrieved and set before DWR initialization.
 */
public class Spring5CompatibleDwrServlet extends DwrSpringServlet {
    
    private static final long serialVersionUID = 1L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        // Ensure Spring context is available before initializing DWR
        WebApplicationContext webApplicationContext = 
            WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        
        if (webApplicationContext == null) {
            throw new ServletException("No Spring WebApplicationContext found - " +
                "cannot initialize DWR. Check that Spring ContextLoaderListener is properly configured.");
        }
        
        // Initialize the parent DwrSpringServlet with proper Spring 5 context
        super.init(config);
    }
}
