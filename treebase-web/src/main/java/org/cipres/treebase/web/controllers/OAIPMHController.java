package org.cipres.treebase.web.controllers;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.cipres.treebase.web.compat.AbstractCommandController;
import org.cipres.treebase.web.model.Identify;
import org.cipres.treebase.web.model.OAIPMHCommand;
import org.cipres.treebase.web.util.IdentifyUtil;
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
				list = (List)submissionService.findSubmissionByLastModifiedDateRange(IdentifyUtil.parseGranularity(identify.getGranularityPattern(),params.getModifiedFrom()), 
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
				list = (List)submissionService.findSubmissionByLastModifiedDateRange(IdentifyUtil.parseGranularity(identify.getGranularityPattern(),params.getModifiedFrom()), 
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
			 
		
			map.put("title", IdentifyUtil.escape4XML(study.getId(),citation.getTitle()));
			map.put("creator", authors);	
			//VG 2010-11-17  fixing SF:3079602 (multiple keywords into multiple <dc:subject> elements)
			//--map.put("subject", IdentifyUtil.escape4XML(study.getId(),citation.getKeywords()));
			map.put("subject", splitKeywords(study.getId(),citation.getKeywords())); 
		    if(study.getName()!=null&study.getNotes()!=null)			
		    	map.put("description", IdentifyUtil.escape4XML(study.getId(),study.getName()+" "+study.getNotes()));
		    else if(study.getNotes()==null)
		    	map.put("description",IdentifyUtil.escape4XML(study.getId(),study.getName()));
		    else
		    	map.put("description",IdentifyUtil.escape4XML(study.getId(),study.getNotes()));
			map.put("publisher", IdentifyUtil.escape4XML(study.getId(),publisher));						
			map.put("date", citation.getPublishYear());
			map.put("identifier", "purl.org/phylo/treebase/phylows/study/TB2:S"+study.getId());
			map.put("datestamp", study.getLastModifiedDate());
			
			}catch(Exception e){
				//study 253 citation= null, data should be fixed 
 				System.err.println("study "+study.getId()+
						" citation= "+e.toString());
 				return null;
			}
			
			//map.put("type", "text");
			//map.put("language", "en");
			
			//map.put("issued", citation.getPublishYear());			
			//map.put("abstract", citation.getAbstract());			
	
			
			return map;
		}  
		
		//VG 2010-11-17  fixing SF:3079602 (multiple keywords into multiple <dc:subject> elements)
		private List<String> splitKeywords(long study_id, String kwstring) {
			List<String> kwlist = new java.util.ArrayList<String>(); 
			if (null == kwstring) return kwlist; 
			StringTokenizer tokenizer = new StringTokenizer(kwstring, ",;");
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken().trim();               
				if (!token.equals("")) {
					kwlist.add(IdentifyUtil.escape4XML(study_id, token));
				}
			}	
			return kwlist; 
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
