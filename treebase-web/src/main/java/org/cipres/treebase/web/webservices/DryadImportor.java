package org.cipres.treebase.web.webservices;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
 
import com.sun.jersey.multipart.*;   
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes; 
//import javax.ws.rs.POST;
import javax.ws.rs.PUT; 
import javax.ws.rs.Path; 
import javax.ws.rs.core.Context;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.web.util.UnTarTool;


 
@Path("/dryadImport") 

public class DryadImportor { 

	@Context
	HttpServletRequest httpRequest;
    
	@PUT 
    //@POST
    //@GET    
    
    @Consumes("multipart/mixed") 
    
public String processMultiparts(MultiPart multiPart) { 
       
        String ip = httpRequest.getRemoteAddr();
    	// apache server re-write the ip to 127.0.0.1
        //if(!AllowIP.isAllowIP(ip))return "client " + ip + " is not authorized"; 
    	
        BodyPartEntity bpe = (BodyPartEntity) multiPart.getBodyParts().get(0).getEntity();  
        File unzipFold;
        String systemTime;
        
        do{
        	systemTime = "" + System.currentTimeMillis();
        	String uploadDir = httpRequest.getSession().getServletContext()
        	.getRealPath(TreebaseUtil.FILESEP + "DryadFileUpload")
        	+ TreebaseUtil.FILESEP + systemTime;
        	unzipFold = new File(uploadDir);
        }while(unzipFold.exists());
        
        
        File zipfile = new File(unzipFold,"data.tar.gz");
       
        try {
        	unzipFold.mkdirs();
        	zipfile.createNewFile();
        	InputStream in = bpe.getInputStream();          	
        	OutputStream out=new FileOutputStream(zipfile);

            byte[] buf = new byte[1024];      
            int len;  
            while ((len = in.read(buf)) > 0){      
            	out.write(buf, 0, len);     
            }     
            in.close();
            out.close(); 
            UnTarTool.unTarGz(zipfile,unzipFold);
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			return sw.getBuffer().toString();  
		} 
		
		String BASEURL = TreebaseUtil.getSiteUrl();
		// apache server re-write the application path to localhost 
		//String[] baseURL=httpRequest.getRequestURL().toString().split("handshaking");
        String importURL= BASEURL + "login.jsp?importKey=" + systemTime;
     
        return importURL; 
  
    }
    
}  

class AllowIP{
	private static final String dryad1 = "152.1.24.8";
	private static final String dryad2 = "152.3.105.16";
	private static final String yale1 = "130.132.27.141";
	private static final String yale2 = "130.132.27.212";
    
	static boolean isAllowIP(String ip){
		if (ip.compareTo(dryad1)==0) return true;
		if (ip.compareTo(dryad2)==0) return true;
		if (ip.compareTo(yale1)==0)return true;
		if (ip.compareTo(yale2)==0)return true;
		
	    return false;	
	}

}
