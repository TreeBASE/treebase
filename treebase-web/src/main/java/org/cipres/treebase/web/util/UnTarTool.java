package org.cipres.treebase.web.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

public class UnTarTool
{

	public static void unTarGz(File file,File outDir) throws IOException{  

		TarInputStream tarIn = null;  

		try{  

			tarIn = new TarInputStream(new GZIPInputStream(  
					new BufferedInputStream(new FileInputStream(file))),  
					1024);  		    
			
			TarEntry entry = null;
            //tarIn.getNextEntry();  
			//File outputDir = new File(outDir, "myBag");
			//outputDir.mkdirs();
			while( (entry = tarIn.getNextEntry()) != null ){  
				File f = new File(outDir,entry.getName());
				if(entry.isDirectory()){ 
					f.mkdirs();
				}else{                     
					f.getParentFile().mkdirs();
					f.createNewFile();
					OutputStream out = null;  
					try{  
						out = new FileOutputStream(f);  
						int length = 0;  
						byte[] b = new byte[1024];  
						while((length = tarIn.read(b)) != -1){  
							out.write(b, 0, length);  
						}  

					}catch(IOException ex){  
						throw ex;  
					}finally{  
						if(out!=null)  
						out.close();  
					}  
				}  
			}  
		}catch(IOException ex){  
			throw ex;  
		} finally{  
			try{  
				if(tarIn != null){  
					tarIn.close();  
				}  
			}catch(IOException ex){  
				throw ex;  
			}  
		}  

	} 
  
} 
