package org.treebase.oai.web.controller;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;
import org.treebase.oai.web.command.Identify;
import org.treebase.oai.web.command.OAIPMHCommand;
import org.treebase.oai.web.util.ParamsUtil;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.domain.study.StudyService;
/**
 * OAIPMHController.java
 * 
 * Created on Nov. 1, 2009
 * 
 * @author Youjun Guo
 * 
 */
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
		 
		 
		 Method method=null;
		 
		 if(ParamsUtil.badMetadataPrefix(params))
			 
			 return new ModelAndView("cannotDisseminateFormat.vm",model);
		 
		 try{
			 method=this.getClass().getMethod(params.getVerb(), new Class[]{HttpServletRequest.class, HttpServletResponse.class, OAIPMHCommand.class, Map.class});
		 }catch(NoSuchMethodException nsme){
			 
			return new ModelAndView("badVerb.vm",model);
		 }catch(NullPointerException e){
			 return (new ModelAndView("badArgument.vm",model));
		 }
		 
		 return (ModelAndView) method.invoke(this, request, response, params, model);
	
	    }

		ModelAndView ListRecoed(HttpServletRequest request, HttpServletResponse response, OAIPMHCommand params, Map model){
			
			List<Submission> list=null;
			model.put("recodeList", list);;
			return (new ModelAndView(params.getMetadataPrefix()+"_ListRecoed.vm",model));
		
		}
    		

		ModelAndView ListIdentifiers(HttpServletRequest request, HttpServletResponse response, OAIPMHCommand params, Map model){
			
			List<Submission> list=null;
			model.put("recodeList", list);
			return (new ModelAndView(params.getMetadataPrefix()+"_ListIdentifiers.vm",model));
	
		}

		ModelAndView GetRecord(HttpServletRequest request, HttpServletResponse response, OAIPMHCommand params, Map model){
			
			Submission submission = null;  
			
			try{
				 long id = ParamsUtil.parseID(params);
			     submission = studyService.findByID(id).getSubmission();
			}catch(NumberFormatException nfe){
				return (new ModelAndView("badArgument.vm",model));
			}
			catch (NullPointerException e){
				
				return (new ModelAndView("idDoesNotExist.vm",model));
			}
			
			model.put("record", submission);			
			return (new ModelAndView(params.getMetadataPrefix()+"_GetRecord.vm",model));
	
		}

		ModelAndView Identify(HttpServletRequest request, HttpServletResponse response, OAIPMHCommand params, Map model){
	
			
			return (new ModelAndView("Identify.vm",model));
	
		}
		
		ModelAndView ListSet(HttpServletRequest request, HttpServletResponse response, OAIPMHCommand params, Map model){
			
			 return (new ModelAndView("ListSet.vm",model));
			
		}
		
		ModelAndView ListMetadataFormats(HttpServletRequest request, HttpServletResponse response, OAIPMHCommand params, Map model){
		
   
			Submission submission = null;  
			
			try{
				 long id = ParamsUtil.parseID(params);
			     submission = studyService.findByID(id).getSubmission();
			}catch(NumberFormatException nfe){
				return (new ModelAndView("badArgument.vm",model));
			}
			catch (NullPointerException e){
				
				return (new ModelAndView("idDoesNotExist.vm",model));
			}
			 
			return (new ModelAndView("ListMetadataFormats.vm",model));
		
		}
		
}
