package org.cipres.treebase.web.compat;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Compatibility class to bridge Spring 3.x AbstractWizardFormController to Spring 4.x
 * This class provides a similar API to the removed Spring 3.x AbstractWizardFormController
 * by extending AbstractController and implementing wizard form handling manually
 */
public abstract class AbstractWizardFormController extends AbstractController {
    
    private static final String PARAM_PAGE = "_page";
    private static final String PARAM_TARGET = "_target";
    private static final String PARAM_FINISH = "_finish";
    private static final String PARAM_CANCEL = "_cancel";
    
    private String[] pages;
    private Class<?> commandClass;
    private String commandName = "command";
    private String formView;
    private String successView;
    
    public void setPages(String[] pages) {
        this.pages = pages;
    }
    
    public String[] getPages() {
        return pages;
    }
    
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
    
    public void setFormView(String formView) {
        this.formView = formView;
    }
    
    public String getFormView() {
        return formView;
    }
    
    public void setSuccessView(String successView) {
        this.successView = successView;
    }
    
    public String getSuccessView() {
        return successView;
    }
    
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object command = createCommand();
        ServletRequestDataBinder binder = createBinder(request, command);
        binder.bind(request);
        BindException errors = new BindException(binder.getBindingResult());
        
        int currentPage = getCurrentPage(request);
        
        // Handle cancel
        if (request.getParameter(PARAM_CANCEL) != null) {
            return processCancel(request, response, command, errors);
        }
        
        // Handle finish
        if (request.getParameter(PARAM_FINISH) != null) {
            validatePage(command, errors, currentPage);
            if (!errors.hasErrors()) {
                return processFinish(request, response, command, errors);
            }
        }
        
        // Determine target page
        int targetPage = getTargetPage(request, currentPage);
        
        // Validate current page if moving forward
        if (targetPage > currentPage) {
            validatePage(command, errors, currentPage);
            if (errors.hasErrors()) {
                return showPage(request, response, errors, currentPage);
            }
        }
        
        // Process page change
        postProcessPage(request, command, errors, currentPage);
        
        // Show target page
        return showPage(request, response, errors, targetPage);
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
    
    protected int getCurrentPage(HttpServletRequest request) {
        String pageParam = request.getParameter(PARAM_PAGE);
        if (pageParam != null) {
            try {
                return Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        return 0;
    }
    
    protected int getTargetPage(HttpServletRequest request, int currentPage) {
        // Check for explicit target page
        if (pages != null) {
            for (int i = 0; i < pages.length; i++) {
                if (request.getParameter(PARAM_TARGET + i) != null) {
                    return i;
                }
            }
        }
        // Default to next page
        return currentPage + 1;
    }
    
    protected ModelAndView showPage(HttpServletRequest request, HttpServletResponse response, BindException errors, int page) throws Exception {
        if (pages != null && page >= 0 && page < pages.length) {
            Map<String, Object> model = referenceData(request, page);
            if (model == null) {
                model = new HashMap<String, Object>();
            }
            model.put(PARAM_PAGE, page);
            model.put(getCommandName(), errors.getModel());
            return new ModelAndView(pages[page], model);
        }
        return new ModelAndView(formView != null ? formView : "");
    }
    
    protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        // Override in subclasses
    }
    
    protected void validatePage(Object command, Errors errors, int page) {
        // Override in subclasses
    }
    
    protected abstract ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception;
    
    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return new ModelAndView(successView != null ? successView : "");
    }
    
    protected Map<String, Object> referenceData(HttpServletRequest request, int page) throws Exception {
        return referenceData(request);
    }
    
    protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
        return null;
    }
}
