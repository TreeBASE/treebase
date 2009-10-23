/*
 * CIPRES Copyright (c) 2005- 2007, The Regents of the University of California All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. *
 * Neither the name of the University of California or the San Diego Supercomputer Center nor the
 * names of its contributors may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.cipres.treebase.dao.study;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.study.Algorithm;
import org.cipres.treebase.domain.study.AlgorithmHome;

/**
 * @author madhu
 * 
 */
public class AlgorithmDAO extends AbstractDAO implements AlgorithmHome {

	/**
	 * Constructor.
	 */
	public AlgorithmDAO() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cipres.treebase.domain.study.AlgorithmHome#finalAllUniqueAlgorithmDescriptions()
	 */
	public List<String> findAllUniqueAlgorithmDescriptions() {

		// case insensitive search
		String sql = "select distinct lower(alg.description) from Algorithm alg";
		// String sql = "select distinct alg.description from Algorithm alg";
		Query q = getSession().createQuery(sql);
		List<String> returnVal = q.list();
		return returnVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cipres.treebase.domain.study.AlgorithmHome#findByDescription(java.lang.String)
	 */
	public Algorithm findByDescription(String pDesc) {
		Algorithm returnVal = null;

		if (!TreebaseUtil.isEmpty(pDesc)) {
			Criteria c = getSession().createCriteria(Algorithm.class);
			c.add(Expression.eq("description", pDesc).ignoreCase());

			returnVal = (Algorithm) c.uniqueResult();
		}
		return returnVal;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.AlgorithmHome#findAllUniqueOtherAlgorithmDescriptions()
	 */
	public List<String> findAllUniqueOtherAlgorithmDescriptions(String pPartialName) {
		// case insensitive search
		String sql = "select distinct description  from OtherAlgorithm where lower(description) like :mStr";
		// String sql = "select distinct alg.description from OtherAlgorithm alg";
		Query q = getSession().createQuery(sql);
		q.setString("mStr", pPartialName.trim().toLowerCase() + '%');
		List<String> returnVal = q.list();
		return returnVal;

	}

}
