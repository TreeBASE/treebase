package org.springframework.web.servlet.mvc.multiaction;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementation of MethodNameResolver that determines the method name from a request parameter.
 * This class was removed in Spring 5.x but is recreated here for backward compatibility.
 */
public class ParameterMethodNameResolver implements MethodNameResolver {
    
    public static final String DEFAULT_PARAM_NAME = "action";
    
    private String paramName = DEFAULT_PARAM_NAME;
    private String defaultMethodName;
    
    /**
     * Set the name of the parameter that contains the method name.
     * Default is "action".
     */
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
    
    /**
     * Set the default method name to use when no parameter is specified.
     */
    public void setDefaultMethodName(String defaultMethodName) {
        this.defaultMethodName = defaultMethodName;
    }
    
    @Override
    public String getHandlerMethodName(HttpServletRequest request) throws NoSuchRequestHandlingMethodException {
        String methodName = request.getParameter(this.paramName);
        
        if (methodName == null || methodName.trim().isEmpty()) {
            if (this.defaultMethodName != null) {
                return this.defaultMethodName;
            }
            throw new NoSuchRequestHandlingMethodException(
                "No method name found in request parameter '" + this.paramName + "'", "");
        }
        
        return methodName;
    }
}
