package org.cipres.treebase.web.compat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Compatibility class to bridge Spring 3.x AbstractCommandController to Spring 4.x
 * This class provides a similar API to the removed Spring 3.x AbstractCommandController
 * by extending AbstractController and implementing command binding manually
 */
public abstract class AbstractCommandController extends AbstractController {
    
    private Class<?> commandClass;
    private String commandName = "command";
    private Validator validator;
    
    public void setCommandClass(Class<?> commandClass) {
        this.commandClass = commandClass;
    }
    
    public Class<?> getCommandClass() {
        return commandClass;
    }
    
    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }
    
    public String getCommandName() {
        return commandName;
    }
    
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
    
    public Validator getValidator() {
        return validator;
    }
    
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = createCommand();
        ServletRequestDataBinder binder = createBinder(request, command);
        binder.bind(request);
        org.springframework.validation.BindException errors = new org.springframework.validation.BindException(binder.getBindingResult());
        
        // Validate if validator is set
        if (validator != null) {
            validator.validate(command, errors);
        }
        
        return handle(request, response, command, errors);
    }
    
    protected Object createCommand() throws Exception {
        if (commandClass != null) {
            return commandClass.newInstance();
        }
        return new Object();
    }
    
    protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object command) throws Exception {
        ServletRequestDataBinder binder = new ServletRequestDataBinder(command, getCommandName());
        initBinder(request, binder);
        return binder;
    }
    
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        // Override to customize binder
    }
    
    /**
     * Template method for handling the command.
     * Subclasses must override this method to process the bound command object.
     * This version includes binding errors for validation.
     */
    protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object command, org.springframework.validation.BindException errors) throws Exception {
        // Default implementation calls the 3-parameter version for backwards compatibility
        return handle(request, response, command);
    }
    
    /**
     * Template method for handling the command.
     * Subclasses can override this method to process the bound command object.
     * Override the 4-parameter version if you need access to binding errors.
     */
    protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        throw new UnsupportedOperationException("Subclasses must override either handle(request, response, command) or handle(request, response, command, errors)");
    }
}
