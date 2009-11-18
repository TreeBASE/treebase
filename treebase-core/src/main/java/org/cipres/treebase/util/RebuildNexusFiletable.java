package org.cipres.treebase.util;

import java.sql.Clob;
import java.util.HashMap;
import java.util.Map;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.hibernate.Transaction;

public class RebuildNexusFiletable extends AbstractStandalone {
	public static void main(String args[]) {
		RebuildNexusFiletable me = new RebuildNexusFiletable();
		me.setupContext();
		
		Map<String,Study> nexusFileStudy = new HashMap<String,Study> ();

		warn("Searching studies for nexus files");
		int nStudies = 0;
		for (TBPersistable obj : ContextManager.getStudyHome().findAll(Study.class)) {
			Study s = (Study) obj;
	
			Map<String,String> studyNexusFile = s.getNexusFiles();
			for (String fileName : studyNexusFile.keySet()) {
				if (nexusFileStudy.containsKey(fileName)) {
					nexusFileStudy.remove(fileName);
					warn("Nexus file name " + fileName + " is not unique; ignoring");
				} else {
					nexusFileStudy.put(fileName, s);
				}
			}
			nStudies++;
			if (nStudies % 100 == 0) {
				warn(nStudies + " studies, " + nexusFileStudy.size() + " nexus files");
			}
		}
				
		for (TBPersistable obj : ContextManager.getStudyHome().findAll(Matrix.class)) {
			Matrix m = (Matrix) obj;
			String nexusFileName = m.getNexusFileName();
			Transaction trans = me.beginTransaction();

			if (! nexusFileStudy.containsKey(nexusFileName)) continue;
			Study oldStudy = nexusFileStudy.get(nexusFileName);
			Study newStudy = m.getStudy();	
			if (newStudy == null || oldStudy == null) continue;
			
			newStudy.getNexusFiles().put(nexusFileName, oldStudy.getNexusFiles().remove(nexusFileName));
			trans.commit();
			warn("Repatriated nexus file " + nexusFileName + " from study " + oldStudy.getId() + " to " + newStudy.getId());
		}
		
		for (TBPersistable obj : ContextManager.getStudyHome().findAll(PhyloTree.class)) {
			PhyloTree t = (PhyloTree) obj;
			String nexusFileName = t.getNexusFileName();
			Transaction trans = me.beginTransaction();

			if (! nexusFileStudy.containsKey(nexusFileName)) continue;
			Study oldStudy = nexusFileStudy.get(nexusFileName);
			Study newStudy = t.getStudy();		
			if (newStudy == null || oldStudy == null) continue;
		
			newStudy.getNexusFiles().put(nexusFileName, oldStudy.getNexusFiles().remove(nexusFileName));
			trans.commit();
			warn("Repatriated nexus file " + nexusFileName + " from study " + oldStudy.getId() + " to " + newStudy.getId());
		}
	}
}
