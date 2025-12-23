package org.cipres.treebase.web.compat;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;

/**
 * Compatibility class for Spring 4.x migration.
 * Provides similar functionality to Spring 3.x AbstractWizardFormController.
 * 
 * Note: AbstractFormController is also deprecated in Spring 4.x, but this provides
 * a migration path. Controllers should be migrated to @Controller with @RequestMapping.
 */
@SuppressWarnings("deprecation")
public abstract class AbstractWizardFormController extends AbstractFormController {
    
    private static final String PARAM_PAGE = "_page";
    private static final String PARAM_TARGET = "_target";
    private static final String PARAM_FINISH = "_finish";
    private static final String PARAM_CANCEL = "_cancel";
    
    private String[] pages;
    private boolean allowDirtyBack = true;
    private boolean allowDirtyForward = true;
    
    public AbstractWizardFormController() {
        setSessionForm(true);
    }
    
    /**
     * Set the wizard pages
     */
    public void setPages(String[] pages) {
        this.pages = pages;
    }
    
    public void setAllowDirtyBack(boolean allowDirtyBack) {
        this.allowDirtyBack = allowDirtyBack;
    }
    
    public void setAllowDirtyForward(boolean allowDirtyForward) {
        this.allowDirtyForward = allowDirtyForward;
    }
    
    /**
     * Main form handling method
     */
    @Override
    protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        int currentPage = getCurrentPage(request);
        
        // Handle cancel
        if (request.getParameter(PARAM_CANCEL) != null) {
            return processCancel(request, response, command, errors);
        }
        
        // Handle finish
        if (request.getParameter(PARAM_FINISH) != null) {
            return processFinish(request, response, command, errors);
        }
        
        // Determine target page
        int targetPage = getTargetPage(request, currentPage);
        
        // Validate current page if moving forward
        if (targetPage > currentPage && !allowDirtyForward) {
            validatePage(command, errors, currentPage);
            if (errors.hasErrors()) {
                return showForm(request, response, errors);
            }
        }
        
        // Process page change
        postProcessPage(request, command, errors, currentPage);
        
        // Show target page
        return showPage(request, response, errors, targetPage);
    }
    
    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        return showPage(request, response, errors, getCurrentPage(request));
    }
    
    /**
     * Get current page number from request
     */
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
    
    /**
     * Get target page number from request
     */
    protected int getTargetPage(HttpServletRequest request, int currentPage) {
        // Check for explicit target page
        for (int i = 0; i < getPageCount(); i++) {
            if (request.getParameter(PARAM_TARGET + i) != null) {
                return i;
            }
        }
        // Default to next page
        return currentPage + 1;
    }
    
    /**
     * Get the number of pages
     */
    protected int getPageCount() {
        return pages != null ? pages.length : 0;
    }
    
    /**
     * Show a specific page
     */
    protected ModelAndView showPage(HttpServletRequest request, HttpServletResponse response, BindException errors, int page) throws Exception {
        if (pages != null && page >= 0 && page < pages.length) {
            Map<String, Object> model = referenceData(request, page);
            if (model == null) {
                model = new java.util.HashMap<String, Object>();
            }
            model.put(PARAM_PAGE, page);
            return new ModelAndView(pages[page], getCommandName(), errors.getModel());
        }
        return new ModelAndView(getFormView());
    }
    
    /**
     * Post-process a page after submission
     */
    protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        // Override in subclasses
    }
    
    /**
     * Validate a specific page
     */
    protected void validatePage(Object command, Errors errors, int page) {
        validatePage(command, errors, page, true);
    }
    
    /**
     * Validate a specific page with option to finish
     */
    protected void validatePage(Object command, Errors errors, int page, boolean finish) {
        // Override in subclasses
    }
    
    /**
     * Process wizard finish
     */
    protected abstract ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception;
    
    /**
     * Process wizard cancel
     */
    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return new ModelAndView(getSuccessView());
    }
    
    /**
     * Get reference data for a page
     */
    protected Map<String, Object> referenceData(HttpServletRequest request, int page) throws Exception {
        return referenceData(request);
    }
    
    /**
     * Get reference data
     */
    protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
        return null;
    }
}
