package org.cipres.treebase.web.viewresolver;

import java.util.List;
import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

public class ChainableVelocityViewResolver implements ViewResolver {
	
	private VelocityViewResolver chainableResolver;
    private List<String> views;
    
    public void setChainableResolver(VelocityViewResolver chainableResolver) {
		this.chainableResolver = chainableResolver;
	}

	public void setViews(List<String> views) {
		this.views = views;
	}

    public View resolveViewName(String viewName, Locale locale) throws Exception {    

        if(views.contains(viewName))

            return chainableResolver.resolveViewName(viewName, locale);

       return null;

    }

}
