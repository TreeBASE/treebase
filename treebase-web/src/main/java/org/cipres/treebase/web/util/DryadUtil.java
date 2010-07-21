package org.cipres.treebase.web.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.study.Citation;
import org.dom4j.*;
import org.dom4j.io.*;

public class DryadUtil {
	
	public static List<File> getDataFiles(File path) {
		// TODO Auto-generated method stub
		 File[] files = path.listFiles(new FileFilter() {
	            public boolean accept(File file) {	                 
	                return file.isDirectory();
	     }});		
	            
		 List<File> flist = new ArrayList<File>();
		 
		 for(int i=0; i<files.length; i++){
			 
			 File[] nexus = files[i].listFiles(new FilenameFilter() {
		            public boolean accept(File file, String name) {
		            boolean ret = (name.endsWith("nexus")); 
		            return ret;
		     }});
			 
			 if (nexus!=null && nexus.length>0) flist.addAll(Arrays.asList(nexus));
		 }				 
				
		return flist;
	}

}
