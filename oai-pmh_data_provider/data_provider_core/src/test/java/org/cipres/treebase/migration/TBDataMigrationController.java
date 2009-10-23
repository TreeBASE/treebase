/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

package org.cipres.treebase.migration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import org.cipres.datatypes.PhyloDataset;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.matrix.ItemDefinitionHome;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixDataTypeHome;
import org.cipres.treebase.domain.matrix.StandardMatrix;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.nexus.mesquite.MesquiteConverter;
import org.cipres.treebase.domain.nexus.ncl.NCLNexusConverter;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalyzedTree;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.tree.PhyloTree;

/**
 * TBDataMigrationController.java
 * 
 * Created on Jun 22, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class TBDataMigrationController extends AbstractDAOTest {

	// private TBDataMigrationGUI mView;
	private String mInsertCitationSQL = null;
	private String mInsertStudySQL;

	private TaxonLabelHome mTaxonLabelHome;
	private MatrixDataTypeHome mMatrixDataTypeHome;
	private ItemDefinitionHome mItemDefinitionHome;

	/**
	 * Constructor.
	 */
	public TBDataMigrationController() {
		super();

		try {
			setUp();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * protect data with quotes
	 */
	private static String quote(String include) {
		return ("\"" + include + "\"");
	}

	/**
	 * Return the MatrixDataTypeHome field.
	 * 
	 * @return MatrixDataTypeHome mMatrixDataTypeHome
	 */
	public MatrixDataTypeHome getMatrixDataTypeHome() {
		return mMatrixDataTypeHome;
	}

	/**
	 * Set the MatrixDataTypeHome field.
	 */
	public void setMatrixDataTypeHome(MatrixDataTypeHome pNewMatrixDataTypeHome) {
		mMatrixDataTypeHome = pNewMatrixDataTypeHome;
	}

	/**
	 * Return the TaxonLabelHome field.
	 * 
	 * @return TaxonLabelHome mTaxonLabelHome
	 */
	public TaxonLabelHome getTaxonLabelHome() {
		return mTaxonLabelHome;
	}

	/**
	 * Set the TaxonLabelHome field.
	 */
	public void setTaxonLabelHome(TaxonLabelHome pNewTaxonLabelHome) {
		mTaxonLabelHome = pNewTaxonLabelHome;
	}

	/**
	 * Return the ItemDefinitionHome field.
	 * 
	 * @return ItemDefinitionHome mItemDefinitionHome
	 */
	public ItemDefinitionHome getItemDefinitionHome() {
		return mItemDefinitionHome;
	}

	/**
	 * Set the ItemDefinitionHome field.
	 */
	public void setItemDefinitionHome(ItemDefinitionHome pNewItemDefinitionHome) {
		mItemDefinitionHome = pNewItemDefinitionHome;
	}

	/**
	 * 
	 * @param pFile
	 */
	public void processLoadCitation(File pFile) {
		System.out.println(" processLoadCitation: " + pFile.getAbsolutePath());
		long before = System.currentTimeMillis();
		StringBuffer buf = new StringBuffer();
		int errCount = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader(pFile));
			String aLine;

			int count = 0;
			while ((aLine = in.readLine()) != null) {
				if (aLine != null && aLine.startsWith("%0 ")) {
					boolean created = loadOneCitation(aLine, in, buf);
					if (created) {
						count++;
					} else {
						errCount++;
					}
				}
			}

			in.close();

			long after = System.currentTimeMillis();
			long time = (after - before) / 100;

			System.out.println(" processCitation Done. count=" + count + " time(s)=" + time);
			System.out.println(" failed to create count=" + errCount + " study_id ="
				+ buf.toString());

		} catch (IOException ex) {
			System.out.println("IO Exception: ");
			ex.printStackTrace();
		}

	}

	/**
	 * 
	 * @param pFile
	 */
	public void processLoadCitationAuthor(File pFile) {

		String citationIDSQL = "select citation_id from citation where tb_studyID = ?";
		String personIDSQL = "select person_id from person where authorID = ?";
		String citationCountSQL = "select count(*) from citation_author " + "where citation_id = ?";
		String citationAuthorCountSQL = "select count(*) from citation_author "
			+ "where citation_id = ? and authors_person_id = ?";
		String authorOrderMaxSQL = "select max(author_order) from citation_author "
			+ "where citation_id = ?";

		String insertCitationAuthorSQL = "INSERT INTO TBASE2.CITATION_AUTHOR"
			+ "(CITATION_ID, AUTHORS_PERSON_ID, AUTHOR_ORDER) VALUES(?, ?, ?)";

		System.out.println(" processLoadCitationAuthor: " + pFile.getAbsolutePath());
		long before = System.currentTimeMillis();
		StringBuffer buf = new StringBuffer();
		int errCount = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader(pFile));
			String aLine;

			// skip the first line:
			aLine = in.readLine();

			int count = 0;
			while ((aLine = in.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(aLine, "\t\n", false);
				final String tb_studyID = st.nextToken();
				String tb_authorID = st.nextToken();
				assertTrue(!st.hasMoreTokens());

				// first check citation and person objects are there:
				Object[] paraTBStudyID = new Object[] {tb_studyID};
				long citationID = -1;
				try {
					citationID = jdbcTemplate.queryForLong(citationIDSQL, paraTBStudyID);
				} catch (IncorrectResultSizeDataAccessException ex) {
					citationID = -1;
					System.out.println("   : no citation found. tb_studyID = " + tb_studyID);
				}

				Object[] paraTBAuthorID = new Object[] {tb_authorID};
				long personID = -1;
				try {
					personID = jdbcTemplate.queryForLong(personIDSQL, paraTBAuthorID);
				} catch (IncorrectResultSizeDataAccessException ex) {
					personID = -1;
					System.out.println("   : no person found. tb_personID = " + tb_authorID);
				}

				if (personID > 0 && citationID > 0) {

					// next make sure the authorship is not already there:
					Object[] paraCitationAuthorCount = new Object[] {citationID, personID};
					int entryCount = jdbcTemplate.queryForInt(
						citationAuthorCountSQL,
						paraCitationAuthorCount);

					if (entryCount < 1) {
						// next get the citation_order:
						int authorOrder = 0;

						Object[] paraCitationID = new Object[] {citationID};
						int citationCount = jdbcTemplate.queryForInt(
							citationCountSQL,
							paraCitationID);

						if (citationCount > 0) {
							authorOrder = jdbcTemplate.queryForInt(
								authorOrderMaxSQL,
								paraCitationID);

							authorOrder++;
						}

						// now create a citation-author entry:
						try {
							Object[] paraCitationAuthor = new Object[] {citationID, personID,
								authorOrder};
							int rowCount = jdbcTemplate.update(
								insertCitationAuthorSQL,
								paraCitationAuthor);

							// Force commit now:
							setComplete();
							endTransaction();

							System.out.println("processOneCitationAuthor: done saving: "
								+ tb_studyID);
							assertTrue(rowCount > 0);

							count++;
						} catch (Exception ex) {
							System.out
								.println("        : failed to create citationAuthor: tb_studyId = "
									+ tb_studyID + " tb_authorID= " + tb_authorID);
							ex.printStackTrace();

							errCount++;
							buf.append(tb_studyID + "-" + tb_authorID + ", ");
						}
					} else {
						System.out.println("    : citationAuthor already exists: tb_studyId = "
							+ tb_studyID + " tb_authorID= " + tb_authorID);
					}
				} else {
					errCount++;
					buf.append(tb_studyID + "-" + tb_authorID + ", ");
				}
			}

			in.close();

			long after = System.currentTimeMillis();
			long time = (after - before) / 100;

			System.out.println(" processCitationAuthor Done. count=" + count + " time(s)=" + time);
			System.out.println(" failed to create count=" + errCount
				+ " tb_studyId and tb_authorID=" + buf.toString());

		} catch (IOException ex) {
			System.out.println("IO Exception: ");
			ex.printStackTrace();
		}

	}

	/**
	 * 
	 * @param pLine
	 * @param pIn
	 */
	private boolean loadOneCitation(String pLine, BufferedReader pIn, StringBuffer pErrorStr)
		throws IOException {

		String type = "";
		// Note: the Book citation type is not in the end note file.

		if (pLine.indexOf("Journal Article") > 0) {
			type = "A";
		} else if (pLine.indexOf("Book Section") > 0) {
			type = "I";
		} else if (pLine.indexOf("Conference Proceedings") > 0) {
			type = "I";
		} else if (pLine.indexOf("Electronic Source") > 0) {
			type = "B"; // treat it as a book for now. It has publisher info.
		} else if (pLine.indexOf("Thesis") > 0) {
			type = "B";
		} else {
			System.out.println("Unknow type: " + pLine);
		}

		// Citation
		// Discard author info, will load author info later from
		// AuthorStudy.txt

		Integer publishYear = null;
		String title = null;
		String journal = null;
		String volume = null;
		String issue = null;
		String pages = null;
		String abst = null;
		String tb_studyID = null;
		String booktitle = null;
		String city = null;
		String publisher = null;

		String aLine = null;
		boolean done = false;
		while ((aLine = pIn.readLine()) != null && !done) {
			if (aLine != null && aLine.startsWith("%")) {
				if (aLine.startsWith("%A")) {
					// skip the authors
				} else if (aLine.startsWith("%D")) {
					try {
						publishYear = Integer.parseInt(aLine.substring(3));
					} catch (NumberFormatException ex) {
						publishYear = null;
						System.out.println("publish year number format exception: " + title);
					}
				} else if (aLine.startsWith("%T")) {
					title = quote(aLine.substring(3));
				} else if (aLine.startsWith("%J")) {
					journal = aLine.substring(3);
				} else if (aLine.startsWith("%V")) {
					volume = aLine.substring(3);
				} else if (aLine.startsWith("%N")) {
					issue = aLine.substring(3);
				} else if (aLine.startsWith("%P")) {
					pages = aLine.substring(3);
				} else if (aLine.startsWith("%!")) {
					// Skip. what is that?
					// it seems to repeat the title
				} else if (aLine.startsWith("%X")) {
					abst = quote(aLine.substring(3));
				} else if (aLine.startsWith("%Z")) {
					tb_studyID = aLine.substring(3);
				} else if (aLine.startsWith("%B")) {
					booktitle = aLine.substring(3);
				} else if (aLine.startsWith("%C")) {
					city = aLine.substring(3);
				} else if (aLine.startsWith("%I")) {
					publisher = aLine.substring(3);
				} else if (aLine.startsWith("%F")) {
					// In press.
					// Skip
				} else if (aLine.startsWith("%E")) {
					// Editor
					// skip for now
				}
			} else {
				done = true;
			}
		}

		String citataionIDSQL = "select count(*) from citation where TB_studyID = ?";

		// first check whether the citation is here already:
		Object[] id = new Object[] {tb_studyID};
		int citationCount = jdbcTemplate.queryForInt(citataionIDSQL, id);

		if (citationCount < 1) {
			// save to db:
			try {
				String sql = getInsertCitationSQL();
				Object[] params = new Object[] {type, pages, publishYear, abst, title, issue,
					volume, journal, city, publisher, booktitle, tb_studyID};

				int rowCount = jdbcTemplate.update(sql, params);

				// Force commit now:
				setComplete();
				endTransaction();

				System.out.println("processOneCitation: done saving: " + title);
				assertTrue(rowCount > 0);

				return true;
			} catch (Exception ex) {
				System.out.println("        : failed to create citation: tb_studyId = "
					+ tb_studyID + " title:" + title);
				ex.printStackTrace();

				pErrorStr.append(tb_studyID + ", ");
				return false;
			}
		} else {
			System.out.println("        : citation exists: tb_studyId = " + tb_studyID + " title:"
				+ title);
			pErrorStr.append(tb_studyID + ", ");

			return false;
		}
	}

	/**
	 * 
	 * @return
	 */
	private String getInsertCitationSQL() {
		/*
		 * INSERT INTO TBASE2.CITATION(TYPE, VERSION, PUBLISHED, PAGES, KEYWORDS, PUBLISHYEAR, PMID,
		 * DOI, ABSTRACT, URL, TITLE, ISSUE, VOLUME, JOURNAL, ISBN, CITY, PUBLISHER, BOOKTITLE,
		 * TB_STUDYID) VALUES('', 0, 0, '', '', 0, '', '', '', '', '', '', '', '', '', '', '', '',
		 * '')
		 */

		if (mInsertCitationSQL == null) {
			mInsertCitationSQL = "INSERT INTO TBASE2.CITATION(TYPE, VERSION, PUBLISHED, PAGES, KEYWORDS, "
				+ "PUBLISHYEAR, PMID, DOI, ABSTRACT, URL, "
				+ "TITLE, ISSUE, VOLUME, JOURNAL, ISBN, "
				+ "CITY, PUBLISHER, BOOKTITLE, TB_STUDYID) "
				+ "VALUES(?, 0, 1, ?, null, "
				+ "?, null, null, ?, null, " + "?, ?, ?, ?, null, " + "?, ?, ?, ?)";
		}
		return mInsertCitationSQL;
	}

	/**
	 * Return the InsertStudySQL field. Uses lazy initialization.
	 * 
	 * @return String mInsertStudySQL
	 */
	private String getInsertStudySQL() {
		// INSERT INTO TBASE2.STUDY(STUDY_ID, VERSION, NOTES, ACCESSIONNUMBER, NAME, CITATION_ID,
		// STUDYSTATUS_ID, TB_STUDYID)
		// VALUES(0, 0, '', '', '', 0, 0, '')

		if (mInsertStudySQL == null) {
			mInsertStudySQL = "INSERT INTO TBASE2.STUDY(VERSION, NOTES, ACCESSIONNUMBER, NAME, CITATION_ID, "
				+ "STUDYSTATUS_ID, TB_STUDYID) VALUES(0, null, ?, null, ?, 3, ?)";
		}
		return mInsertStudySQL;
	}

	/**
	 * 
	 * @param pFile
	 */
	public void processLoadStudy(File pFile) {
		System.out.println(" processLoadStudy: " + pFile.getAbsolutePath());
		long before = System.currentTimeMillis();
		String citataionIDSQL = "select citation_id from citation where TB_studyID = ?";
		String studyIDSQL = "select count(*) from study where TB_studyID = ?";

		try {
			BufferedReader in = new BufferedReader(new FileReader(pFile));
			String aLine;

			int count = 0;
			int noCitationCount = 0;
			StringBuffer noCitationStudy = new StringBuffer();
			while ((aLine = in.readLine()) != null) {

				// first check whether the study is here already:
				Object[] id = new Object[] {aLine};
				int studyCount = jdbcTemplate.queryForInt(studyIDSQL, id);

				if (studyCount < 1) {
					// get the citation id:
					long citation_id = -1;

					try {
						citation_id = jdbcTemplate.queryForLong(citataionIDSQL, id);
					} catch (IncorrectResultSizeDataAccessException ex) {
						citation_id = -1;
					}

					if (citation_id > 0) {
						// save to db:
						String sql = getInsertStudySQL();
						Object[] params = new Object[] {aLine, new Long(citation_id), aLine};

						int rowCount = jdbcTemplate.update(sql, params);

						// Force commit now:
						setComplete();
						endTransaction();

						System.out.println("processOneStudy: done saving: " + aLine);
						assertTrue(rowCount > 0);
						count++;
					} else {
						System.out.println("           : no citation found: " + aLine);
						noCitationCount++;
						noCitationStudy.append(aLine + ", ");
					}
				} else {
					System.out.println("processOneStudy: study exists: " + aLine);
				}
			}

			in.close();

			long after = System.currentTimeMillis();
			long time = (after - before) / 100;

			System.out.println(" processStudy Done. count=" + count + " time(s)=" + time);
			System.out.println(" study with no citation count=" + noCitationCount + " id ="
				+ noCitationStudy.toString());

		} catch (IOException ex) {
			System.out.println("IO Exception: ");
			ex.printStackTrace();
		}

	}

	/**
	 * 
	 * @param pF
	 */
	public void processLoadAnalysis(File pFile) {

		System.out.println(" processLoadStudy: " + pFile.getAbsolutePath());
		long before = System.currentTimeMillis();

		String studyIDSQL = "select study_id from study where TB_studyID = ?";
		String studyCountSQL = "select count(*) from study where TB_studyID = ?";
		String analysisStepCountSQL = "select count(*) from analysisStep where TB_analysisID = ?";
		String analysisCountSQL = "select count(*) from analysis where study_id = ?";
		String analysisOrderSQL = "select max(analysis_order) from analysis where study_id = ?";
		String softwareIDSQL = "select software_id from software where NAME = ?";

		final String insertSoftwareSQL = "INSERT INTO TBASE2.SOFTWARE("
			+ "VERSION, SOFTWAREVERSION, NAME, DESCRIPTION) VALUES(1, null, ?, null)";
		final String insertAlogrithmSQL = "INSERT INTO TBASE2.ALGORITHM("
			+ "TYPE, VERSION, PROPERTYVALUE, PROPERTYNAME, POLYTCOUNT_ID, GAPMODE_ID, USERTYPE_ID, DESCRIPTION) "
			+ "VALUES(?, 0, null, null, null, null, null, ?)";
		final String insertAnalysisStepSQL = "INSERT INTO TBASE2.ANALYSISSTEP("
			+ "VERSION, PUBLISHED, NOTES, COMMANDS, NAME, "
			+ "ANALYSIS_ID, ALGORITHM_ID, SOFTWARE_ID, STEP_ORDER, TB_ANALYSISID) VALUES("
			+ "1, 1, null, null, null, ?, ?, ?, 0, ?)";
		final String insertAnalysisSQL = "INSERT INTO TBASE2.ANALYSIS("
			+ "VERSION, NOTES, NAME, STUDY_ID, ANALYSIS_ORDER) VALUES(1, null, ?, ?, ?)";

		try {
			BufferedReader in = new BufferedReader(new FileReader(pFile));
			String aLine;

			int count = 0;
			int noStudyCount = 0;
			StringBuffer noStudyAnalysis = new StringBuffer();

			// skip the first line:
			aLine = in.readLine();

			while ((aLine = in.readLine()) != null) {

				// workaround the problem that StringTokenizer does not report the empty token.
				// call this enough times to catch multipel empty tokens.
				aLine = aLine.replaceAll("\t\t", "\t \t");
				aLine = aLine.replaceAll("\t\t", "\t \t");
				aLine = aLine.replaceAll("\t\t", "\t \t");
				aLine = aLine.replaceAll("\t\t", "\t \t");

				StringTokenizer st = new StringTokenizer(aLine, "\t\n", false);
				String tb_analysisID = st.nextToken();
				String tb_studyID = st.nextToken();
				String analysisNameParsed = st.nextToken();
				String softwareOriginal = st.nextToken();
				String algorithmParsed = st.nextToken();

				final String algorithm = algorithmParsed.trim();
				final String analysisName = analysisNameParsed.trim();
				final String software = softwareOriginal.trim();

				// System.out.println(analysisID + ", " + studyID + ", " + analysisName + ", "
				// + software + ", " + algorithm);

				assertTrue(!st.hasMoreTokens());

				// first check whether the analysis is here already, skip these
				// already there.
				Object[] paraAnalysisID = new Object[] {tb_analysisID};
				int analysisStepCount = jdbcTemplate.queryForInt(
					analysisStepCountSQL,
					paraAnalysisID);

				if (analysisStepCount < 1) {
					// next check whether the study is here, skip these that
					// there is no study.
					Object[] paraTBStudyid = new Object[] {tb_studyID};
					long studyID = -1;
					try {
						studyID = jdbcTemplate.queryForLong(studyIDSQL, paraTBStudyid);
					} catch (IncorrectResultSizeDataAccessException ex) {
						studyID = -1;
					}

					if (studyID >= 0) {
						// save to db:
						// first get the correct analysis_order for the studyID.
						int analysis_order = 0;
						Object[] paraStudyid = new Object[] {studyID};
						try {
							int analysisCount = jdbcTemplate.queryForInt(
								analysisCountSQL,
								paraStudyid);

							if (analysisCount > 0) {
								analysis_order = jdbcTemplate.queryForInt(
									analysisOrderSQL,
									paraStudyid);

								analysis_order++;
							}
						} catch (IncorrectResultSizeDataAccessException ex) {
							analysis_order = 0;
						}

						// then save the analysis, get the new analysis id
						final long finalStudyID = studyID;
						final int finalAnalysis_order = analysis_order;

						KeyHolder keyHolder = new GeneratedKeyHolder();
						PreparedStatementCreator psCreator = new PreparedStatementCreator() {
							public PreparedStatement createPreparedStatement(Connection con)
								throws SQLException {

								PreparedStatement ps = con.prepareStatement(
									insertAnalysisSQL,
									Statement.RETURN_GENERATED_KEYS);
								ps.setString(1, analysisName);
								ps.setLong(2, finalStudyID);
								ps.setInt(3, finalAnalysis_order);

								return ps;
							};
						};

						int rowCount = jdbcTemplate.update(psCreator, keyHolder);
						long newAnalysisID = keyHolder.getKey().longValue();
						assertTrue("failed to create a new analysis.", newAnalysisID > 0);

						// second find softwareID by name. If not found,
						// create a new software entry and get its new ID.
						Long softwareIDObj = null;
						long softwareID = -1;
						if (!TreebaseUtil.isEmpty(software)) {
							Object[] paraSoftwareName = new Object[] {software};
							try {
								softwareID = jdbcTemplate.queryForInt(
									softwareIDSQL,
									paraSoftwareName);
							} catch (IncorrectResultSizeDataAccessException ex) {
								softwareID = -1;
							}

							if (softwareID < 0) {
								// need to create a new software entry:

								KeyHolder keyHolderSoftware = new GeneratedKeyHolder();
								PreparedStatementCreator psSoftware = new PreparedStatementCreator() {
									public PreparedStatement createPreparedStatement(Connection con)
										throws SQLException {

										PreparedStatement ps = con.prepareStatement(
											insertSoftwareSQL,
											Statement.RETURN_GENERATED_KEYS);
										ps.setString(1, software);

										return ps;
									};
								};

								int softwareCount = jdbcTemplate.update(
									psSoftware,
									keyHolderSoftware);
								softwareID = keyHolderSoftware.getKey().longValue();
							}
							assertTrue("failed to create a new software.", softwareID >= 0);

							softwareIDObj = softwareID;
						}

						// third create an algorithm object, get its new id.
						String algorithmType = "O";
						if ("parsimony".compareToIgnoreCase(algorithm) == 0) {
							algorithmType = "P";
						}
						final String finalAlgorithmType = algorithmType;

						KeyHolder keyHolderAlgorithm = new GeneratedKeyHolder();
						PreparedStatementCreator psAlgorithm = new PreparedStatementCreator() {
							public PreparedStatement createPreparedStatement(Connection con)
								throws SQLException {

								PreparedStatement ps = con.prepareStatement(
									insertAlogrithmSQL,
									Statement.RETURN_GENERATED_KEYS);
								ps.setString(1, finalAlgorithmType);
								ps.setString(2, algorithm);

								return ps;
							};
						};

						int algorithmCount = jdbcTemplate.update(psAlgorithm, keyHolderAlgorithm);
						long algorithmID = keyHolderAlgorithm.getKey().longValue();
						assertTrue("failed to create a new algorithm.", algorithmID >= 0);

						// finally, we are ready for creating analysis step:
						Object[] paramAnalysisStep = new Object[] {newAnalysisID, algorithmID,
							softwareIDObj, tb_analysisID};

						int newRowCount = jdbcTemplate.update(
							insertAnalysisStepSQL,
							paramAnalysisStep);

						// Force commit now:
						setComplete();
						endTransaction();

						System.out.println("processOneAnalysis: done saving: " + tb_analysisID);
						assertTrue(newRowCount > 0);
						count++;
					} else {
						System.out.println("   : no study found: tb_studyID= " + tb_studyID);
						noStudyCount++;
						noStudyAnalysis.append(tb_analysisID + ", ");
					}
				} else {
					System.out.println("   : analysis exists: " + tb_analysisID);
				}
			}

			in.close();

			long after = System.currentTimeMillis();
			long time = (after - before) / 100;

			System.out.println(" processAnalysis Done. count=" + count + " time(s)=" + time);
			System.out.println(" analysis with no study count=" + noStudyCount + " tb_analysisID ="
				+ noStudyAnalysis.toString());

		} catch (IOException ex) {
			System.out.println("IO Exception: ");
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @param pFile
	 */
	public void processLoadMatrixAnalysis(File pFile) {
		System.out.println(" processLoadMatrix-Analysis: " + pFile.getAbsolutePath());
		long before = System.currentTimeMillis();
		String analysisStepIDSQL = "select analysisStep_id from analysisStep where TB_analysisID = ?";
		String matrixCountSQL = "select count(*) from matrix where TB_matrixID = ?";

		final String insertMatrixSQL = "INSERT INTO TBASE2.MATRIX("
			+ "MATRIXTYPE, VERSION, MISSINGSYMBOL, GAPSYMBOL, PUBLISHED, "
			+ "TITLE, DESCRIPTION, ALIGNED, TRIANGLE, DIAGONAL, "
			+ "CASESENSITIVE, MATRIXDATATYPE_ID, CODONPOSITIONSET_ID, CHARWEIGHTSET_ID, CHARSET_ID, "
			+ "TYPESET_ID, ANCSTATESET_ID, TB_MATRIXID) VALUES" + "('S', 0, null, null, 1, "
			+ "null, null, null, null, null, " + "0, null, null, null, null, null, null, ?)";

		String insertAnalyzedDataSQL = "INSERT INTO TBASE2.ANALYZEDDATA"
			+ "(TYPE, VERSION, NOTES, INPUT, TREELENGTH, ANALYSISSTEP_ID, MATRIX_ID, PHYLOTREE_ID) VALUES"
			+ "('M', 0, null, null, null, ?, ?, null)";

		try {
			BufferedReader in = new BufferedReader(new FileReader(pFile));
			String aLine;

			// skip the first line
			aLine = in.readLine();

			int count = 0;
			int noAnalysisCount = 0;
			StringBuffer noAnalysisMatrix = new StringBuffer();
			while ((aLine = in.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(aLine, "\t\n", false);
				final String tb_matrixID = st.nextToken();
				String tb_analysisID = st.nextToken();
				assertTrue(!st.hasMoreTokens());

				// first check whether the matrix is here already:
				Object[] paraMatrixID = new Object[] {tb_matrixID};
				int matrixCount = jdbcTemplate.queryForInt(matrixCountSQL, paraMatrixID);

				if (matrixCount < 1) {
					// get the analysis Step id:
					long analysisStepID = -1;
					Object[] paraAnalysisID = new Object[] {tb_analysisID};

					try {
						analysisStepID = jdbcTemplate.queryForLong(
							analysisStepIDSQL,
							paraAnalysisID);
					} catch (IncorrectResultSizeDataAccessException ex) {
						analysisStepID = -1;
					}

					if (analysisStepID > 0) {
						// first save the matrix to db, and get the new matrix id:
						// ALERT: wrong implementation
						// it is possible same matrix associates with multiple steps.
						// cannot create multiple matices!!!
						// need to add tb_matrixID and/or tb_analysisID columns

						KeyHolder keyHolder = new GeneratedKeyHolder();
						PreparedStatementCreator psCreator = new PreparedStatementCreator() {
							public PreparedStatement createPreparedStatement(Connection con)
								throws SQLException {

								PreparedStatement ps = con.prepareStatement(
									insertMatrixSQL,
									Statement.RETURN_GENERATED_KEYS);
								ps.setString(1, tb_matrixID);

								return ps;
							};
						};

						int rowCount = jdbcTemplate.update(psCreator, keyHolder);
						long newMatrixID = keyHolder.getKey().longValue();
						assertTrue("failed to create a new matrix.", newMatrixID > 0);

						// second save the AnalyzedData to db:
						Object[] paramAnalyzedData = new Object[] {analysisStepID, newMatrixID};

						int analyzedDataCount = jdbcTemplate.update(
							insertAnalyzedDataSQL,
							paramAnalyzedData);
						assertTrue("failed to create an analyzedData.", analyzedDataCount > 0);

						// Force commit now:
						setComplete();
						endTransaction();

						System.out.println("processOneMatrix: done saving: " + tb_matrixID);
						assertTrue(rowCount > 0);
						count++;
					} else {
						System.out.println("    : no analysisStep found: tb_analysisID="
							+ tb_analysisID);
						noAnalysisCount++;
						noAnalysisMatrix.append(tb_analysisID + ", ");
					}
				} else {
					System.out.println("processOneMatrix: matrix exists: " + tb_matrixID);
				}
			}

			in.close();

			long after = System.currentTimeMillis();
			long time = (after - before) / 100;

			System.out.println(" processMatrix-Analysis Done. count=" + count + " time(s)=" + time);
			System.out.println(" matrix with no analysis step count=" + noAnalysisCount + " id ="
				+ noAnalysisMatrix.toString());

		} catch (IOException ex) {
			System.out.println("IO Exception: ");
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @param pFile
	 */
	public void processSetMatrixName(File pFile) {
		System.out.println(" processSetMatrixNames: " + pFile.getAbsolutePath());
		long before = System.currentTimeMillis();
		// String matrixIDSQL = "select matrix_id from matrix where TB_matrixID = ?";
		String matrixCountSQL = "select count(*) from matrix where TB_matrixID = ?";

		final String updateMatrixSQL = "UPDATE TBASE2.MATRIX SET TITLE= ? WHERE TB_MATRIXID= ?";

		try {
			BufferedReader in = new BufferedReader(new FileReader(pFile));
			String aLine;

			// skip the first line
			aLine = in.readLine();

			int count = 0;
			int emptyNameCount = 0;
			int noMatrixCount = 0;
			StringBuffer noMatrixNames = new StringBuffer();
			while ((aLine = in.readLine()) != null) {

				// workaround the problem that StringTokenizer does not report the empty token.
				// call this enough times to catch multipel empty tokens.
				aLine = aLine.replaceAll("\t\t", "\t \t");
				aLine = aLine.replaceAll("\t\t", "\t \t");
				aLine = aLine.replaceAll("\t\t", "\t \t");
				aLine = aLine.replaceAll("\t\t", "\t \t");

				StringTokenizer st = new StringTokenizer(aLine, "\t\n", false);
				final String tb_matrixID = st.nextToken();
				String matrixName = st.nextToken();
				matrixName.trim();

				// discard the rest of the tokens
				// assertTrue(!st.hasMoreTokens());

				if (!TreebaseUtil.isEmpty(matrixName)) {
					// first check whether the matrix is here already:
					Object[] paraMatrixID = new Object[] {tb_matrixID};
					int matrixCount = jdbcTemplate.queryForInt(matrixCountSQL, paraMatrixID);

					if (matrixCount > 0) {
						// set the matrix name:
						Object[] paramMatrix = new Object[] {matrixName, tb_matrixID};

						int updateCount = jdbcTemplate.update(updateMatrixSQL, paramMatrix);
						assertTrue("failed to update the matrix name.", updateCount > 0);

						// Force commit now:
						setComplete();
						endTransaction();

						System.out.println("matrix updated: tb_matrixID = " + tb_matrixID
							+ " title=" + matrixName);
						count++;
					} else {
						System.out.println("    : no matrix found: tb_matrixID=" + tb_matrixID);
						noMatrixCount++;
						noMatrixNames.append(tb_matrixID + ", ");
					}
				} else {
					emptyNameCount++;
					System.out.println("     : matrix name is empty: " + tb_matrixID);
				}
			}
			in.close();

			long after = System.currentTimeMillis();
			long time = (after - before) / 100;

			System.out.println(" processUpdate Matrix Name Done. count=" + count + " time(s)="
				+ time);
			System.out.println("   Matrix with empty names count =" + emptyNameCount);
			System.out.println(" no matrix found count=" + noMatrixCount + " tb_matrixID ="
				+ noMatrixNames.toString());

		} catch (IOException ex) {
			System.out.println("IO Exception: ");
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @param pDirectory
	 */
	public void processLoadNexus(File pDirectory) {
		System.out.println(" processLoadNexus " + pDirectory.getAbsolutePath());
		long before = System.currentTimeMillis();

		File[] files = pDirectory.listFiles();

		int parsedCount = 0;
		int errCount = 0;
		StringBuffer errFileNames = new StringBuffer();
		StringBuffer noMatrix = new StringBuffer();

		// open read nexus server:
		PhyloDataset cipresData = null;
		try {
			cipresData = new PhyloDataset(PhyloDataset.MULTI_USE_READER);
		} catch (Exception ex) {
			cipresData = null;
			ex.printStackTrace();
		}

		if (cipresData != null) {
			for (int i = 0; i < files.length; i++) {
				File f = files[i];

				if (f.isFile()) {
					// treat as NEXUS file:
					try {
						boolean parsed = loadOneNexus(f, noMatrix, cipresData);

						if (parsed) {
							parsedCount++;
							System.out.println("nexus loaded: tb_matrixID = " + f.getName());
						} else {
							System.out.println("   : not loaded = " + f.getName());
						}
					} catch (Exception ex) {
						errCount++;
						errFileNames.append(f.getName() + ", ");
						System.out.println("   : failed to load w exception = " + f.getName());
						System.out.println(ex.getMessage());
						System.out.println();
					}
				}
			}

			cipresData.closeReader();
		}

		long after = System.currentTimeMillis();
		long time = (after - before) / 100;

		System.out.println(" process load NEXUS Done. parsed count=" + parsedCount + " time(s)="
			+ time);
		System.out.println(" no matrix found: tb_matrixID =" + noMatrix.toString());
		System.out.println(" err count=" + errCount + " tb_matrixID =" + errFileNames.toString());

	}

	/**
	 * 
	 * @param pFile
	 * @param pNoMatrix
	 * @return
	 */
	private boolean loadOneNexus(File pFile, StringBuffer pNoMatrix, PhyloDataset pCipresData)
		throws Exception {

		boolean loaded = false;
		String matrixCountSQL = "select count(*) from matrix where TB_matrixID = ?";
		String matrixColumnCountSQL = "select count(*) from matrixColumn where matrix_ID = ?";
		String matrixIDSQL = "select matrix_id from matrix where TB_matrixID = ?";
		String analysisStepIDSQL = "select analysisstep_ID from analyzeddata where MATRIX_ID = ?";

		// String insertAnalyzedTreeSQL = "INSERT INTO TBASE2.ANALYZEDDATA"
		// + "(TYPE, VERSION, NOTES, INPUT, TREELENGTH, ANALYSISSTEP_ID, MATRIX_ID, PHYLOTREE_ID)
		// VALUES"
		// + "('T', 0, null, null, null, ?, null, ?)";

		// tb_matrixID is filename.
		String tb_matrixID = pFile.getName();

		// first check whether the matrix is here already:
		Object[] paraTBMatrixID = new Object[] {tb_matrixID};
		int matrixCount = jdbcTemplate.queryForInt(matrixCountSQL, paraTBMatrixID);

		if (matrixCount > 0) {
			// get matrix_id:
			long matrixID = jdbcTemplate.queryForLong(matrixIDSQL, paraTBMatrixID);
			assertTrue("failed to load matrix id", matrixID > 0);

			// next check whether the matrix is empty:
			Object[] paraMatrixID = new Object[] {matrixID};
			int columnCount = jdbcTemplate.queryForInt(matrixColumnCountSQL, paraMatrixID);

			if (columnCount < 1) {

				// ALERT: need to handle one matrix assoicates with multiple steps???

				// third check whether matrix associates with analysis step:
				long analysisStepID = -1;
				try {
					analysisStepID = jdbcTemplate.queryForLong(analysisStepIDSQL, paraMatrixID);
				} catch (IncorrectResultSizeDataAccessException ex) {
					analysisStepID = -1;

					System.out.println("   : no associated analysisStep  matrixID = " + matrixID);
				}

				if (analysisStepID > 0) {

					AnalysisStep step = (AnalysisStep) loadObject(
						AnalysisStep.class,
						analysisStepID);
					assertTrue("Failed to load Analysis step", step != null);
					Study study = step.getAnalysis().getStudy();
					assertTrue("Failed to load study", study != null);

					// Parse the nexus file!
					// Clear phylodataset:
					clear(pCipresData);

					pCipresData.initialize(pFile);

					NexusDataSet data = new NexusDataSet();

					NCLNexusConverter converter = new NCLNexusConverter();
					converter.setMatrixDataTypeHome(getMatrixDataTypeHome());
					converter.setTaxonLabelHome(getTaxonLabelHome());
					converter.parsePhyloDataSet(study, data, pCipresData);

					List<Matrix> matrices = data.getMatrices();
					assertTrue("One NEXUS file, one matrix", matrices.size() == 1);
					StandardMatrix m = (StandardMatrix) matrices.get(0);
					m.setMatrixID(matrixID);

					// for the sake of hibernate cannot handle null boolean values:
					m.setAligned(true);
					m.setCaseSensitive(false);

					// Now, for each tree, create one analyzedData.
					List<PhyloTree> trees = data.getPhyloTrees();
					hibernateTemplate.saveOrUpdateAll(data.getTaxonLabels());
					Matrix savedMatrix = (Matrix) hibernateTemplate.merge(m);

					for (PhyloTree tree : trees) {
						AnalyzedTree analyzedTree = new AnalyzedTree();
						analyzedTree.setTree(tree);
						step.addAnalyzedData(analyzedTree);

						tree.setPublished(true);

						hibernateTemplate.save(tree);
						hibernateTemplate.save(analyzedTree);
					}
					hibernateTemplate.update(step);

					// Force commit now:
					setComplete();
					endTransaction();

					// System.out.println("matrix updated: matrixID = " + matrixID + " title="
					// + matrixName);

					// clear data:
					data.clearData();
					hibernateTemplate.clear();

					// Open next session:
					onSetUp();

					loaded = true;
				}
			}
		} else {
			pNoMatrix.append(tb_matrixID + ", ");
		}
		return loaded;
	}

	/**
	 * 
	 * @param pCiresData
	 */
	private void clear(PhyloDataset pCipresData) {
		pCipresData.setDataMatrix(null);
		pCipresData.setTaxaInfo(null);
		pCipresData.setTrees(null);
	}

	/**
	 * 
	 */
	public void processTestCitation() {

		// show db and jdbc version
		try {
			DatabaseMetaData metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();

			int dbVersionMajor = metaData.getDatabaseMajorVersion();
			int dbVersionMinor = metaData.getDatabaseMinorVersion();
			int jdbcVersionMajor = metaData.getJDBCMajorVersion();
			int jdbcVersionMinor = metaData.getJDBCMinorVersion();

			System.out.println("JDBC version: " + jdbcVersionMajor + "." + jdbcVersionMinor);
			System.out.println("DB version: " + dbVersionMajor + "." + dbVersionMinor);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void processTestStudy() {
		System.out.println("TODO: processTestStudy");
	}

	/**
	 * 
	 */
	public void processTestAnalysis() {
		System.out.println("TODO: processTestAnalysis");
	}

	/**
	 * 
	 * @param pF
	 */
	public void processLoadNexusMesquite(File pFile) {
		System.out.println(" processLoadNexusMesquite " + pFile.getAbsolutePath());
		long before = System.currentTimeMillis();

		// int parsedCount = 0;
		// int errCount = 0;
		// StringBuffer errFileNames = new StringBuffer();
		// StringBuffer noMatrix = new StringBuffer();

		// read nexus using Mesquite:
		MesquiteConverter converter = new MesquiteConverter();
		converter.setMatrixDataTypeHome(getMatrixDataTypeHome());
		converter.setItemDefinitionHome(getItemDefinitionHome());
		converter.setTaxonLabelHome(getTaxonLabelHome());

		NexusDataSet dataSet = new NexusDataSet();
		Study study = null;
		Collection<File> files = new HashSet<File>();
		files.add(pFile);

		try {
			converter.processLoadFile(files, study, dataSet, null);

			// TODO study.printNexus();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		long after = System.currentTimeMillis();
		long time = (after - before) / 100;

		System.out.println(" process load NEXUS Done. parsed count=" + " time(s)=" + time);
		// System.out.println(" no matrix found: tb_matrixID =" + noMatrix.toString());
		// System.out.println(" err count=" + errCount + " tb_matrixID =" +
		// errFileNames.toString());

	}

}
