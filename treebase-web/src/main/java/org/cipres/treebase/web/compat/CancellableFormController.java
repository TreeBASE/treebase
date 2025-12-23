package org.cipres.treebase.web.compat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * Compatibility class for Spring 4.x migration.
 * Provides similar functionality to Spring 3.x CancellableFormController.
 * 
 * Note: SimpleFormController is also deprecated in Spring 4.x, but this provides
 * a migration path. Controllers should be migrated to @Controller with @RequestMapping.
 */
@SuppressWarnings("deprecation")
public abstract class CancellableFormController extends SimpleFormController {
    
    /**
     * Handle cancel action - override to provide custom cancel behavior
     */
    protected ModelAndView onCancel(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        return new ModelAndView(getSuccessView());
    }
    
    /**
     * Override to detect cancel action
     */
    @Override
    protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        if (request.getParameter("_cancel") != null || isCancelRequest(request)) {
            return onCancel(request, response, command);
        }
        return super.processFormSubmission(request, response, command, errors);
    }
    
    /**
     * Check if the request is a cancel request
     */
    protected boolean isCancelRequest(HttpServletRequest request) {
        return request.getParameter("_cancel") != null;
    }
}
