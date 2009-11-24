package org.treebase.oai.web.controller;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;
import org.treebase.oai.web.command.Identify;
import org.treebase.oai.web.command.OAIPMHCommand;
import org.treebase.oai.web.util.IdentifyUtil;
import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.study.ArticleCitation;
import org.cipres.treebase.domain.study.BookCitation;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.Study;
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
		 		 
		 if(errors.getErrorCount()>0){			 
		     if(errors.hasFieldErrors("metadataPrefix")){
				model.put("error_code", "cannotDisseminateFormat");
				model.put("error", "bad metadataPrefix");
		    	return new ModelAndView("error",model); 
		     }
		     if(errors.hasFieldErrors("verb")){
		    	 model.put("error_code", "badVerb");
				 model.put("error", "no verb was found");
				 return new ModelAndView("error",model);
		     }
		     if(errors.hasFieldErrors("identifier")){
		    	 model.put("error_code", "idDoesNotExist");
				 model.put("error", "no id was found");		     
				 return new ModelAndView("error",model);
		     }
		 }
		 
		 Method method=null;
		 try{
			 method=this.getClass().getMethod(params.getVerb(), new Class[]{OAIPMHCommand.class, Map.class});
		 }catch(NoSuchMethodException nsme){
			 model.put("error_code", "badVerb");
			 model.put("error", "invalid verb");
			 return new ModelAndView("error",model);
		 }catch(NullPointerException e){
			 model.put("error_code", "badVerb");
			 model.put("error", "missing verb");
			 return (new ModelAndView("error",model));
		 }
		 
		 return (ModelAndView) method.invoke(this, params, model);
	
	    }

	public ModelAndView ListRecords(OAIPMHCommand params, Map model){
			
			List<Submission> list=null;
			try {
				list = (List)submissionService.findSubmissionByCreateDateRange(IdentifyUtil.parseGranularity(identify.getGranularityPattern(),params.getModifiedFrom()), 
						IdentifyUtil.parseGranularity(identify.getGranularityPattern(),params.getModifiedUntil()));
			} catch (ParseException e) {
				model.put("error_code", "badArgument");
				model.put("error", "invalid from or until format");
				return (new ModelAndView("error",model));
			}
			model.put("recordList", getRecordList(list));
			return (new ModelAndView("ListRecords",model));
		
		}
    		

		public ModelAndView ListIdentifiers( OAIPMHCommand params, Map model){
			
			List<Submission> list=null;
			try {
				list = (List)submissionService.findSubmissionByCreateDateRange(IdentifyUtil.parseGranularity(identify.getGranularityPattern(),params.getModifiedFrom()), 
						IdentifyUtil.parseGranularity(identify.getGranularityPattern(), params.getModifiedUntil()));
			} catch (ParseException e) {
				model.put("error_code", "badArgument");
				model.put("error", "invalid from or until format");
				return (new ModelAndView("error",model));
			}
			model.put("recordList", getRecordList(list));
			return (new ModelAndView(params.getMetadataPrefix()+"_ListIdentifiers",model));
	
		}

		public ModelAndView GetRecord( OAIPMHCommand params, Map model){
			
			Submission submission = null;  
			
			try{
				 long id = IdentifyUtil.parseID(params);
			     submission = studyService.findByID(id).getSubmission();
			}catch(NumberFormatException nfe){
				model.put("error_code", "badArgument");
				model.put("error", "invalid id format");
				return (new ModelAndView("error",model));
		
			}
			catch (NullPointerException e){
				
				model.put("error_code", "idDoesNotExist");
				model.put("error", "invalid id");
				return (new ModelAndView("error",model));
				
			}
			model.put("record", getRecordMap(submission));			
			return (new ModelAndView("GetRecord",model));
	
		}

		public ModelAndView Identify(OAIPMHCommand params, Map model){
	
			
			return (new ModelAndView("Identify",model));
	
		}
		
		public ModelAndView ListSets(OAIPMHCommand params, Map model){
			model.put("error_code", "noSetHierarchy");
			model.put("error", "This repository does not support sets");			
			return (new ModelAndView("error",model));
			
		}
		
		public ModelAndView ListMetadataFormats(OAIPMHCommand params, Map model){
		
			Submission submission = null;  
			
			try{
				 long id = IdentifyUtil.parseID(params);
			     submission = studyService.findByID(id).getSubmission();
			}catch(NumberFormatException nfe){
				model.put("error_code", "badArgument");
				model.put("error", "invalid id format");			
				return (new ModelAndView("error",model));
				
			}
			catch (NullPointerException e){
				//id is optional for ListMetadataFormats
				//return (new ModelAndView("error",model));
			}
			
			
			return (new ModelAndView("ListMetadataFormats",model));
		
		}
	
		
		private Map getRecordMap(Submission submission){
			
			Map map= new HashMap();
			
			Study study=submission.getStudy();
			Citation citation=study.getCitation();
			String publisher=null;
			if(!study.isPublished())return null;
			
			//System.out.println("ctype: "+citation.getCitationType());
			try{
			if(citation.getCitationType().toUpperCase().contains("BOOK"))
				   publisher=((BookCitation)citation).getPublisher();
			else publisher=((ArticleCitation)citation).getJournal();
			
			
			List<Person> authors=citation.getAuthors();
			 
		
			map.put("title", citation.getTitle());
			map.put("creator", authors);			
			map.put("subject", citation.getKeywords());
		    if(study.getName()!=null&study.getNotes()!=null)			
		    	map.put("description", study.getName()+" "+study.getNotes());
		    else if(study.getNotes()==null)
		    	map.put("description",study.getName());
		    else
		    	map.put("description",study.getNotes());
			map.put("publisher", publisher);						
			map.put("date", "published on "+citation.getPublishYear());
			map.put("identifier", "treebase.org/study/TB2:s"+study.getId());
			map.put("datestamp", study.getReleaseDate());
			
			}catch(NullPointerException e){
				//study 253 citation= null, data should be fixed 
 				System.err.println("study "+study.getId()+
						" citation= "+e.getMessage());
 				return null;
			}
			
			//map.put("type", "text");
			//map.put("language", "en");
			
			//map.put("issued", citation.getPublishYear());			
			//map.put("abstract", citation.getAbstract());			
	
			
			return map;
		}  
		
		private List getRecordList(List<Submission> sList)
		{
			List recordList=new ArrayList<Map>();
			
			for(int i =0; i< sList.size(); i++){
				Map map=getRecordMap(sList.get(i));
				if(map!=null)recordList.add(map);
			}
			return recordList;
		}
}
