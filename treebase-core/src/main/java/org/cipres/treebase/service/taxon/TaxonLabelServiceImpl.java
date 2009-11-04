
package org.cipres.treebase.service.taxon;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.TreebaseIDString.MalformedTreebaseIDString;
import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonHome;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeHome;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * @author madhu
 * 
 * Significant modifications pertaining to UBIO services were done on April, 2008
 * 
 */
public class TaxonLabelServiceImpl extends AbstractServiceImpl implements TaxonLabelService {
	private static final Logger LOGGER = Logger.getLogger(TaxonLabelServiceImpl.class);

	private static final String NCBITAXONOMYURLPART = "http://www.ncbi.nlm.nih.gov/Taxonomy/Browser";
	private static final String TAXONFINDERSERVICEURL = "http://www.ubio.org/webservices/service_internal.php?function=taxonFinder&includeLinks=1&freeText=";
	private static final String NAMEBANKOBJECTURLPART = "http://www.ubio.org/webservices/service_internal.php?function=namebank_object&version=2.0&namebankID=";
	private static final String UBIOKEYCODE = "1462ff89b9f272ba910f3920a68368404dd319b4";
	private static final String NMBKID = "namebankID";
	private static final String NAMESTR = "nameString";
	private static final int TIMEOUT = 10000;
	private static final String VALUETAG = "value";
	private static final String DETAILSSERVICEURLPART = "http://www.ubio.org/browser/details.php?namebankID=";

	private TaxonLabelHome mTaxonLabelHome;
	private PhyloTreeHome mPhyloTreeHome;
	private TaxonHome mTaxonHome;

	/**
	 * constructor.
	 */
	public TaxonLabelServiceImpl() {
		super();
	}

	/**
	 * Return the PhyloTreeHome field.
	 * 
	 * @return PhyloTreeHome mPhyloTreeHome
	 */
	private PhyloTreeHome getPhyloTreeHome() {
		return mPhyloTreeHome;
	}

	/**
	 * Set the PhyloTreeHome field.
	 */
	public void setPhyloTreeHome(PhyloTreeHome pNewPhyloTreeHome) {
		mPhyloTreeHome = pNewPhyloTreeHome;
	}

	@Override
	protected DomainHome getDomainHome() {
		return getTaxonLabelHome();
	}

	private TaxonLabelHome getTaxonLabelHome() {
		return mTaxonLabelHome;
	}

	public void setTaxonLabelHome(TaxonLabelHome pTaxonLabelHome) {
		mTaxonLabelHome = pTaxonLabelHome;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelService#findByID(java.lang.Long)
	 */
	public TaxonLabel findByID(Long pTaxonLabelID) {
		if (pTaxonLabelID == null) {
			return null;
		}
		return getTaxonLabelHome().findPersistedObjectByID(TaxonLabel.class, pTaxonLabelID);
	}

	/**
	 * TaxonLabel changed. Regenerate Newick strings for all affected trees.
	 * 
	 * @param pTaxonLabel the instance to be updated.
	 * @return the updated instance.
	 */
	public TaxonLabel updateAndRegenerateNewick(TaxonLabel pTaxonLabel) {
		TaxonLabel newLabel = super.update(pTaxonLabel);

		if (newLabel != null) {
			List<TaxonLabel> labels = new ArrayList<TaxonLabel>();
			labels.add(newLabel);

			// update Newick string for all affected trees.
			regenerateNewick(labels);
		}

		return newLabel;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelService#updateChanged(java.util.Collection)
	 */
	public List<TaxonLabel> updateChanged(Collection<TaxonLabel> pChangedLabels) {
		List<TaxonLabel> returnVal = new ArrayList<TaxonLabel>();

		if (pChangedLabels != null && !pChangedLabels.isEmpty()) {
			for (TaxonLabel taxonLabel : pChangedLabels) {
				taxonLabel.setTaxonVariant(null);
				returnVal.add(super.update(taxonLabel));
			}

			regenerateNewick(returnVal);
		}

		return returnVal;
	}

	/**
	 * 
	 * @param pLabels
	 */
	private void regenerateNewick(List<TaxonLabel> pLabels) {
		Collection<PhyloTree> trees = (Collection<PhyloTree>) getPhyloTreeHome()
			.findByAnyTaxonLabel(pLabels);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Number of Trees with updated Newick String = " + trees.size()); //$NON-NLS-1$
		}
		for (PhyloTree phyloTree : trees) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("EACH TREE_LABEL= " + phyloTree.getLabel() + TreebaseUtil.LINESEP);
			}
			phyloTree.updateNewickString();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelService#findBySubstring(java.lang.String)
	 */
	public Collection<TaxonLabel> findBySubstring(String pTerm) {
		return getTaxonLabelHome().findBySubstring(pTerm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelService#findBySubstring(java.lang.String)
	 */
	public Collection<TaxonLabel> findByExactString(String pTerm) {
		return getTaxonLabelHome().findByExactString(pTerm);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelService#findTaxonVariants(org.cipres.treebase.domain.taxon.TaxonLabel)
	 */
	public Collection<TaxonVariant> findTaxonVariants(TaxonLabel pTaxonLabel) {
		String alabel = pTaxonLabel.getTaxonLabel();
		Collection<TaxonVariant> taxonVariant = null;		
		String[] parts = null;
		
		// Check if taxon label has three or more words and third word contains a digit.
		boolean isNonBinomial = false;		
		if (alabel.indexOf(TreebaseUtil.ANEMPTYSPACE) > 0) {
			parts = alabel.split(TreebaseUtil.ANEMPTYSPACE);
			if (parts.length > 2) {
				char[] seq = parts[2].toCharArray();
				for (char a : seq) {
					if (a > 47 && a < 58) {
						isNonBinomial = true;
						break;
					}
				}
			}
		}	
		
		// Check if first two words of the taxon label are present in the database.
		if (isNonBinomial) {
			String testString = parts[0] + TreebaseUtil.ANEMPTYSPACE + parts[1];
			taxonVariant = getTaxonHome().findVariantsByFullName(testString);
		}
		else {
			taxonVariant = getTaxonHome().findVariantsByFullName(alabel);
		}
		return taxonVariant;		
	}
	
	/**
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelService#findTaxonVariant(org.cipres.treebase.domain.taxon.TaxonLabel)
	 */
	public TaxonVariant findTaxonVariant(TaxonLabel pTaxonLabel) {
		String alabel = pTaxonLabel.getTaxonLabel();

		TaxonVariant taxonVariant = getTaxonHome().findTaxonVariantByFullName(alabel);
		if (taxonVariant != null) {
			return taxonVariant;
		}

		String[] parts = null;
		boolean isNonBinomial = false;

		// Check if taxon label has three or more words and third word contains a digit.
		if (alabel.indexOf(TreebaseUtil.ANEMPTYSPACE) > 0) {
			parts = alabel.split(TreebaseUtil.ANEMPTYSPACE);
			if (parts.length > 2) {
				char[] seq = parts[2].toCharArray();
				for (char a : seq) {
					if (a > 47 && a < 58) {
						isNonBinomial = true;
						break;
					}
				}
			}
		} 
		else {
			return null;
		}

		// Check if first two words of the taxon label are present in the database.
		if (isNonBinomial) {
			String testString = parts[0] + TreebaseUtil.ANEMPTYSPACE + parts[1];
			taxonVariant = getTaxonHome().findTaxonVariantByFullName(testString);
		}
		return taxonVariant;
	}
	
	/**
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelService#findNcbiTaxIdByUBIOTaxId(java.lang.Long)
	 */
	public Integer findNcbiTaxIdByUBIOTaxId(Long nameBankId) {
		Integer ncbiId = null;
		String detailsServiceURL = DETAILSSERVICEURLPART + nameBankId;
		String responseString = getStringFromURL(detailsServiceURL, null, null);
		String urlPattern = NCBITAXONOMYURLPART + "/wwwtax.cgi?mode=Info&id=";
		int indexOfNcbi = responseString.indexOf(urlPattern);
		if ( indexOfNcbi > 0 ) {
			String firstPart = responseString.substring(indexOfNcbi + urlPattern.length());
			LOGGER.warn("stripped first part "+firstPart);
			String ncbiString = firstPart.substring(0,firstPart.indexOf('&'));
			LOGGER.warn("parsed NCBI ID "+ncbiString);
			ncbiId = Integer.parseInt(ncbiString);
		}
		else {
			LOGGER.warn("No NCBI ID found");
		}
		return ncbiId;		
	}
	
	/**
	 * Normalizes a taxon label string in accordance with TreeBASE requirements:
	 * if the label is longer than binomial, and the third term contains digits,
	 * the third part is stripped.
	 * 
	 * @param label
	 * @return normalized label string
	 */
	private String normalizeLabelString(String label) {
		String withoutsuffix = null;
		
		//remove any host names, crosses, etc
		label = label.replaceFirst("^([\\w\\s\\-]+) ex\\.? .+$", "$1");
		label = label.replaceFirst("^([\\w\\s\\-]+) fm\\.? .+$", "$1");
		label = label.replaceFirst("^([\\w\\s\\-]+) [xX]\\.? .+$", "$1");
		
		/*		
		remove comments or notations that simply indicate ambiguity
		but have the effect of disrupting taxonFinder's function
		*/
		label = label.replaceFirst(" cf\\.? ", " ");
		label = label.replaceFirst(" var\\.? ", " ");
		label = label.replaceFirst(" nr\\.? ", " ");
		label = label.replaceFirst(" aff\\.? ", " ");

		/*
		separate any cases of a lower case followed by an upper case
		because taxonFinder will fail if there is no separation between 
		species and suffix code (e.g. "Homo sapiensLJ34")
		*/
		label = label.replaceAll("([a-z])([A-Z])", "$1 $2");
		
		/*
		separate any cases of a letter followed by a number, again because
		taxonFInder will fail if there is no separation between species and 
		suffix code (e.g. "Homo sapiens3453")
		*/
		label = label.replaceFirst("([a-z])([0-9])", "$1 $2");
		
		/*
		remove a period followed by a number or letter. This is because people
		frequently append their suffix codes with periods, e.g. "Homo sapiens.2342"
		*/
		label = label.replaceFirst("([a-z])\\.([a-z0-9])", "$1 $2");

		// first try to capture a trinomial with a trailing suffix
		if ( label.matches("^[A-Z][a-z\\-]+ [a-z\\-]+ [a-z\\-]+ .+$") ) {
			String[] parts = label.split(" ");
			withoutsuffix = parts[0] + " " + parts[1] + " " + parts[2];
		}
		// but maybe you have a good trinomial without a trailing suffix
		else if ( label.matches("^[A-Z][a-z\\-]+ [a-z\\-]+ [a-z\\-]+$") ) {
			String[] parts = label.split(" ");
			withoutsuffix = parts[0] + " " + parts[1] + " " + parts[2];			
		}
		// if that does not work, capture a binomial with a trailing suffix
		else if ( label.matches("^[A-Z][a-z\\-]+ [a-z\\-]+ .+$") ) {
			String[] parts = label.split(" ");
			withoutsuffix = parts[0] + " " + parts[1];
		}
		else {
			withoutsuffix = label;
		}
		
		return withoutsuffix;
	}
	
	/**
	 * 
	 * 
	 * @param nameBankObjectValue
	 * @param ubioDataList
	 * @param firstVariant
	 * @return
	 */
	private Map<String,String> parseNameBankObjectResponse(String nameBankObjectValue,List<TaxonVariant> ubioDataList,TaxonVariant firstVariant) {
		Map<String,String> returnVal = new HashMap<String,String>();
		javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		javax.xml.parsers.DocumentBuilder db = null;
		org.w3c.dom.Document xmlDoc = null;	
		try {
			db = dbf.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(nameBankObjectValue.getBytes("UTF-8"));
			xmlDoc = db.parse(is);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
		org.w3c.dom.Element id = (org.w3c.dom.Element)xmlDoc.getDocumentElement().getElementsByTagName(NMBKID).item(0);
		org.w3c.dom.Element nameS = (org.w3c.dom.Element)xmlDoc.getDocumentElement().getElementsByTagName(NAMESTR).item(0);
		if (nameS != null) {
			returnVal.put("nameStringNBO", nameS.getTextContent());
		}
		org.w3c.dom.Element canonicalF = (org.w3c.dom.Element)xmlDoc.getDocumentElement().getElementsByTagName("canonicalForm").item(0);
		if (canonicalF != null) {
			returnVal.put("canonicalFormNBO", canonicalF.getTextContent());
		}
		// For this particular element, canonical form is treated as nameString and
		// nameString as fullNameString.
		firstVariant = new TaxonVariant(
			TreebaseUtil.parseLong(id.getTextContent(), null),
			returnVal.get("canonicalFormNBO"),
			returnVal.get("nameStringNBO"),
			null);
		ubioDataList.add(firstVariant);		
		org.w3c.dom.Element lexicalG = (org.w3c.dom.Element)xmlDoc.getDocumentElement().getElementsByTagName("lexicalGroups").item(0);
		if (lexicalG != null) {
			org.w3c.dom.NodeList valueElements = ((org.w3c.dom.Element)lexicalG.getElementsByTagName(VALUETAG).item(0)).getElementsByTagName(VALUETAG);
			getValuesFromElementList(valueElements, ubioDataList);
		}
		org.w3c.dom.Element basionymG = (org.w3c.dom.Element)xmlDoc.getDocumentElement().getElementsByTagName("basionymGroup").item(0);
		if (basionymG != null) {
			org.w3c.dom.NodeList basioElements = basionymG.getElementsByTagName(VALUETAG);
			getValuesFromElementList(basioElements, ubioDataList);
		}				
		return returnVal;
	}

	/**
	 * @param response
	 * @return
	 */
	private Map<String,String> parseTaxonFinderResponse(String response) {		
		Map<String,String> returnVal = new HashMap<String,String>();
		javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
		javax.xml.parsers.DocumentBuilder db = null;
		org.w3c.dom.Document xmlDoc = null;
		org.w3c.dom.Element elemId = null;
		org.w3c.dom.Element elemStr = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (javax.xml.parsers.ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}		
		try {
			InputStream is = new ByteArrayInputStream(response.getBytes("UTF-8"));
			xmlDoc = db.parse(is);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
		org.w3c.dom.Element allNames = (org.w3c.dom.Element)xmlDoc.getDocumentElement().getElementsByTagName("allNames").item(0);
		if ( allNames != null ) {
			org.w3c.dom.Element entity   = (org.w3c.dom.Element)allNames.getElementsByTagName("entity").item(0);
			if ( entity != null ) {
				elemId   = (org.w3c.dom.Element)entity.getElementsByTagName(NMBKID).item(0);
				elemStr  = (org.w3c.dom.Element)entity.getElementsByTagName(NAMESTR).item(0);
			}
		}
		if (elemId != null) {
			returnVal.put("mNameBankID", elemId.getTextContent());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("createFromUBIOService(TaxonLabel) - NameBank ID: " + returnVal.get("mNameBankID"));
			}
		}	
		if (elemStr != null) {
			returnVal.put("nameString", elemStr.getTextContent());
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("createFromUBIOService(TaxonLabel) - Name String: " + returnVal.get("nameString"));
			}
		}		
		return returnVal;
	}
	
	/**
	 * @param pRequest HttpServletRequest object.
	 * @param label String 'Taxon Label' that is to be checked against the ubio REST service.
	 * 
	 * @return TaxonVariant created preferred taxon variant.
	 */
	@SuppressWarnings("unchecked")
	public TaxonVariant createFromUBIOService(TaxonLabel pTaxonLabel) {
		String ncbiString = null, ncbiID = null, testString = null, 
			ncbiPreferredName = null, preferredVariant = null, mNameBankID = null, 
			nameString = null, nameStringNBO = null, canonicalFormNBO = null;
		boolean ncbiStringFromTF = false;
		booleanplaceholder bph = new booleanplaceholder();
		longplaceholder lph = new longplaceholder();
		TaxonVariant firstVariant = null;

		// XXX 1. Turn label into tri- or binomial, no var./ex./etc.
		testString = normalizeLabelString(pTaxonLabel.getTaxonLabel());

		// XXX 2. Run TaxonFinder query on URL-escaped, normalized label
		String taxonFinderFullURL = TAXONFINDERSERVICEURL + testString.replaceAll(TreebaseUtil.ANEMPTYSPACE, "%20");
		String result = getStringFromURL(taxonFinderFullURL, null, null);
		
		// XXX 3. Process TaxonFinder result
		if (result == null || result.indexOf(NMBKID) < 0 || result.indexOf(testString) < 0 || result.indexOf(NAMESTR) < 0) {
			LOGGER.warn("Problem: uBio result is garbled or doesn't contain our testString");
			return null;
		} 
		else {		
			if ( LOGGER.isDebugEnabled() ) {
				LOGGER.debug("Going to parse TaxonFinder response "+result);
			}
			// XXX 4. Get namebankID and canonical string from TaxonFinder result
			Map<String,String> uBioResponseMap = parseTaxonFinderResponse(result);
			if ( uBioResponseMap != null ) {
				mNameBankID = uBioResponseMap.get("mNameBankID");
				nameString = uBioResponseMap.get("nameString");
			}
			if (mNameBankID == null || nameString == null) {
				LOGGER.debug("createFromUBIOService(TaxonLabel) - Name Bank ID  or Name String is not present."); //$NON-NLS-1$
				return null;
			}			
									
			// XXX 5. Check if TaxonFinder returns an NCBI reference
			int ncbiIndex = result.indexOf(NCBITAXONOMYURLPART);
			if ( ncbiIndex >= 0 ) {
				ncbiString = result.substring(ncbiIndex).trim();
				ncbiStringFromTF = true;
			} 
			// XXX 6. If no NCBI reference in TaxonFinder, screenscrape uBio record details
			else {
				ncbiString = getStringFromURL(DETAILSSERVICEURLPART + mNameBankID, NCBITAXONOMYURLPART, bph);
			}
		}
		if (ncbiStringFromTF || bph.isNcbiRecordFound()) {
			ncbiID = getNcbiID(ncbiString);
		} 
		if (ncbiID != null) {
			ncbiPreferredName = getNCBIPreferredName(ncbiID);
		}
		String nameBankObjectURLFull = NAMEBANKOBJECTURLPART.concat(mNameBankID).concat("&keyCode=").concat(UBIOKEYCODE);
		String nameBankObjectValue = getStringFromURL(nameBankObjectURLFull, null, null);
		if (nameBankObjectValue == null) {
			return null;
		}

		List<TaxonVariant> ubioDataList = new ArrayList<TaxonVariant>();
		Map<String,String> detailedUBioResponse = parseNameBankObjectResponse(nameBankObjectValue, ubioDataList, firstVariant);
		nameStringNBO = detailedUBioResponse.get("nameStringNBO");
		canonicalFormNBO = detailedUBioResponse.get("canonicalFormNBO");
		firstVariant = ubioDataList.get(0);

		if (ncbiPreferredName == null) {
			if (canonicalFormNBO == null) {
				// Preferred Variant is Name String from TaxonFinder Service
				preferredVariant = nameString;
			} 
			else {
				//  Preferred Variant is CanonicalForm
				preferredVariant = canonicalFormNBO;
			}
		} 
		else {
			// Preferred Variant is NCBI preferred name
			preferredVariant = ncbiPreferredName;
		}

		if (isNcbiPreferredNameInList(preferredVariant, ubioDataList, lph)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("createFromUBIOService(TaxonLabel) - Match found: " + lph.getNameBankIDForTaxa()); //$NON-NLS-1$
			}
		} 
		else {
			LOGGER.debug("createFromUBIOService(TaxonLabel) - No Match found: "); //$NON-NLS-1$
		}

		Collection<Taxon> taxa = getTaxonHome().findTaxaByName(preferredVariant);
		if (taxa.size() == 0) {
			// now save to database: Taxon preferredVariant , taxonVariantList ubioDataList, update TaxonLabel
			Taxon taxon = new Taxon();
			taxon.setName(preferredVariant);
			if (ncbiID != null) {
				taxon.setNcbiTaxId(Integer.parseInt(ncbiID));
			}
			if (lph.getNameBankIDForTaxa() > 0L) { // it means
				taxon.setUBioNamebankId(lph.getNameBankIDForTaxa());
			}

			// save the new taxon and associated taxon variants.
			for (TaxonVariant taxonVariant : ubioDataList) {
				taxonVariant.setTaxon(taxon);
			}
			getTaxonHome().store(taxon);
			getTaxonHome().storeAll(ubioDataList);
		} 
		else {
			// Important: firstVariant needs to be merged too.
			Taxon taxon = taxa.iterator().next();
			firstVariant = combineVariants(taxon, ubioDataList, firstVariant);
			update(taxon);
			if ( taxa.size() > 1 ) {
				LOGGER.warn("Homonyms found!");
			}
		}

		return firstVariant;
	}

	/**
	 * Combine the list of variants into the Taxon variants list.
	 * 
	 * @param pTaxon
	 * @param pNewVariants the newly found variants to be combined
	 * @param pCurrentVariant the variant the taxon label is referring to. Need to merge.
	 * @return the merged current variant.
	 */
	private TaxonVariant combineVariants(
		Taxon pTaxon,
		List<TaxonVariant> pNewVariants,
		TaxonVariant pCurrentVariant) {

		if (pNewVariants == null || pNewVariants.isEmpty()) {
			return pCurrentVariant;
		}

		// default the return value to be current variant. Need to merge
		TaxonVariant returnVal = pCurrentVariant;

		Collection<TaxonVariant> currentVariants = getTaxonHome().findVariantsByTaxon(pTaxon);
		Collection<TaxonVariant> newVariants = new ArrayList<TaxonVariant>();

		// match the namebankid AND the fullname.
		for (TaxonVariant taxonVariant : pNewVariants) {

			boolean matched = false;
			Iterator<TaxonVariant> variantIter = currentVariants.iterator();
			while (variantIter.hasNext() && !matched) {
				TaxonVariant currentVariant = variantIter.next();

				// TODO: move the code to TaxonVariant.equals() method. define hashcode()
				if (TreebaseUtil.isEqual(taxonVariant.getFullName(), currentVariant.getFullName())
					&& TreebaseUtil.isEqual(taxonVariant.getNamebankID(), currentVariant
						.getNamebankID())) {

					// already in the list
					matched = true;

					// Important: use the current existing variant. discard the
					// new variant pCurrentVariant.
					if (taxonVariant == pCurrentVariant) {
						returnVal = currentVariant;
					}
				}
			}

			if (!matched) {
				newVariants.add(taxonVariant);
				taxonVariant.setTaxon(pTaxon);
			}
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER
				.debug("combineVariants(): current variants size=" + currentVariants.size() + " new variants size=" + newVariants.size()); //$NON-NLS-1$
		}

		getTaxonHome().storeAll(newVariants);

		return returnVal;
	}

	/**
	 * This methods checks if NCBI preferred name is in the bioDataList list or not. First, it
	 * checks against UbioData's getFullNameString method. If no match is found, then check is done
	 * against UbioData's getLexicalQualifier method. If either match is found then returned value
	 * is true else false.
	 * 
	 * @param preferredName NCBI preferred name
	 * @param taxonVariantList TaxonVariant List
	 * @return true if match is found else return false.
	 */
	public boolean isNcbiPreferredNameInList(
		String preferredName,
		List<TaxonVariant> txnVariantList,
		longplaceholder lpholder) {

		for (TaxonVariant bioData : txnVariantList) {
			if (preferredName.equals(bioData.getFullName())) {
				lpholder.setNameBankIDForTaxa(bioData.getNamebankID());
				break;
			}
		}

		if (lpholder.getNameBankIDForTaxa() > 0L) {
			return true;
		}
		for (TaxonVariant bioData : txnVariantList) {
			String lexicalQualifier = bioData.getLexicalQualifier();
			if (lexicalQualifier != null && preferredName.equals(lexicalQualifier)) {
				lpholder.setNameBankIDForTaxa(bioData.getNamebankID());
				break;
			}
		}
		if (lpholder.getNameBankIDForTaxa() > 0L) {
			return true;
		}

		return false;
	}

	/**
	 * This method extracts values from jdom element List and adds them to the bioDataList.
	 * 
	 * @param elementList
	 * @param txnVariantList
	 */
	public void getValuesFromElementList(org.w3c.dom.NodeList elementList,List<TaxonVariant> txnVariantList) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Number of Elements: " + elementList.getLength() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		for ( int i = 0; i < elementList.getLength(); i++ ) {
			org.w3c.dom.Element element = (org.w3c.dom.Element) elementList.item(i);
			Long nameBankID = TreebaseUtil.parseLong(element.getElementsByTagName(NMBKID).item(0).getTextContent(), null);
			String nameString = element.getElementsByTagName(NAMESTR).item(0).getTextContent();
			String fullNameString = element.getElementsByTagName("fullNameString").item(0).getTextContent();
			String lexicalQualifier = null;

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("B nameBankId: " + nameBankID + "\nB nameString: " + nameString + "\nB fullNameString: " + fullNameString); //$NON-NLS-1$
			}

			if (element.getElementsByTagName("lexicalQualifier").item(0) != null) {
				lexicalQualifier = element.getElementsByTagName("lexicalQualifier").item(0).getTextContent();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("B lexicalQualifier: " + lexicalQualifier); //$NON-NLS-1$
				}
			}
			txnVariantList.add(new TaxonVariant(
				nameBankID,
				nameString,
				fullNameString,
				lexicalQualifier));			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("getValuesFromElementList() - " + TreebaseUtil.LINESEP); //$NON-NLS-1$
			}
		}		
	}
	
	/**
	 * This method extracts the NCBI taxon ID from a String.
	 * 
	 * @param testString
	 * @return String NCBIID if is is present else return null.
	 */
	public String getNcbiID(String testString) {
		int startIndex = testString.indexOf("id=");
		int endIndex = testString.length() - 1;
		System.out.println("START INDEX: " + startIndex + " END INDEX: " + endIndex);
		endIndex = endIndex - startIndex > 20 ? startIndex + 20 : endIndex;
		String ncbiIDLine = testString.substring(startIndex + 3, endIndex);
		System.out.println("NCBI ID LINE: " + ncbiIDLine);
		char[] charSeqNcbiID = ncbiIDLine.toCharArray();
		int n = -5;
		// To check where the NCBIID ends
		for (int i = 0; i < charSeqNcbiID.length; i++) {
			char a = charSeqNcbiID[i];

			if (a < 48 || a > 57) {
				n = i;
				break;
			}
		}
		return ncbiIDLine.substring(0, n);
	}

	/**
	 * @param urlString
	 * @param searchElement
	 * @return the line containing the search element
	 */
	public String getStringFromURL(
		String urlString,
		String searchElement,
		booleanplaceholder ncbiRecord) {		
		if ( LOGGER.isDebugEnabled() ) {
			LOGGER.debug("SEARCH ELEMENT: " + searchElement);
		}
		try {
			URL url = new URL(urlString);
			URLConnection urlconn = url.openConnection();
			urlconn.setConnectTimeout(TIMEOUT);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
			StringBuilder results = new StringBuilder();
			String str;
			while ( (str = in.readLine()) != null ) {				
				// Some times NCBI stream has 'TITLE' instead of 'title'
				String testStr = str.replaceAll("TITLE", "title");
				if (searchElement != null && testStr.indexOf(searchElement) >= 0) {
					String[] strParts = testStr.split(searchElement);
					if ( LOGGER.isDebugEnabled() ) {
						LOGGER.debug("Str Parts: " + strParts[1]);
					}
					if (searchElement.equals(NCBITAXONOMYURLPART)) {
						if (ncbiRecord.isNcbiRecordFound() == false) {
							ncbiRecord.setNcbiRecordFound(true);
						}
					}
					return strParts[1];
				} 
				else {
					results.append(str).append(TreebaseUtil.LINESEP);
				}
			}
			return results.toString();
		} catch (MalformedURLException mfe) {
			mfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	/**
	 * It fetches NCBI preferred name from NCBI REST services using ncbiID
	 * 
	 * @param ncbiID
	 * @return String NCBI preferred name.
	 */
	public String getNCBIPreferredName(String ncbiID) {
		String taxonomyString = NCBITAXONOMYURLPART + "/wwwtax.cgi?&id=" + ncbiID + "&unlock";

		String searchPiece = "<title>Taxonomy browser";
		String result = getStringFromURL(taxonomyString, searchPiece, null);
		if (result == null) {
			// Problem with NCBI services
			return null;
		}
		int start = result.indexOf("(");
		int end = result.indexOf(")");
		if ( LOGGER.isDebugEnabled() ) {
			LOGGER.debug("NCBI Preferred Name: " + result.substring(start + 1, end));
		}
		return result.substring(start + 1, end);
	}

	/**
	 * @return the taxonHome
	 */
	public TaxonHome getTaxonHome() {
		return mTaxonHome;
	}

	/**
	 * @param pTaxonHome the taxonHome to set
	 */
	public void setTaxonHome(TaxonHome pTaxonHome) {
		mTaxonHome = pTaxonHome;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelService#setStudyForAllLabels(org.cipres.treebase.domain.tree.PhyloTree, org.cipres.treebase.domain.study.Study)
	 */
	public void updateStudyForAllLabels(PhyloTree tree, Study study) {
		getTaxonLabelHome().updateStudyForAllLabels(tree, study);
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelService#setStudyForAllLabels(org.cipres.treebase.domain.matrix.Matrix, org.cipres.treebase.domain.study.Study)
	 */
	public void updateStudyForAllLabels(Matrix matrix, Study study) {
		getTaxonLabelHome().updateStudyForAllLabels(matrix, study);
	}

	public Collection<TaxonVariant> findTaxonVariantWithSubstring(String s) {
		return getTaxonLabelHome().findTaxonVariantWithSubstring(s, false);
	}
	
	public Collection<TaxonVariant> findTaxonVariantWithSubstring(String s, Boolean caseSensitive) {
		return getTaxonLabelHome().findTaxonVariantWithSubstring(s, caseSensitive);
	}

	public Collection<TaxonLabel> findByStudy(Study theStudy) {
		return getTaxonLabelHome().findByStudy(theStudy);
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelService#expandTaxonVariant(org.cipres.treebase.domain.taxon.TaxonVariant)
	 */
	public Set<TaxonVariant> expandTaxonVariant(TaxonVariant pTV) {
		return getTaxonLabelHome().expandTaxonVariant(pTV);
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelService#expandTaxonVariantSet(java.util.Set)
	 */
	public Set<TaxonVariant> expandTaxonVariantSet(Set<TaxonVariant> pTVSet) {
		return getTaxonLabelHome().expandTaxonVariantSet(pTVSet);
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelService#findByTaxonVariant(org.cipres.treebase.domain.taxon.TaxonVariant)
	 */
	public Set<TaxonLabel> findByTaxonVariant(TaxonVariant pTV) {
		return getTaxonLabelHome().findByTaxonVariant(pTV);
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelService#findByTaxonVariantSet(java.util.Set)
	 */
	public Set<TaxonLabel> findByTaxonVariantSet(Set<TaxonVariant> pTVSet) {
		Set<TaxonLabel> result = new HashSet<TaxonLabel> ();
		for (TaxonVariant tv : pTVSet) {
			result.addAll(findByTaxonVariant(tv));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelService#findStudies(java.util.Collection)
	 */
	public Collection<Study> findStudiesWithTaxonLabels(Collection<TaxonLabel> pTaxonLabels) {
		return getTaxonLabelHome().findStudiesWithTaxonLabels(pTaxonLabels);
	}	
	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelService#findStudies(org.cipres.treebase.domain.taxon.TaxonVariant)
	 */
	public Set<Study> findStudies(TaxonVariant pTaxonVariant) {
		return getTaxonLabelHome().findStudies(pTaxonVariant);
	}
	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelService#findStudies(java.util.Collection)
	 */
	public Set<Study> findStudiesWithTaxonVariants(Collection<TaxonVariant> taxonVariants) {
		Set<Study> results = new HashSet<Study> ();
		for (TaxonVariant tv : taxonVariants) {
			results.addAll(findStudies(tv));
		}
		return results;
	}
	
	public Collection<Matrix> findMatricesWithTaxonLabels(Collection<TaxonLabel> taxonLabels) {
		return getTaxonLabelHome().findMatricesWithTaxonLabels(taxonLabels);
	}

	public Set<Matrix> findMatrices(TaxonVariant taxonVariant) {
		return getTaxonLabelHome().findMatrices(taxonVariant);
	}

	public Set<Matrix> findMatricesWithTaxonVariants(Collection<TaxonVariant> taxonVariants) {
		Set<Matrix> results = new HashSet<Matrix> ();
		for (TaxonVariant tv : taxonVariants) {
			results.addAll(findMatrices(tv));
		}
		return results;
	}

	public Collection<PhyloTree> findTreesWithTaxonLabels(Collection<TaxonLabel> taxonLabels) {
		return getTaxonLabelHome().findTreesWithTaxonLabels(taxonLabels);
	}

	public Set<PhyloTree> findTrees(TaxonVariant taxonVariant) {
		return getTaxonLabelHome().findTrees(taxonVariant);
	}

	public Set<PhyloTree> findTreesWithTaxonVariants(Collection<TaxonVariant> taxonVariants) {
		Set<PhyloTree> results = new HashSet<PhyloTree> ();
		for (TaxonVariant tv : taxonVariants) {
			results.addAll(findTrees(tv));
		}
		return results;
	}
	
	public Set<Submission> findSubmissions(TaxonLabel taxonLabel) {
		return getTaxonLabelHome().findSubmissions(taxonLabel);
	}
	
	public Set<Submission> findSubmissions(Collection<TaxonLabel> taxonLabels) {
		Set<Submission> results = new HashSet<Submission> ();
		for (TaxonLabel tl : taxonLabels) {
			results.addAll(findSubmissions(tl));
		}
		return results;
	}

	public Collection<TaxonVariant> findTaxonVariantByFullName(String s) {
		return getTaxonLabelHome().findTaxonVariantByFullName(s);
	}
	
	public TaxonVariant findTaxonVariantByID(Long id) {
		TaxonVariant variant = getTaxonLabelHome().findPersistedObjectByID(TaxonVariant.class, id);
		return variant;
	}

	public Set<TaxonVariant> findTaxonVariantByName(String s) {
		return getTaxonLabelHome().findTaxonVariantByName(s);
	}

	/**
	 * @param t
	 * @return
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#findMatrices(org.cipres.treebase.domain.taxon.Taxon)
	 * @author mjd 20081204
	 */
	public Collection<Matrix> findMatrices(Taxon t) {
		return getTaxonLabelHome().findMatrices(t);
	}

	/**
	 * @param t
	 * @return
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#findStudies(org.cipres.treebase.domain.taxon.Taxon)
	 * @author mjd 20081204
	 */
	public Collection<Study> findStudies(Taxon t) {
		return getTaxonLabelHome().findStudies(t);
	}

	/**
	 * @param t
	 * @return
	 * @see org.cipres.treebase.domain.taxon.TaxonLabelHome#findTrees(org.cipres.treebase.domain.taxon.Taxon)
	 * @author mjd 20081204
	 */
	public Collection<PhyloTree> findTrees(Taxon t) {
		return getTaxonLabelHome().findTrees(t);
	}

	public Taxon findTaxonByIDString(String idString) throws MalformedTreebaseIDString {
		TreebaseIDString id = new TreebaseIDString(idString, Taxon.class);
		return  getDomainHome().findPersistedObjectByID(id.getTBClass(), id.getId());
	}
	
	public String getIDPrefix() {
		return "Tl";
	}

	public Collection<TaxonLabel> findBySubstring(String term, boolean caseSensitive) {
		return getTaxonLabelHome().findBySubstring(term,caseSensitive);
	}

	public Collection<Taxon> findTaxaByName(String term) {
		return getTaxonHome().findTaxaByName(term);
	}

	public Collection<Taxon> findTaxaBySubstring(String term, boolean caseSensitive) {
		return getTaxonHome().findTaxaBySubstring(term,caseSensitive);
	}

	@Override
	public Class defaultResultClass() {
		return TaxonLabel.class;
	}
}
