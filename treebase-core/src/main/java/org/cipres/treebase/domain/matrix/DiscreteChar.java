/*
 * CIPRES Copyright (c) 2005- 2006, The Regents of the University of California All rights reserved.
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
package org.cipres.treebase.domain.matrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import mesquite.lib.StringUtil;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import org.cipres.treebase.TreebaseUtil;

/**
 * ContinuousChar.java
 * 
 * Created on Mar 27, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("D")
public class DiscreteChar extends PhyloChar {

	private static final long serialVersionUID = 834258437080501833L;
	private static final Logger LOGGER = Logger.getLogger(DiscreteChar.class);

	private Set<DiscreteCharState> mCharStates = new HashSet<DiscreteCharState>();

	//Transient
	private List<DiscreteCharState> mCharStateList;

	/**
	 * Constructor.
	 */
	public DiscreteChar() {
		super();
	}

	/**
	 * Return the CharStates field.
	 * 
	 * Note: it is an internal method. Please do not use. 
	 * 
	 * @return Set<DiscreteCharState>
	 */
	@OneToMany(mappedBy = "char", cascade = CascadeType.ALL)
	@Fetch(FetchMode.SUBSELECT)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matrixCache")
	public Set<DiscreteCharState> getCharStates() {
		return mCharStates;
	}

	/**
	 * Set the CharStates field.
	 */
	protected void setCharStates(Set<DiscreteCharState> pNewCharStates) {
		mCharStates = pNewCharStates;
	}

	/**
	 * Return a read only set of character states.
	 * 
	 * @return
	 */
	@Transient
	public Set<DiscreteCharState> getCharStatesReadOnly() {
		return Collections.unmodifiableSet(getCharStates());
	}

	/**
	 * Append a state to the end of the list. Manage bi-directional relationship.
	 * 
	 * Creation date: Feb 22, 2006 12:06:25 PM
	 * 
	 * @param pCharState
	 */
	public void addCharState(DiscreteCharState pCharState) {
		if (pCharState != null && !getCharStates().contains(pCharState)) {
			getCharStates().add(pCharState);
			pCharState.setChar(this);
		}
	}

	/**
	 * Remove a state. Manage bi-directional relationship.
	 * 
	 * Creation date: Feb 22, 2006 12:06:25 PM
	 * 
	 * @param pCharState
	 */
	public void removeCharState(DiscreteCharState pCharState) {
		if (pCharState != null && getCharStates().contains(pCharState)) {
			getCharStates().remove(pCharState);
			pCharState.setChar(null);
		}
	}

	/**
	 * Return a char states based on the symbol.
	 * 
	 * Returns null if no match is found in the character states.
	 * 
	 * Deprecated, use getStateBySymbol instead.
	 * @param pDescription
	 * @return
	 */
	@Transient
	@Deprecated 
	public DiscreteCharState getStateByDescription(String pDescription) {

		DiscreteCharState state = null;

		if (!TreebaseUtil.isEmpty(pDescription)) {
			boolean foundMatch = false;
			Set<DiscreteCharState> charStates = getCharStates();

			for (Iterator<DiscreteCharState> iter = charStates.iterator(); iter.hasNext()
				&& !foundMatch;) {

				DiscreteCharState element = iter.next();
				//ALERT: need to handle null description...
				if ((element.getDescription() != null) && element.getDescription().compareToIgnoreCase(pDescription) == 0) {
					state = element;
					foundMatch = true;
				}

			}
		}

		// bail out if a match is not found. Cannot assemble the list:
		if (state == null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("getStateByDescription(String) - no match found!"); //$NON-NLS-1$
				LOGGER.debug(" desc: " + pDescription + " states count: " + getCharStates().size());
			}
		}
		return state;
	}

	/**
	 * Return a list of char states based on the symbols.
	 * 
	 * Returns null if any symbol is not matched in the character states.
	 * 
	 * @param pSymbols
	 * @return
	 */
	@Transient
	public List<DiscreteCharState> getStatesBySymbol(String pSymbols) {

		List<DiscreteCharState> states = new ArrayList<DiscreteCharState>();
		String errorSymbol = null;

		if (!TreebaseUtil.isEmpty(pSymbols)) {
			Set<DiscreteCharState> charStates = getCharStates();
			for (int i = 0; i < pSymbols.length() && (errorSymbol == null); i++) {
				String aChar = pSymbols.substring(i, i + 1);

				boolean foundMatch = false;
				for (Iterator<DiscreteCharState> iter = charStates.iterator(); iter.hasNext()
					&& !foundMatch;) {

					DiscreteCharState element = iter.next();
					String base = element.getDescription();
					if (element.getSymbol() != null) {
						base = element.getSymbol().toString();
					}
					
					if (base.compareToIgnoreCase(aChar) == 0) {
						states.add(element);
						foundMatch = true;
					}
				}

				// bail out if a match is not found. Cannot assemble the list:
				if (!foundMatch) {
					errorSymbol = aChar;
					states = null;
				}
			}
		}

		if (states == null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("getStatesBySymbol(String) - no match found!"); //$NON-NLS-1$
				LOGGER.info(" symbols: " + pSymbols + " err Symbol: " + errorSymbol);
			}
		}

		return states;
	}

	/**
	 * Create a list of char states to generate "STATELABEL" for nexus file. This property is not
	 * persisted in the database. It is a transient property to make sure the same statelabel list
	 * is used for the entire matrix.
	 * 
	 * @return the charStateList
	 */
	@Transient
	public List<DiscreteCharState> getCharStateList() {
		if (mCharStateList == null) {
			mCharStateList = new ArrayList<DiscreteCharState>(getCharStates());
		}
		return mCharStateList;
	}

	/**
	 * Generate Nexus style CharStateLabels information for this char. 
	 * Do not generate content if neither character name nor state labels are defined.
	 * The state labels are ordered by the symbols string. 
	 * 
	 * @param pSymbols order character states based on the symbols. 
	 * @return whether content is generated.
	 */
	public String generateCharLabelsInfo(String pSymbols) {

		StringBuilder builder = new StringBuilder();
		
		//handling old matrix that do not have symbols generated:
		// the old matrices need to be reparsed:
		if (TreebaseUtil.isEmpty(pSymbols)) {
			return builder.toString();
		}
		
		Set<DiscreteCharState> charStates = getCharStates();
		boolean found = false;
		boolean hasMoreSymbol = true;
		boolean isFirst = true;
		for (int i = 0; i < pSymbols.length() && hasMoreSymbol; i++) {
			char aChar = pSymbols.charAt(i);
			//ALERT: to be nice, skip the empty space, although none should be there:
			if (aChar == ' ') {
				continue;
			}
			
			found = false;
			
			//search all states to find a match:
			Iterator<DiscreteCharState> stateIter = charStates.iterator();
			while (stateIter.hasNext() && !found) {
				DiscreteCharState aCharState = (DiscreteCharState) stateIter.next();
				
				if (aChar == aCharState.getSymbol()) {
					found = true;
					if (!TreebaseUtil.isEmpty(aCharState.getDescription())) {
						if (!isFirst) {
							builder.append(' ');
						}
						isFirst = false;
						builder.append(StringUtil.tokenize(aCharState.getDescription()));
					}
				}
			}

			//stop once the symbol is not found, there is no more symbol. 
			if (!found) {
				hasMoreSymbol = false;
			}
			
		}
		
		return builder.toString();
		
	}

}
