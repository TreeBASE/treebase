package org.cipres.treebase.web.compat;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Compatibility class to bridge Spring 3.x CancellableFormController to Spring 4.x
 * This class provides a similar API to the removed Spring 3.x CancellableFormController
 * by extending AbstractController and implementing form handling manually
 */
public abstract class CancellableFormController extends AbstractController {
    
    private String formView;
    private String successView;
    private String cancelView;
    private Class<?> commandClass;
    private String commandName = "command";
    private boolean sessionForm = false;
    
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
    
    public void setCancelView(String cancelView) {
        this.cancelView = cancelView;
    }
    
    public String getCancelView() {
        return cancelView;
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
    
    public void setSessionForm(boolean sessionForm) {
        this.sessionForm = sessionForm;
    }
    
    public boolean isSessionForm() {
        return sessionForm;
    }
    
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (isFormSubmission(request)) {
            Object command = getSessionFormObject(request);
            if (command == null) {
                command = formBackingObject(request);
            }
            ServletRequestDataBinder binder = createBinder(request, command);
            binder.bind(request);
            onBind(request, command);
            BindException errors = new BindException(binder.getBindingResult());
            
            if (isCancelRequest(request)) {
                removeSessionFormObject(request);
                return onCancel(request, response, command);
            }
            
            ModelAndView result = processFormSubmission(request, response, command, errors);
            if (!errors.hasErrors()) {
                removeSessionFormObject(request);
            }
            return result;
        } else {
            Object command = formBackingObject(request);
            storeSessionFormObject(request, command);
            BindException errors = new BindException(command, getCommandName());
            Map<String, Object> refData = referenceData(request);
            return showForm(request, response, errors, refData);
        }
    }
    
    protected Object getSessionFormObject(HttpServletRequest request) {
        String sessionAttrName = getFormSessionAttributeName();
        if (sessionAttrName != null && request.getSession(false) != null) {
            return request.getSession(false).getAttribute(sessionAttrName);
        }
        return null;
    }
    
    protected void storeSessionFormObject(HttpServletRequest request, Object command) {
        String sessionAttrName = getFormSessionAttributeName();
        if (sessionAttrName != null) {
            request.getSession(true).setAttribute(sessionAttrName, command);
        }
    }
    
    protected void removeSessionFormObject(HttpServletRequest request) {
        String sessionAttrName = getFormSessionAttributeName();
        if (sessionAttrName != null && request.getSession(false) != null) {
            request.getSession(false).removeAttribute(sessionAttrName);
        }
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
    
    protected boolean isFormSubmission(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod());
    }
    
    protected boolean isCancelRequest(HttpServletRequest request) {
        return request.getParameter("_cancel") != null;
    }
    
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        Map<String, Object> refData = referenceData(request);
        return showForm(request, response, errors, refData);
    }
    
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors, Map<String, Object> controlModel) throws Exception {
        ModelAndView mav = new ModelAndView(getFormView(), errors.getModel());
        if (controlModel != null) {
            mav.addAllObjects(controlModel);
        }
        return mav;
    }
    
    protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        if (errors.hasErrors()) {
            return showForm(request, response, errors);
        }
        return onSubmit(request, response, command, errors);
    }
    
    protected abstract ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception;
    
    protected ModelAndView onCancel(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        String cancelViewName = getCancelView() != null ? getCancelView() : getSuccessView();
        return new ModelAndView(cancelViewName);
    }
    
    /**
     * Template method for getting reference data to be used in the form view.
     * Subclasses can override this to provide model attributes.
     */
    protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
        return new java.util.HashMap<String, Object>();
    }
    
    /**
     * Template method for creating the form backing object.
     * Subclasses can override this to provide a custom object.
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return createCommand();
    }
    
    /**
     * Hook for binding data from the request. Called after standard binding.
     */
    protected void onBind(HttpServletRequest request, Object command) throws Exception {
        // Override to perform custom binding
    }
    
    /**
     * Template method to get session attribute name for form object.
     * Return null if form object should not be stored in session.
     */
    protected String getFormSessionAttributeName() {
        if (sessionForm) {
            return getCommandName() + "_FORM";
        }
        return null;
    }
}
