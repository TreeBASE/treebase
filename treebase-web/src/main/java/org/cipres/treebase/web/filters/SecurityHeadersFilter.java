package org.cipres.treebase.web.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Security filter to add HTTP security headers to all responses.
 * This helps protect against XSS, clickjacking, and other common web vulnerabilities.
 */
public class SecurityHeadersFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (response instanceof HttpServletResponse) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            
            // Prevent clickjacking attacks
            httpResponse.setHeader("X-Frame-Options", "SAMEORIGIN");
            
            // Prevent MIME-sniffing
            httpResponse.setHeader("X-Content-Type-Options", "nosniff");
            
            // Enable XSS protection in older browsers
            httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
            
            // Content Security Policy - allow same origin and specific trusted sources
            // This is a basic policy that can be tightened further
            httpResponse.setHeader("Content-Security-Policy", 
                "default-src 'self' 'unsafe-inline' 'unsafe-eval' https:; " +
                "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://www.google-analytics.com; " +
                "style-src 'self' 'unsafe-inline'; " +
                "img-src 'self' data: https:; " +
                "font-src 'self' data:;");
            
            // Prevent caching of sensitive pages
            httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            httpResponse.setHeader("Pragma", "no-cache");
            httpResponse.setHeader("Expires", "0");
        }
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}
