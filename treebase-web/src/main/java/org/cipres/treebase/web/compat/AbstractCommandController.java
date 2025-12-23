package org.cipres.treebase.web.compat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = createCommand();
        ServletRequestDataBinder binder = createBinder(request, command);
        binder.bind(request);
        
        return handle(request, response, command);
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
    
    protected abstract ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception;
}
