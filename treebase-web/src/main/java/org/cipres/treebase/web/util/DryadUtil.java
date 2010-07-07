package org.cipres.treebase.web.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.study.Citation;
import org.dom4j.*;
import org.dom4j.io.*;

public class DryadUtil {
	
	
	public static Citation createCitation(File path){
		Citation citation = new Citation();
		
		File f = new File(path, "citation.xml");
		SAXReader reader = new SAXReader();
		Document doc= null ;
		try {
			doc = reader.read(f);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = doc.getRootElement();
		Element node;
		for (Iterator i = root.elementIterator("author"); i.hasNext();) {
			node = (Element) i.next();
			Person p = new Person();
			//node.getName();
			citation.addAuthor(p);
		}
		citation.setTitle(root.element("title").getName());
		
		
		return citation;
	}

	public static Collection<File> getDataFiles(File path) {
		// TODO Auto-generated method stub
		List<File> flist=Arrays.asList(path.listFiles(new FilenameFilter() {
            public boolean accept(File file, String name) {
                boolean ret = !(name.endsWith("xml")); 
                return ret;
            }}));		
		return flist;
	}

}
