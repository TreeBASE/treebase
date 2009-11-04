package org.treebase.oai.web.controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;
import org.treebase.oai.web.command.Identify;
import org.treebase.oai.web.command.OAIPMHCommand;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.domain.study.StudyService;

public class OAIPMHController extends AbstractCommandController{

	private SubmissionService submissionService;
	private StudyService studyService;
	private Identify identify;
	
	public OAIPMHController(){
		  setCommandClass(OAIPMHCommand.class);
		  setCommandName("requestParams");
		 }
	
	public SubmissionService getSubmissionService() {
		return submissionService;
	}

	public void setSubmissionService(SubmissionService submissionService) {
		this.submissionService = submissionService;
	}

	public StudyService getStudyService() {
		return studyService;
	}

	public void setStudyService(StudyService studyService) {
		this.studyService = studyService;
	}

	public Identify getIdentify() {
		return identify;
	}

	public void setIdentify(Identify identify) {
		this.identify = identify;
	}
	
	
	
	@Override
	protected ModelAndView handle(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		// TODO Auto-generated method stub
		
		 OAIPMHCommand params = (OAIPMHCommand) command;
		 
		 Map  model=errors.getModel();
		 model.put("requestParams", params);
		 model.put("identify", this.identify);
		 
         String responsDate;
		 
		 Method method=null;
		 
		 
		 try{
			 method=this.getClass().getMethod(params.getVerb(), new Class[]{HttpServletRequest.class, HttpServletResponse.class, OAIPMHCommand.class, Map.class});
		 }catch(NoSuchMethodException e){
			 
			return new ModelAndView("badVerb.vm",model);
		 }
		 
		 	return (ModelAndView) method.invoke(this, request, response, params, model);
		}

		ModelAndView ListRecoed(HttpServletRequest request, HttpServletResponse response, OAIPMHCommand params, Map model){
			
			return (new ModelAndView(params.getMetadataPrefix()+"_ListRecoed.vm",model));
		
		}
    		

		ModelAndView ListIdentifiers(HttpServletRequest request, HttpServletResponse response, OAIPMHCommand params, Map model){
			
			return (new ModelAndView(params.getMetadataPrefix()+"_ListIdentifiers.vm",model));
	
		}

		ModelAndView GetRecord(HttpServletRequest request, HttpServletResponse response, OAIPMHCommand params, Map model){
			
			return (new ModelAndView(params.getMetadataPrefix()+"_GetRecord.vm",model));
	
		}

		ModelAndView Identify(HttpServletRequest request, HttpServletResponse response, OAIPMHCommand params, Map model){
	
			return (new ModelAndView("Identify.vm",model));
	
		}
		
		ModelAndView ListSet(HttpServletRequest request, HttpServletResponse response, OAIPMHCommand params, Map model){
			
			 return (new ModelAndView("ListSet.vm",model));
			
		}
		
		ModelAndView ListMetadataFormats(HttpServletRequest request, HttpServletResponse response, OAIPMHCommand params, Map model){
		
			return (new ModelAndView("ListMetadataFormats.vm",model));
		
		}
		
}
