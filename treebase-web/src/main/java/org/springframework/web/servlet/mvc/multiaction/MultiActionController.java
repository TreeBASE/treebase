package org.springframework.web.servlet.mvc.multiaction;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Custom MultiActionController for Spring 5.x compatibility.
 * This class provides multi-action controller functionality that was removed from Spring 5.x.
 * 
 * Note: This is a simplified implementation that maintains backward compatibility.
 * In Spring 5.x, the recommended approach is to use @Controller with @RequestMapping.
 */
public class MultiActionController extends AbstractController {

    private MethodNameResolver methodNameResolver;

    /**
     * Set the MethodNameResolver to use for determining the handler method name.
     */
    public void setMethodNameResolver(MethodNameResolver methodNameResolver) {
        this.methodNameResolver = methodNameResolver;
    }

    /**
     * Get the current MethodNameResolver.
     */
    public MethodNameResolver getMethodNameResolver() {
        if (this.methodNameResolver == null) {
            this.methodNameResolver = new ParameterMethodNameResolver();
        }
        return this.methodNameResolver;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        String methodName = getMethodName(request);
        Method method = findMethod(methodName);
        
        if (method == null) {
            throw new NoSuchMethodException("No method found with name: " + methodName);
        }
        
        return (ModelAndView) method.invoke(this, request, response);
    }

    /**
     * Determine the method name from the request using the configured MethodNameResolver.
     */
    protected String getMethodName(HttpServletRequest request) throws NoSuchRequestHandlingMethodException {
        return getMethodNameResolver().getHandlerMethodName(request);
    }

    /**
     * Find a method by name that matches the MultiActionController signature.
     */
    protected Method findMethod(String methodName) {
        try {
            return getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
        } catch (NoSuchMethodException e) {
            // Method not found
            return null;
        }
    }
}
