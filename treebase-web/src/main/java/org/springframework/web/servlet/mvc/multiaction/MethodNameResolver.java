package org.springframework.web.servlet.mvc.multiaction;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface for method name resolvers used by MultiActionController.
 * This interface was removed in Spring 5.x but is recreated here for backward compatibility.
 */
public interface MethodNameResolver {
    
    /**
     * Return the method name for the given request.
     * 
     * @param request the current HTTP request
     * @return the name of the method to invoke
     * @throws NoSuchRequestHandlingMethodException if no method name could be determined
     */
    String getHandlerMethodName(HttpServletRequest request) throws NoSuchRequestHandlingMethodException;
}
