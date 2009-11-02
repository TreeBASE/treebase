package org.treebase.oai.web.controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;
import org.treebase.oai.web.command.OAIPMHCommand;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.domain.study.StudyService;

public class OAIPMHController extends AbstractCommandController{
   
	private SubmissionService submissionService;
	private StudyService studyService;
	
	public OAIPMHController(){
		  setCommandClass(OAIPMHCommand.class);
		  setCommandName("params");
		 }
	
	@Override
	protected ModelAndView handle(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		// TODO Auto-generated method stub
		
		 OAIPMHCommand params = (OAIPMHCommand) command;
		 
		 Map  model=errors.getModel();
		 model.put("params", params);
		 
		 Method method=null;
		 
		 
		 try{
			 method=this.getClass().getMethod(params.getVerb(), new Class[]{HttpServletRequest.class, HttpServletResponse.class, Map.class});
		 }catch(NoSuchMethodException e){
			 
			return new ModelAndView("badVerb.vm",model);
		 }
		 
		 	return (ModelAndView) method.invoke(this, request, response, model);
		}

		ModelAndView ListRecoed(HttpServletRequest request, HttpServletResponse response, Map model){
			
			return (new ModelAndView(metadataPrefix+"_ListRecoed.vm",model));
		
		}
    
		

		ModelAndView ListIdentifiers(HttpServletRequest request, HttpServletResponse response, Map model){
			metadataPrefix
			return (new ModelAndView(metadataPrefix+"_ListIdentifiers.vm",model));
	
		}

		ModelAndView GetRecord(HttpServletRequest request, HttpServletResponse response, Map model){
			metadataPrefix
			return (new ModelAndView(metadataPrefix+"_GetRecord.vm",model));
	
		}

		ModelAndView Identify(HttpServletRequest request, HttpServletResponse response, Map model){
	
			return (new ModelAndView("Identify.vm",model));
	
		}
		
		ModelAndView ListSet(HttpServletRequest request, HttpServletResponse response, Map model){
			
			 return (new ModelAndView("ListSet.vm",model));
			
		}
		
		ModelAndView ListMetadataFormats(HttpServletRequest request, HttpServletResponse response, Map model){
		
			return (new ModelAndView("ListMetadataFormats.vm",model));
		
		}
}
