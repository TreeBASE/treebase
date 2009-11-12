package org.treebase.oai.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.treebase.oai.web.command.Identify;
import org.treebase.oai.web.command.OAIPMHCommand;
/*
 * 
 * @author youjun
 *
 */
public class IdentifyUtil {

	// check if the MetadataPrefix in the params is supported by the service
	
	public static boolean badMetadataPrefix(OAIPMHCommand params){
		if (params.getMetadataPrefix().toLowerCase()=="oai_dc") 
			return false;
		if (params.getMetadataPrefix().toLowerCase()=="dryad")
		    return false;
		return true;
	}
	
	// return a studyID by parsing params.identifier
	
	public static long parseID(OAIPMHCommand params)
	{
		String [] ids = params.getIdentifier().split("[/:.]");
		
		return Long.parseLong(ids[ids.length-1].replaceAll("[a-zA-Z]",""));		 
	}
	
	
	// convert a time String to date object based on granularity
	
	public static Date parseGranularity(String granularity, String time ) throws ParseException
	{
				
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
}
