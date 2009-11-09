package org.treebase.oai.web.controller;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.web.servlet.ModelAndView;
import org.treebase.oai.web.command.Identify;



public class OAIPMHControllerTest extends AbstractDependencyInjectionSpringContextTests {

	/**
	 * constructor.
	 * 
	 */ 
	
	VelocityUtil vu;
	 
	private OAIPMHController controller;		
	
    public void setController(OAIPMHController controller) {
		this.controller = controller;
	}

	private Identify identify;    
    
    public void setIdentify(Identify identify) {
		this.identify = identify;
	}
	
	
	class VelocityUtil{
	   VelocityEngine ve;
	   
	   VelocityUtil(){
		   ve = new VelocityEngine();

		   try {
			        ve.init();
		       } catch (Exception e) {
			// TODO Auto-generated catch block
			   logger.info(e.getMessage());
		    } 
	   }
	   
	   String runTemplate(ModelAndView mav) {
		   StringWriter writer=null;
		   Template t=null;
		try {
			t = ve.getTemplate(mav.getViewName());
		
		   VelocityContext context = new VelocityContext();
   		   context.put("model", mav.getModel());
   		   writer= new StringWriter();
           t.merge(context, writer);
          
        } catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
		return writer.toString();
	   }
    }

	@Override
	protected String[] getConfigLocations() {
		return new String[]{"applicationContext.xml","top-servlet.xml"};
	}

	public void setup(){
		vu= new VelocityUtil();
	}
	
	public void testHandle() {
		this.assertEquals(true, true);
	}
}
