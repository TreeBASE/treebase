package org.springframework.web.servlet.mvc.multiaction;

/**
 * Exception thrown when no handler method can be found for a request.
 * This exception was removed in Spring 5.x but is recreated here for backward compatibility.
 */
public class NoSuchRequestHandlingMethodException extends Exception {
    
    private final String methodName;
    
    public NoSuchRequestHandlingMethodException(String methodName) {
        super("No handler method '" + methodName + "' found");
        this.methodName = methodName;
    }
    
    public NoSuchRequestHandlingMethodException(String message, String methodName) {
        super(message);
        this.methodName = methodName;
    }
    
    public String getMethodName() {
        return methodName;
    }
}
