
package org.cipres.treebase.service.matrix;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.matrix.CharacterHome;
import org.cipres.treebase.domain.matrix.CharacterService;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * CharacterServiceImpl.java
 * 
 * Created on Jun 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class CharacterServiceImpl extends AbstractServiceImpl implements CharacterService {

	private CharacterHome mCharacterHome;

	/**
	 * Constructor.
	 */
	public CharacterServiceImpl() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.service.AbstractServiceImpl#getDomainHome()
	 */
	@Override
	protected DomainHome getDomainHome() {
		return getCharacterHome();
	}

	/**
	 * Return the CharacterHome field.
	 * 
	 * @return CharacterHome mCharacterHome
	 */
	private CharacterHome getCharacterHome() {
		return mCharacterHome;
	}

	/**
	 * Set the CharacterHome field.
	 */
	public void setCharacterHome(CharacterHome pNewCharacterHome) {
		mCharacterHome = pNewCharacterHome;
	}

	@Override
	public Class defaultResultClass() {
		return Character.class;
	}

}
