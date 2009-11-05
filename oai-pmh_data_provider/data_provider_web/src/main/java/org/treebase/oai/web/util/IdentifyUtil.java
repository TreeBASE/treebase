package org.treebase.oai.web.util;

import org.treebase.oai.web.command.OAIPMHCommand;

public class IdentifyUtil {

	public static boolean badMetadataPrefix(OAIPMHCommand params){
		if (params.getMetadataPrefix().toLowerCase()=="dc") 
			return false;
		if (params.getMetadataPrefix().toLowerCase()=="dryad")
		    return false;
		return true;
	}
	
	public static long parseID(OAIPMHCommand params)
	{
		String [] ids = params.getIdentifier().split("/");
		return Long.parseLong(ids[ids.length-1]);		 
	}
}
