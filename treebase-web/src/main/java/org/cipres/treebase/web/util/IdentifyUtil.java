package org.cipres.treebase.web.util;

import java.text.CharacterIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.cipres.treebase.web.model.Identify;
import org.cipres.treebase.web.model.OAIPMHCommand;
/*
 * 
 * @author youjun
 *
 */
public class IdentifyUtil {	
	
	// return a studyID by parsing params.identifier
	
	public static long parseID(OAIPMHCommand params)
	{
		String [] ids = params.getIdentifier().split("[/:.]");
		
		return Long.parseLong(ids[ids.length-1].replaceAll("[a-zA-Z]",""));		 
	}
	
	
	// convert a time String to date object based on granularity
	
	public static Date parseGranularity(String granularity, String time ) throws ParseException
	{
		if(time=="")return new Date();		
		SimpleDateFormat sdf = new SimpleDateFormat(granularity);			
		Date utcDate = sdf.parse(time);
		return utcToLocal(utcDate); 
		
		  
	}
	
	//convert a date object from utc to local 
	
	protected static Date utcToLocal(Date utcDate)
	{
		long utcMiliseconds = utcDate.getTime();
	
	    Calendar cal = new GregorianCalendar();
	    cal.setTimeInMillis(utcMiliseconds);
	    return new Date(utcMiliseconds + cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET));
	}
	
	  public static String escape4XML(long study_id, String xmlStr){
		    if(xmlStr==null)return null;
		    StringBuilder result = new StringBuilder();
		    StringCharacterIterator sci = new StringCharacterIterator(xmlStr);
		    char c =  sci.current();
		    while (c != CharacterIterator.DONE ){
		      if (c == '<') {
		        result.append("&lt;");
		      }
		      else if (c == '>') {
		        result.append("&gt;");
		      }
		      else if (c == '\"') {
		        result.append("&quot;");
		      }
		      else if (c == '\'') {
		        result.append("&#039;");
		      }
		      else if (c == '&') {
		         result.append("&amp;");
		      }
		      else {
		        if (!Character.isDefined(c)) {		    	  
		    	    try {
					    throw new Exception();
				    } catch (Exception e) {
					    // TODO Auto-generated catch block
					    System.err.println("study:"+ study_id+" undefined charachter:"+ c + " in " + xmlStr );
				    }
		            
			         
			    }
		        result.append(c);		        
		      }
		      c = sci.next();
		    }
		    return result.toString();
		  }

}
