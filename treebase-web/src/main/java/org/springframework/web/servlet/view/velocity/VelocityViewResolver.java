package org.springframework.web.servlet.view.velocity;

import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

/**
 * Custom VelocityViewResolver for Spring 5.x compatibility.
 * This class provides Velocity view resolution that was removed from Spring 5.x.
 */
public class VelocityViewResolver implements ViewResolver, InitializingBean {

    private VelocityEngine velocityEngine;
    private String prefix = "";
    private String suffix = ".vm";
    private String contentType = "text/html;charset=UTF-8";
    private String encoding = "UTF-8";

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public void setPrefix(String prefix) {
        this.prefix = (prefix != null ? prefix : "");
    }

    public void setSuffix(String suffix) {
        this.suffix = (suffix != null ? suffix : "");
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.velocityEngine == null) {
            throw new IllegalArgumentException("Property 'velocityEngine' is required");
        }
    }

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        return new VelocityView(prefix + viewName + suffix);
    }

    private class VelocityView implements View {
        private final String templatePath;

        public VelocityView(String templatePath) {
            this.templatePath = templatePath;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
                throws Exception {
            
            response.setContentType(getContentType());

            Context velocityContext = new VelocityContext();
            
            // Add model attributes to Velocity context
            if (model != null) {
                for (Map.Entry<String, ?> entry : model.entrySet()) {
                    velocityContext.put(entry.getKey(), entry.getValue());
                }
            }

            // Add request, response, and session to context for compatibility
            velocityContext.put("request", request);
            velocityContext.put("response", response);
            if (request.getSession(false) != null) {
                velocityContext.put("session", request.getSession());
            }

            // Merge template
            StringWriter writer = new StringWriter();
            velocityEngine.mergeTemplate(templatePath, encoding, velocityContext, writer);
            
            response.getWriter().write(writer.toString());
        }
    }
}
