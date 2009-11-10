package org.treebase.oai.web.controller;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.web.servlet.ModelAndView;
import org.treebase.oai.web.command.Identify;
import org.treebase.oai.web.command.OAIPMHCommand;



public class OAIPMHControllerTest extends AbstractDependencyInjectionSpringContextTests {

	
	
	VelocityUtil vu= new VelocityUtil();; 
	
	//OAIPMHCommand command;
	 
	private OAIPMHController controller;		
	
    public void setController(OAIPMHController controller) {
		this.controller = controller;
	}

	private Identify identify;    
    
    public void setIdentify(Identify identify) {
		this.identify = identify;
	}
	
	// velocity utils for test only 
	class VelocityUtil{
	   VelocityEngine ve;
	   
	   VelocityUtil(){
		   ve = new VelocityEngine();
		   Properties p = new Properties();  
		   p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");  
		   p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");  
		   p.setProperty("file.resource.loader.class", 
				   "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");  

		   try {
			        ve.init(p);
		       } catch (Exception e) {
			// TODO Auto-generated catch block
			   logger.info(e.getMessage());
		    } 
	   }
	   
	   String runTemplate(ModelAndView mav) {
		   StringWriter writer=null;
		   Template t=null;
		try {
			t = ve.getTemplate("\\"+mav.getViewName());
		
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
		return new String[]{"applicationContext.xml","top-servlet.xml",
				"classpath*:applicationContext-dao.xml","classpath*:applicationContext-service.xml"};
	}

	
	public void testIdentify() {
		
		OAIPMHCommand params=new OAIPMHCommand();
		params.setVerb("Identify");
		Map model=new HashMap();
		model.put("identify",identify );		
		ModelAndView mav=controller.Identify(params, model);
		String result=vu.runTemplate(mav);		
		this.assertNotNull(result);
		System.out.print(result);
		
	}
}
