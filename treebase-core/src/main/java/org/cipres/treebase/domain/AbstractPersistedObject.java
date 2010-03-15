
package org.cipres.treebase.domain;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.cipres.treebase.Constants;
import org.cipres.treebase.NamespacedGUID;
import org.cipres.treebase.PhyloWSPath;
import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseUtil;
import org.hibernate.annotations.GenericGenerator;

/**
 * The abstract super class for all persisted domain objects.
 * 
 * Created on Sep 27, 2005
 * 
 * @author Jin Ruan
 * 
 */
@SuppressWarnings("serial")
@MappedSuperclass
public class AbstractPersistedObject implements TBPersistable, Serializable {

	public static final Comparator<AbstractPersistedObject> COMPARATOR_ID = new Comparator<AbstractPersistedObject>() {
		
		public int compare(AbstractPersistedObject pObject1, AbstractPersistedObject pObject2) {
			Long id1 = pObject1.getId();
			Long id2 = pObject2.getId();
			return id1.compareTo(id2);
		}
	
	};
	
	private Long mId;
	private int mVersion;

	/**
	 * Constructor.
	 */
	public AbstractPersistedObject() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.TBPersistable#getId()
	 */
	@Id
	@GenericGenerator(name="TB2SEQGEN", strategy = "org.cipres.treebase.domain.TB2SequenceGenerator")	  
	@GeneratedValue(generator="TB2SEQGEN")
		public Long getId() {
		return mId;
	}

	/**
	 * Set the Id field.
	 */
	public void setId(Long pNewId) {
		mId = pNewId;
	}

	/**
	 * Return the Version field.
	 * 
	 * @return int
	 */
	@Version
	@Column(name = "VERSION")
	public int getVersion() {
		return mVersion;
	}

	/**
	 * Set the Version field.
	 */
	@SuppressWarnings("unused")
	private void setVersion(int pNewVersion) {
		mVersion = pNewVersion;
	}
	
	@Transient
	public TreebaseIDString getTreebaseIDString () {
		return new TreebaseIDString(this);
	}
	
	@Transient
	public NamespacedGUID getNamespacedGUID () {
		return getTreebaseIDString().getNamespacedGUID();
	}
	
	@Transient
	public PhyloWSPath getPhyloWSPath() {
		return new PhyloWSPath(this);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.cipres.treebase.domain.Annotatable#getAnnotations()
	 */
	@Transient
	public List<Annotation> getAnnotations() {
		// This is called by child classes using super.getAnnotations()
		// to get the common annotations out of the way
		List<Annotation> annotations = new ArrayList<Annotation>();
		URI uri = URI.create(TreebaseUtil.getPurlDomain()+getPhyloWSPath());
		annotations.add(new Annotation(Constants.OWLURI,"owl:sameAs",uri));
		return annotations;
	}

	/*
	 * (non-Javadoc)
	 * @see org.cipres.treebase.domain.NexmlWritable#getLabel()
	 */
	@Transient
	public String getLabel() {
		// This is overridden by child classes that want a name/title/label
		// to be serialized in nexml or rdf
		return null;
	}

}
