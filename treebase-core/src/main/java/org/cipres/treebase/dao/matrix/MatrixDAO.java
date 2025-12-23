
package org.cipres.treebase.dao.matrix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.dao.jdbc.MatrixJDBC;
import org.cipres.treebase.domain.matrix.AncStateSet;
import org.cipres.treebase.domain.matrix.CharWeightSet;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.GeneticCodeSet;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixColumnHome;
import org.cipres.treebase.domain.matrix.MatrixElement;
import org.cipres.treebase.domain.matrix.MatrixHome;
import org.cipres.treebase.domain.matrix.MatrixKind;
import org.cipres.treebase.domain.matrix.MatrixRow;
import org.cipres.treebase.domain.matrix.MatrixRowHome;
import org.cipres.treebase.domain.matrix.TypeSet;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.AnalyzedDataHome;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionHome;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;

/**
 * MatrixDAO.java
 * 
 * Created on Apr 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class MatrixDAO extends AbstractDAO implements MatrixHome {

	private static final Logger LOGGER = LogManager.getLogger(MatrixDAO.class);

	private MatrixColumnHome mMatrixColumnHome;
	private MatrixRowHome mMatrixRowHome;
	private SubmissionHome mSubmissionHome;
	private AnalyzedDataHome mAnalyzedDataHome;
	private TaxonLabelHome mTaxonLabelHome;
	
	/**
	 * Constructor.
	 */
	public MatrixDAO() {
		super();
	}

	/**
	 * Return the MatrixRowHome field.
	 * 
	 * @return MatrixRowHome mMatrixRowHome
	 */
	private MatrixRowHome getMatrixRowHome() {
		return mMatrixRowHome;
	}

	/**
	 * Set the MatrixRowHome field.
	 */
	public void setMatrixRowHome(MatrixRowHome pNewMatrixRowHome) {
		mMatrixRowHome = pNewMatrixRowHome;
	}

	/**
	 * Return the MatrixColumnHome field.
	 * 
	 * @return MatrixColumnHome mMatrixColumnHome
	 */
	private MatrixColumnHome getMatrixColumnHome() {
		return mMatrixColumnHome;
	}

	/**
	 * Set the MatrixColumnHome field.
	 */
	public void setMatrixColumnHome(MatrixColumnHome pNewMatrixColumnHome) {
		mMatrixColumnHome = pNewMatrixColumnHome;
	}

	/**
	 * Return the AnalyzedDataHome field.
	 * 
	 * @return AnalyzedDataHome mAnalyzedDataHome
	 */
	private AnalyzedDataHome getAnalyzedDataHome() {
		return mAnalyzedDataHome;
	}

	/**
	 * Set the AnalyzedDataHome field.
	 */
	public void setAnalyzedDataHome(AnalyzedDataHome pNewAnalyzedDataHome) {
		mAnalyzedDataHome = pNewAnalyzedDataHome;
	}

	/**
	 * set the TaxonLabelHome field.
	 */
	public void setTaxonLabelHome(TaxonLabelHome pNewTaxonLabelHome) {
		mTaxonLabelHome = pNewTaxonLabelHome;
	}

	/**
	 * Return the TaxonLabelHome field.
	 * 
	 * 
	 * @return TaxonLabelHome mTaxonLabelHome
	 */
	private TaxonLabelHome getTaxonLabelHome() {
		return mTaxonLabelHome;
	}
	
	
	
	/**
	 * Return the SubmissionHome field.
	 * 
	 * @return SubmissionHome mSubmissionHome
	 */
	private SubmissionHome getSubmissionHome() {
		return mSubmissionHome;
	}

	/**
	 * Set the SubmissionHome field.
	 */
	public void setSubmissionHome(SubmissionHome pNewSubmissionHome) {
		mSubmissionHome = pNewSubmissionHome;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#delete(java.util.Collection)
	 */
	public void delete(Collection<Matrix> pMatrices) {
		if (pMatrices == null || pMatrices.isEmpty()) {
			return;
		}

		for (Matrix matrix : pMatrices) {
			delete(matrix);
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#cascadeDelete(org.cipres.treebase.domain.matrix.CharacterMatrix)
	 */
	public void cascadeDelete(CharacterMatrix pMatrix) {
		
		// bi-directional relationships:
		// * delete submission-> matrix
		// * delete analyzedData -> matrix
		Submission sub = getSubmissionHome().findByMatrix(pMatrix);
		if (sub != null) {
			sub.removeMatrix(pMatrix);
		}

		Collection<AnalyzedData> dataLink = getAnalyzedDataHome().findByMatrix(pMatrix);
		for (AnalyzedData data : dataLink) {
			data.getAnalysisStep().removeAnalyzedData(data);
		}
		
		TaxonLabelSet tSet = pMatrix.getTaxa();
		List<TaxonLabel> tList = pMatrix.getAllTaxonLabels();
      
		getHibernateTemplate().delete(pMatrix);  
		
		getTaxonLabelHome().clean(tSet);
		getTaxonLabelHome().clean(tList);
		
	}
	
	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#delete(org.cipres.treebase.domain.matrix.Matrix)
	 */
	public void delete(Matrix pMatrix) {
		if (pMatrix != null && pMatrix.getId() != null) {

			// Use double dispatch.
			pMatrix.cascadeDelete(this);
						
		
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#cascadeDeleteColumns(org.cipres.treebase.domain.matrix.CharacterMatrix)
	 */
	public void cascadeDeleteColumns(CharacterMatrix pMatrix) {
		// getMatrixColumnHome().deleteColumns(pColumns);

		// delete all matrix columns by direct JDBC:
		// * delete all matrixColumn_itemDefinitions
		// * delete all columns
		MatrixJDBC.deleteMatrixColumnSQL(pMatrix, getSession());
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#cascadeDeleteRows(java.util.Collection)
	 */
	public void cascadeDeleteRows(Collection<MatrixRow> pRows) {
		getMatrixRowHome().deleteRows(pRows);
	}

	
	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#cascadeDeleteRows(java.util.Collection)
	 */
	public void cascadeDeleteRows(CharacterMatrix pMatrix) {
		MatrixJDBC.deleteMatrixRowSQL(pMatrix,getSession());
	}
	
	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#cascadeDeleteAncStateSet(java.util.Set)
	 */
	public void cascadeDeleteAncStateSet(Set<AncStateSet> pAncStateSets) {
	// FIXME: cascadeDeleteAncStateSet

	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#cascadeDeleteCharWeightSet(java.util.Set)
	 */
	public void cascadeDeleteCharWeightSet(Set<CharWeightSet> pWeightSets) {
	// FIXME: cascadeDeleteCharWeightSet

	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#cascadeDeleteCodeSet(java.util.Set)
	 */
	public void cascadeDeleteCodeSet(Set<GeneticCodeSet> pCodeSets) {
	// FIXME: cascadeDeleteCodeSet

	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#cascadeDeleteTypeSet(java.util.Set)
	 */
	public void cascadeDeleteTypeSet(Set<TypeSet> pTypeSets) {
	// FIXME: cascadeDeleteTypeSet

	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#loadMatrixEagerFetch(java.lang.Long)
	 */
	public Matrix loadMatrixEagerFetch(Long pMatrixID) {

		Matrix matrix = super.findPersistedObjectByID(Matrix.class, pMatrixID);

		// FIXME: loadMatrixEagerFetch
		// Criteria c = getSession().createCriteria(Matrix.class);
		// c.add(Expression.eq("id", pMatrixID)).setFetchMode("rows", FetchMode.JOIN).setFetchMode(
		// "columns",
		// FetchMode.JOIN);
		//		
		// Matrix uniqueResult = (Matrix) c.uniqueResult();

		return matrix;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#cascadeDeleteElements(org.cipres.treebase.domain.matrix.CharacterMatrix)
	 */
	public void cascadeDeleteElements(CharacterMatrix pMatrix) {
		// delete all matrix elements by direct JDBC:
		// * delete all state modifiers
		// * delete all item values
		// * delete all compound_element
		// * delete all elements
		MatrixJDBC.deleteMatrixElementSQL(pMatrix, getSession());

	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#persistAll(java.util.List)
	 */
	public void persistAll(List<Matrix> pMatrices) {
		if (pMatrices == null || pMatrices.isEmpty()) {
			return;
		}

		for (Matrix matrix : pMatrices) {
			if (matrix.getId() == null) {

				long timeStart = System.currentTimeMillis();
				getHibernateTemplate().persist(matrix);

				// Use double dispatch.
				matrix.cascadePersist(this);

				// bi-directional relationships:
				// * persist submission-> matrix
				// * persist analyzedData -> matrix
				// Submission sub = getSubmissionHome().findByMatrix(pMatrix);
				// if (sub != null) {
				// sub.removeMatrix(pMatrix);
				// }
				//
				// Collection<AnalyzedData> dataLink = getAnalyzedDataHome().findByMatrix(pMatrix);
				// for (AnalyzedData data : dataLink) {
				// data.getAnalysisStep().removeAnalyzedData(data);
				// }

				if (LOGGER.isDebugEnabled()) {
					long timeEnd = System.currentTimeMillis();
					LOGGER.debug("matrix saving time=" + (timeEnd - timeStart) / 1000); //$NON-NLS-1$
				}

			}
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#cascadePersistElements(org.cipres.treebase.domain.matrix.CharacterMatrix)
	 */
	public void cascadePersistElements(CharacterMatrix pCharacterMatrix) {

		// Batch insert of all elements:
		// int count = 0;

		// TODO: need disable second level cache.
		// Collection<MatrixElement> elements = new ArrayList<MatrixElement>();

		Iterator<MatrixRow> rows = pCharacterMatrix.getRowsReadOnly().iterator();
		while (rows.hasNext()) {
			Iterator<MatrixElement> elementIter = rows.next().getElementIterator();
			while (elementIter.hasNext()) {
				MatrixElement elem = (MatrixElement) elementIter.next();
				getHibernateTemplate().persist(elem);
			}
		}

	}

	// /**
	// *
	// * @see
	// org.cipres.treebase.domain.matrix.MatrixHome#cascadePersitElements(org.cipres.treebase.domain.matrix.CharacterMatrix)
	// */
	// public void cascadePersitElements(CharacterMatrix pCharacterMatrix) {
	//
	// //Batch insert of all elements:
	// int count = 0;
	// //Session session = getSession();
	//		
	// Problem: cannot use flush here, since matrixRow has a collection of transient elements!
	// getSession().flush();
	//		
	// StatelessSession session = getSessionFactory().openStatelessSession();
	// Transaction tx = session.beginTransaction();
	//		
	// //TODO: need disable second level cache.
	// Iterator<MatrixRow> rows = pCharacterMatrix.getRowsReadOnly().iterator();
	// while (rows.hasNext()) {
	//			
	// Iterator<MatrixElement> elementIter = rows.next().getElementIterator();
	// while (elementIter.hasNext()) {
	// MatrixElement elem = (MatrixElement) elementIter.next();
	// session.insert(elem);
	//
	// count++;
	// // if (count % DomainHome.JDBC_BATCH_SIZE == 0) {
	// // //flush a batch of inserts and release memory:
	// // session.flush();
	// // session.clear();
	// // }
	//				
	// }
	// }
	//		
	// tx.commit();
	// session.close();
	// }

	/**
	 * 
	 * @see org.cipres.treebase.domain.tree.PhyloTreeHome#findByStudies(java.util.List)
	 */
	public Collection<Matrix> findByStudies(Collection<Study> pStudies) {

		// FIXME: findByStudies
		Collection<Matrix> returnVal = new ArrayList<Matrix>();

		if (pStudies != null && !pStudies.isEmpty()) {
			String query = "select m from Matrix m where study in (:studies)";

			Query q = getSession().createQuery(query);

			q.setParameterList("studies", pStudies);
			List results = q.list();

			returnVal.addAll(results);
		}
		return returnVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#findMatricesByTitle(java.lang.String)
	 */
	public Set<Matrix> findMatricesByTitle(String title) {
		Set<Matrix> returnVal = new HashSet<Matrix>();
		String query = "select m from Matrix m where title = :title";

		Query q = getSession().createQuery(query);

		q.setParameter("title", title);
		List results = q.list();

		returnVal.addAll(results);
		return returnVal;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#findByNexusFileName(java.lang.Long,
	 *      java.lang.String)
	 */
	public Collection<Matrix> findByNexusFileName(Long pSubmissionId, String pFileName) {

		StringBuffer query = new StringBuffer(
			"select m from Submission s join s.submittedMatrices m")
			.append(" where s.id = :submissionId and m.nexusFileName = :nexusFileName");

		Query q = getSession().createQuery(query.toString());

		q.setParameter("submissionId", pSubmissionId);
		q.setParameter("nexusFileName", pFileName);

		List results = q.list();
		return results;
	}

	// TODO: It's confusing that this method and the previous one are so similar.
	// Clean this up. 20080327 mjd
	public Collection<Matrix> findByNexusFile(String fn) {
		Query q = getSession().createQuery("from Matrix where nexusfilename = :fn");
		q.setParameter("fn", fn);
		return q.list();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixService#findByTB1MatrixID(java.lang.String)
	 */
	public Collection<Matrix> findByTB1MatrixID(String pTB1MatrixID) {
		if ( pTB1MatrixID == null ) {
			return null;
		}
		Query q = getSession().createQuery("from Matrix where tb_matrixid = :tbmid");
		q.setParameter("tbmid", pTB1MatrixID);
		return q.list();		
	}	
	
	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#findRowAsString(org.cipres.treebase.domain.matrix.CharacterMatrix,
	 *      int, int)
	 */
	public List<String> findRowAsString(CharacterMatrix pMatrix, int pStart, int pEnd) {
		List<String> returnVal = new ArrayList<String>();

		if (pMatrix == null || pStart > pEnd) {
			return returnVal;
		}

		// first do range checking:
		List<MatrixRow> rows = pMatrix.getRowsReadOnly();
		int rowCount = rows.size();
		int colCount = pMatrix.getColumnsReadOnly().size();

		int start = pStart;
		if (start < 0) {
			start = 0;
		}

		int end = pEnd;
		if (end > colCount) {
			end = colCount;
		}

		if (end < start) {
			end = start;
		}

		String sql = "select {e.*}, e.matrixrow_id as rowId, e.element_order as elemIndex from matrixelement e join matrixrow r on r.MATRIXROW_ID = e.MATRIXrow_id "
			+ "where r.MATRIX_ID = :matrixID and e.ELEMENT_ORDER between :startIndex and :endIndex order by e.MATRIXROW_ID, e.ELEMENT_ORDER";

		List results = getSession()
			.createSQLQuery(sql).addEntity("e", MatrixElement.class).addScalar(
				"rowId",
				StandardBasicTypes.LONG).addScalar("elemIndex", StandardBasicTypes.INTEGER).setLong(
				"matrixID",
				pMatrix.getId()).setInteger("startIndex", start).setInteger("endIndex", end).list();

		long tmpRowId = 0;
		boolean isInitial = true;
		List<List<MatrixElement>> rowElements = new ArrayList<List<MatrixElement>>();
		List<MatrixElement> aRowElements = new ArrayList<MatrixElement>();

		for (Object result : results) {
			Object[] resultArray = (Object[]) result;

			MatrixElement e = (MatrixElement) resultArray[0];
			Long rowID = (Long) resultArray[1];
			int elementIndex = (Integer) resultArray[2];

			if (isInitial) {
				tmpRowId = rowID;
				isInitial = false;
			} else if (rowID > tmpRowId) {
				tmpRowId = rowID;

				// start the next row:
				rowElements.add(aRowElements);
				aRowElements = new ArrayList<MatrixElement>();
			}

			aRowElements.add(e);
		}

		// don't forget the last row:
		rowElements.add(aRowElements);

		for (List<MatrixElement> elements : rowElements) {

			String aRowString = MatrixRow.buildElementAsString(elements);
			returnVal.add(aRowString);
		}

		// Map<MatrixRow, MatrixElement> elementsByRowIDMap = new HashMap<Long, MatrixElement>();
		//
		// for (MatrixElement matrixElement : allElements) {
		// elementsByRowIDMap.put(matrixElement.getRow(), matrixElement);
		// }

		// for (MatrixRow matrixRow : rows) {
		//			
		// List<MatrixElement> elements = getSession().createFilter(matrixRow.getElements(), "")
		// .setFirstResult(start).setMaxResults(end-start+1).list();
		//			
		// String aRowString = matrixRow.buildElementAsString(elements);
		// returnVal.add(aRowString);
		// }
		return returnVal;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#updatePublishedFlagByStudy(org.cipres.treebase.domain.study.Study,
	 *      boolean)
	 */
	public int updatePublishedFlagByStudy(Study pStudy, boolean pPublished) {
		if (pStudy == null) {
			return 0;
		}

		String query = "update Matrix m set m.published = :pub where m.study = :study";

		Query q = getSession().createQuery(query);
		q.setBoolean("pub", pPublished);
		q.setParameter("study", pStudy);

		return q.executeUpdate();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#findKindByDescription(java.lang.String)
	 */
	public MatrixKind findKindByDescription(String pDescription) {
		if (TreebaseUtil.isEmpty(pDescription)) {
			return null;
		}

		MatrixKind returnVal = null;

		Criteria c = getSession().createCriteria(MatrixKind.class).add(
			org.hibernate.criterion.Expression.eq("description", pDescription));

		returnVal = (MatrixKind) c.uniqueResult();
		return returnVal;
	}

	/*
	 * (non-Javadoc)
	 * @see org.cipres.treebase.domain.matrix.MatrixHome#findByTB1StudyID(java.lang.String)
	 */
	public Matrix findByTB1StudyID(String pTB1MatrixID) {
		Criteria c = getSession().createCriteria(Matrix.class);
		c.add(Expression.eq("TB1MatrixID", pTB1MatrixID));
		return (Matrix) c.uniqueResult();
	}
}
