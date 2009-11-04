
package org.cipres.treebase.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonHome;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.cipres.treebase.exception.UnimplementedMethodError;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

class LoadTaxonData extends AbstractStandalone implements LoadTaxonDataInterface {

	TaxonHome taxonHome;
	TaxonLabelHome taxonLabelHome;

	static boolean buildTaxa = false, checkTaxa = false;
	static boolean buildTaxonVariants = false, checkTaxonVariants = false;
	static boolean linkTaxonLabels = false, checkTaxonLabels = false;

	static boolean checkOnly;
	static boolean mergeDuplicates;

	static UnixOptions opts;

	public static void main(String [] args) throws FileNotFoundException {
		setupContext();

		SessionFactory sessionFactory = (SessionFactory) ContextManager.getBean("sessionFactory");
		sessionFactory.openSession();

		GetOpts<UnixOptions> go = new GetOpts<UnixOptions>(new UnixOptions ("XMtTvVlL"));
		opts = go.getOpts(args);

		mergeDuplicates = opts.getBoolOpt("M");
		buildTaxa = opts.getBoolOpt("T");
		checkTaxa = opts.getBoolOpt("t");
		buildTaxonVariants = opts.getBoolOpt("V");
		checkTaxonVariants = opts.getBoolOpt("v");
		linkTaxonLabels = opts.getBoolOpt("L");
		checkTaxonLabels = opts.getBoolOpt("l");
		
		String[] tRequiredFields = {"tID", "uBIO", "nameString"};
		String[] tOptionalFields = {"NCBI", "unused"};
		String[] tvRequiredFields = {"tvID", "tID", "uBIO", "nameString", 
		"fullNameString"};
		String[] tvOptionalFields = {"lexQual"};
		String[] tlRequiredFields = {"tlID", "tvID", "legacyID"};
		String[] tlOptionalFields = {"taxonlabel"};

		args = go.getArgs();

		String dir = ".";
		if (args.length >= 1) {
			dir = args[0];
		}

		OptionalHashFieldReader tlFH = 
			new OptionalHashFieldReader(new FileReader(dir + "/TL.tab"), 
					"\t",
					tlRequiredFields, tlOptionalFields);
		OptionalHashFieldReader tvFH = 
			new OptionalHashFieldReader(new FileReader(dir + "/TV.tab"),
					"\t",
					tvRequiredFields, tvOptionalFields);
		OptionalHashFieldReader tFH = 
			new OptionalHashFieldReader(new FileReader(dir + "/T.tab"), 
					"\t",
					tRequiredFields, tOptionalFields);

		LoadTaxonDataInterface me = (LoadTaxonDataInterface) ContextManager.getBean("loadTaxonData");

		// Special case: just set up the legacy IDs for taxa
		if (opts.getBoolOpt("X")) {
			warn("Populating legacy IDs");
			me.populateTaxaLegacyIDs(tFH);
			warn("Done.");
			System.exit(0);
		}

		// Phase 1: build or locate taxon objects
		Map<String,Taxon> taxon = null;
		if (buildTaxa) 
			taxon = me.buildTaxa(tFH);
		if (checkTaxa) 
			taxon = me.checkTaxa(tFH);
		if (taxon == null) taxon = me.knownTaxonCache();
			
		// Phase 2: build or locate TV objects and connect them to the taxon objects from phase 1
		Map<String,TaxonVariant> taxonVariant = null;
		if (buildTaxonVariants) 
			taxonVariant = me.buildTaxonVariants(tvFH, taxon); 
		if (checkTaxonVariants)
			taxonVariant = me.checkTaxonVariants(tvFH, taxon);
		if (taxonVariant == null) taxonVariant = me.knownTaxonVariantCache();
		
		// Phase 3: connect existing taxonlabel objects to the taxonvariant objects from phase 2
		if (linkTaxonLabels) 
			me.linkTaxonLabels(tlFH, taxonVariant);
		if (checkTaxonLabels)
			me.checkTaxonLabels(tlFH, taxonVariant);
	}

	/* 
	 * **************************************************************************
	 * ***** PHASE 1
	 * Create the underlying taxa, or locate them if they already exist
	 * Correct errors where possible
	 */	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.util.JUNK#buildTaxa(org.cipres.treebase.util.OptionalHashFieldReader)
	 */
	public Map<String, Taxon> buildTaxa(OptionalHashFieldReader fh) {

		Session sess = getSessionFactory().getCurrentSession();

		Map<String,Taxon> knownTaxon = knownTaxonCache();

		int uncommittedTaxa = 0;
		final int maxUncomittedTaxa = 100;
		for (Map<String,String> rec : fh) { 

			String oldID = rec.get("tID");
			Integer oldIDInteger = Integer.decode(oldID);
			boolean updateThisTaxon = false;

			Long uBioId;
			{
				String uBioIdString = rec.get("uBIO");
				uBioId = uBioIdString.equals("") ? null : Long.decode(uBioIdString);
			}

			Integer ncbiTaxid;
			{
				String ncbiTaxidString = rec.get("NCBI");
				ncbiTaxid =  ncbiTaxidString == null    ? null : 
					ncbiTaxidString.equals("") ? null : Integer.decode(ncbiTaxidString);
			}
			String taxonName = rec.get("nameString");

			Transaction trans = sess.getTransaction();

			Taxon t;
			t = knownTaxon.get(oldID);
			if (t == null) { 
				warn("Creating new taxon with oldID=" + oldID + " NCBI=" + ncbiTaxid + " uBIO=" + uBioId);
				t = new Taxon();
				t.setUBioNamebankId(uBioId);
				t.setNcbiTaxId(ncbiTaxid);
				t.setTB1LegacyId(oldIDInteger);
				sess.save(t);
				knownTaxon.put(oldID, t);
				updateThisTaxon = true;
			}

			if (t.getName() == null) {
				warn("Setting name to " + taxonName);
				t.setName(taxonName);
			} else if (! t.getName().equals(taxonName)) {
				warn("taxon " + oldID + " found in DB as " + t.getId() +
						" with mismatched name " + taxonName + " / " + t.getName() +
				"; correcting");
				updateThisTaxon = true;
				t.setName(taxonName);
			}


			if (updateThisTaxon) { 
				sess.update(t);
				uncommittedTaxa++;
			}
			if (uncommittedTaxa >= maxUncomittedTaxa) {
				warn("Committing " + uncommittedTaxa + " taxa.");
				trans.commit();
				trans.begin();
				uncommittedTaxa = 0;
			}

			if (fh.recNumber % 10000 == 0) {
				warn("T " + fh.recNumber + " : " + taxonName);
			}
		}

		return knownTaxon;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.util.JUNK#checkTaxa(org.cipres.treebase.util.OptionalHashFieldReader)
	 */
	public Map<String, Taxon> checkTaxa(OptionalHashFieldReader fh) {
		Map<String,Taxon> knownTaxon = knownTaxonCache();
		Map<String,Taxon> taxon = new HashMap<String, Taxon> (423533*4/3);

		for (Map<String,String> rec : fh) {
			String oldID = rec.get("tID");
			Long uBioId;
			{
				String uBioIdString = rec.get("uBIO");
				uBioId = uBioIdString.equals("") ? null : Long.decode(uBioIdString);
			}

			Integer ncbiTaxID;
			{
				String ncbiTaxidString = rec.get("NCBI");
				ncbiTaxID =  ncbiTaxidString == null    ? null : 
					ncbiTaxidString.equals("") ? null : Integer.decode(ncbiTaxidString);
			}
			String taxonName = rec.get("nameString");

			if (taxonName == null && ncbiTaxID == null)
				die("Taxon " + oldID + " has neither name nor NCBI id");

			Taxon t = knownTaxon.get(oldID);
			if (t == null) { 
				warn("File calls for missing taxon with TB1id=" + oldID);
				continue;
			}

			// TODO: reduce duplication in the next three blocks of code
			if (uBioId == null && t.getUBioNamebankId() != null) 
				warn("TB1id=" + oldID + " should have no uBio id, but T" + t.getId() + " has " + t.getUBioNamebankId());
			else if (uBioId != null && t.getUBioNamebankId() == null)
				warn("TB1id=" + oldID + " should have uBio id " + uBioId + " but T" + t.getId() + " has none");
			else if (uBioId != null && t.getUBioNamebankId() != null)
				if (! uBioId.equals(t.getUBioNamebankId()))
					warn("File says TB1id=" + oldID + " T" + t.getId() + " should have ubio id " + uBioId + " but it has " + t.getUBioNamebankId());

			if (ncbiTaxID == null && t.getNcbiTaxId() != null) 
				warn("TB1id=" + oldID + " should have no NCBI id, but T" + t.getId() + " has " + t.getNcbiTaxId());
			else if (ncbiTaxID != null && t.getNcbiTaxId() == null)
				warn("TB1id=" + oldID + " should have NCBI id " + ncbiTaxID + " but T" + t.getId() + " has none");
			else if (ncbiTaxID != null && t.getNcbiTaxId() != null)
				if (! ncbiTaxID.equals(t.getNcbiTaxId()))
					warn("File says TB1id=" + oldID + " T" + t.getId() + " should have NCBI id " + ncbiTaxID + " but it has " + t.getNcbiTaxId());

			if (taxonName == null && t.getName() != null) 
				warn("TB1id=" + oldID + " should have no name, but T" + t.getId() + " has " + t.getName());
			else if (taxonName != null && t.getName() == null)
				warn("TB1id=" + oldID + " should have name " + taxonName + " but T" + t.getId() + " has none");
			else if (taxonName != null && t.getName() != null)
				if (! taxonName.equals(t.getName()))
					warn("File says TB1id=" + oldID + " T" + t.getId() + " should have name " + taxonName + " but it has " + t.getName());

			taxon.put(oldID, t);
			knownTaxon.remove(oldID);

//			if (fh.recNumber % 100 == 0) {
//			warn("T " + fh.recNumber + " : " + taxonName);
//			}
		}

		for (String key : knownTaxon.keySet()) 
			warn("Database contains excess taxon T" + knownTaxon.get(key).getId());

		return taxon;
	}

	/* 
	 * Cache the existing taxon objects here; it will save a lot of database querying later
	 * Keys are name strings and NCBI genbank ID numbers, joined with a comma
	 */
	/* (non-Javadoc)
	 * @see org.cipres.treebase.util.JUNK#knownTaxonCache()
	 */
	public Map<String, Taxon> knownTaxonCache() {
		Map<String,Taxon> knownTaxon = new HashMap<String, Taxon> ();
		Map<String,Set<Taxon>> duplicateTaxa = new HashMap<String, Set<Taxon>> ();

		warn("Building knownTaxon cache...");
		for (TBPersistable tX : taxonHome.findAll(Taxon.class)) {
			Taxon t = (Taxon) tX;
			Integer legacyID = t.getTB1LegacyId();
			Integer ncbiID = t.getNcbiTaxId();
			String name = t.getName();
			if (ncbiID != null || name != null) {
				String uniqueKey = name + "," + ncbiID;
				if (knownTaxon.containsKey(uniqueKey)) {
					warn("Duplicate taxa with key '" + uniqueKey + "': " + idListString(t.getId(), knownTaxon.get(uniqueKey).getId()));
					if (mergeDuplicates) {
						if (! duplicateTaxa.containsKey(uniqueKey))
							duplicateTaxa.put(uniqueKey, new HashSet<Taxon>());
						duplicateTaxa.get(uniqueKey).add(t);
						duplicateTaxa.get(uniqueKey).add(knownTaxon.get(uniqueKey));
					}
				}
				knownTaxon.put(legacyID.toString(), t);
			}
		}
		warn("  ...finished (" + knownTaxon.size() + " taxa)");			

		if (mergeDuplicates && ! duplicateTaxa.isEmpty()) {
			// TODO: Use ObjectGroupMerger 20090416 MJD
			warn("Merging duplicate taxa...");
			MergeDuplicateTaxaInterface mdt = getMergeDuplicateTaxaService();
			for (Set<Taxon> taxonClass : duplicateTaxa.values()) {
				mdt.doMergeTaxonClass(taxonClass);
				warn("  ...finished");
			}
		}

		return knownTaxon;
	}

	/* 
	 * **************************************************************************
	 * ***** PHASE 2
	 * Create the underlying taxonvariants, or locate them if they already exist
	 */	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.util.JUNK#buildTaxonVariants(org.cipres.treebase.util.OptionalHashFieldReader, java.util.Map)
	 */
	public Map<String, TaxonVariant> buildTaxonVariants(
			OptionalHashFieldReader tvFH,
			Map<String,Taxon> taxon
	) {
		int uncommittedTaxonVariants = 0;
		final int maxUncomittedTaxonVariants = 100;

		Session sess = getSessionFactory().getCurrentSession();
		Transaction trans = sess.beginTransaction();

		/* 
		 * Cache the existing taxonvariant objects here; it will save a lot of database querying later
		 * Keys are taxonvariant fullnames
		 */
		Map<String, TaxonVariant> knownTaxonVariant = knownTaxonVariantCache();

		/*
		 *  Build up a map from the old TB1 taxonvariant ids to the resulting taxonvariant objects
		 *  We expect around 546225 tvs.  The default load factor in a HashMap is 0.75.
		 *  So presizing the map to 546225/0.75 should prevent dynamic resizing.
		 */
		for (Map<String,String> rec : tvFH) { 

			String oldID = rec.get("tvID");
			Integer oldIDInteger = Integer.decode(oldID);
			String tvFullName = rec.get("fullNameString");
			String nameBankID = rec.get("uBIO");

			TaxonVariant tv = knownTaxonVariant.get(oldID);

			Taxon t = taxon.get(rec.get("tID"));
			if (t == null) {
				warn("taxonVariant " + oldID + " calls for unknown taxon " + 
						rec.get("tID") + "; skipping");
				continue;
			}

			if (tv == null) {
				Long uBioID = TreebaseUtil.parseLong(rec.get("uBIO"), null);

				tv = new TaxonVariant(
						uBioID,
						rec.get("nameString"),
						rec.get("fullNameString"),
						rec.get("lexQual")
				);

				tv.setTaxon(t);
				tv.setTB1LegacyId(oldIDInteger);
				sess.save(tv);
				knownTaxonVariant.put(oldID, tv);
				uncommittedTaxonVariants++;
				warn("Made new TV '" + tvFullName + "'");
			} 

			if (tv.getTaxon() == null || tv.getTaxon().equals(t)) {
				Long oldId = tv.getTaxon() == null ? null : tv.getTaxon().getId();
				warn("Mapping TaxonVariant " + tvFullName + " to Taxon " + t.getId() + "; was " + oldId);
				tv.setTaxon(t);
				sess.update(tv);
				uncommittedTaxonVariants++;
			}

			if (uncommittedTaxonVariants >= maxUncomittedTaxonVariants) { 
				warn("Committing " + uncommittedTaxonVariants + " changes");
				trans.commit();
				trans.begin();
				uncommittedTaxonVariants = 0; 
			}

			if (tvFH.recNumber % 100 == 0) {
				warn("V " + tvFH.recNumber + " : " + tvFullName);
			}
		}

		return knownTaxonVariant;
	}


	/* (non-Javadoc)
	 * @see org.cipres.treebase.util.JUNK#checkTaxonVariants(org.cipres.treebase.util.OptionalHashFieldReader, java.util.Map)
	 */
	public Map<String, TaxonVariant> checkTaxonVariants(
			OptionalHashFieldReader tvFH, Map<String, Taxon> taxon) {
		Map<String, TaxonVariant> knownTaxonVariant = knownTaxonVariantCache();
		Map<String, TaxonVariant> taxonVariant = new HashMap<String, TaxonVariant> ();

		for (Map<String,String> rec : tvFH) { 
			String oldID = rec.get("tvID");
			String tvFullName = rec.get("fullNameString");	
			String nameBankID = rec.get("uBIO");

			String oldTaxonID = rec.get("tID");

			Taxon t = taxon.get(oldTaxonID);

			TaxonVariant tv = knownTaxonVariant.get(oldID);
			if (tv == null) {
				warn("File calls for missing taxonVariant with TB1id=" + oldID + ", oldID " + oldID);
				continue;
			}

			if (oldTaxonID == null) {
				if (tv.getTaxon() != null)
					warn("TB1id=" + oldID + ", Tv" + tv.getId() + " should have no taxon, but does");
			} else {
				if (t == null) {
					if (tv.getTaxon() != null)
						warn("TaxonVariant " + oldID + " calls for taxon " + rec.get("tID") + " which is missing from the DB");
				} else {
					if (tv.getTaxon() == null) 
						warn("TaxonVariant " + oldID + " should link to taxon " + rec.get("tID") + " (T" + t.getId() + ") but links to none");
					else if (! tv.getTaxon().getId().equals(t.getId())) 
						warn("TaxonVariant " + oldID + " should link to taxon " + rec.get("tID") + " (T" + t.getId() + ") but links to T" + tv.getTaxon().getId() + " instead");
				}
			}

			{
				String nameString = rec.get("nameString");
				if (nameString == null && tv.getName() != null) 
					warn("TB1id=" + oldID + " should have no name, but Tv" + tv.getId() + " has " + tv.getName());
				else if (nameString != null && tv.getName() == null)
					warn("TB1id=" + oldID + " should have name " + nameString + " but Tv" + tv.getId() + " has none");
				else if (nameString != null && tv.getName() != null)
					if (! nameString.equals(tv.getName()))
						warn("File says TB1id=" + oldID + " Tv" + tv.getId() + " should have name " + nameString + " but it has " + tv.getName());
			}

			{
				String lexQualF = rec.get("lexQual"), lexQualDB = tv.getLexicalQualifier();
				if (lexQualDB.equals("")) lexQualDB = null;
				if (lexQualF.equals("")) lexQualF = null;

				if (lexQualF == null && lexQualDB != null) 
					warn("TB1id=" + oldID + " should have no LQ, but Tv" + tv.getId() + " has " + lexQualDB);
				else if (lexQualF != null && lexQualDB == null)
					warn("TB1id=" + oldID + " should have LQ " + lexQualF + " but Tv" + tv.getId() + " has none");
				else if (lexQualF != null && lexQualDB != null)
					if (! lexQualF.equals(lexQualDB))
						warn("File says TB1id=" + oldID + " Tv" + tv.getId() + " should have LQ " + lexQualF + " but it has " + lexQualDB);
			}


			{
				Long uBioId;
				{
					String uBioIdString = rec.get("uBIO");
					uBioId = uBioIdString.equals("") ? null : Long.decode(uBioIdString);
				}

				if (uBioId == null && tv.getNamebankID() != null) 
					warn("TB1id=" + oldID + " should have no name, but Tv" + tv.getId() + " has " + tv.getNamebankID());
				else if (uBioId != null && tv.getNamebankID() == null)
					warn("TB1id=" + oldID + " should have name " + uBioId + " but Tv" + tv.getId() + " has none");
				else if (uBioId != null && tv.getNamebankID() != null)
					if (! uBioId.equals(tv.getNamebankID()))
						warn("File says TB1id=" + oldID + " Tv" + tv.getId() + " should have name " + uBioId + " but it has " + tv.getNamebankID());
			}

			taxonVariant.put(oldID, tv);
			knownTaxonVariant.remove(oldID);
		}

		for (String oldID : knownTaxonVariant.keySet()) 
			warn("Database contains excess taxonVariant Tv" + knownTaxonVariant.get(oldID).getId());

		return taxonVariant;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.util.JUNK#knownTaxonVariantCache()
	 */
	public Map<String, TaxonVariant> knownTaxonVariantCache() {
		Map<String,TaxonVariant> knownTaxonVariant = new HashMap<String, TaxonVariant>();
		Map<String,TaxonVariant> keyCheck = new HashMap<String, TaxonVariant>();

		warn("Building knownTaxonVariant cache...");
		for (TBPersistable tX : taxonHome.findAll(TaxonVariant.class)) {
			TaxonVariant tv = (TaxonVariant) tX;
			if (tv.getLexicalQualifier().equals("(trash)")) continue; // XXX
			String legacyID = tv.getTB1LegacyId() == null ? "null" : tv.getTB1LegacyId().toString();
			String fullName = tv.getFullName();
			Long nameBankID = tv.getNamebankID();
			String nameBankIDString = nameBankID == null ? "" : nameBankID.toString(); 
			Taxon t = tv.getTaxon();
			if (fullName != null || nameBankIDString != null) {
				String uniqueKey = fullName + "," + nameBankIDString;
				if (t != null) uniqueKey += "," + t.getName() + "," + t.getNcbiTaxId() ;
				else uniqueKey = null;

				if (uniqueKey != null && keyCheck.containsKey(uniqueKey))
					warn("Very similar taxonvariants with key '" + uniqueKey + "': " + idListString(tv.getId(), keyCheck.get(uniqueKey).getId()));

				if (knownTaxonVariant.containsKey(legacyID)) {
					warn("Duplicate taxonvariants with legacy ID '" + legacyID + "': " +
							idListString(tv.getId(),
									knownTaxonVariant.get(legacyID).getId()));
				}

				knownTaxonVariant.put(legacyID, tv);
			}

		}
		warn("  ...finished (" + knownTaxonVariant.size() + " taxonvariants)");
		return knownTaxonVariant;
	}

	/* 
	 * **************************************************************************
	 * ***** PHASE 3
	 * Connect existing taxonlabel objects to the taxonvariant objects from phase 2
	 */	
	/* (non-Javadoc)
	 * @see org.cipres.treebase.util.JUNK#linkTaxonLabels(org.cipres.treebase.util.OptionalHashFieldReader, java.util.Map)
	 */
	public void linkTaxonLabels(
			OptionalHashFieldReader tlFH, 
			Map<String, TaxonVariant> taxonVariant) {
		if (! linkTaxonLabels) return;

		Session sess = getCurrentSession();

		/* Cache existing taxonlabel objects to save many database queries
		 * Keys are taxonlabel labels, values are sets of taxonlabels with that label
		 */
		Map<String, Set<TaxonLabel>> knownTaxonLabel = knownTaxonLabelCache();

		int maxUncommittedChanges = 1000;
		int uncomittedChanges = 0;

		for (Map<String,String> rec : tlFH) {
			String taxonLabelString = rec.get("taxonlabel");
			TaxonVariant tv = taxonVariant.get(rec.get("tvID"));

			if (tv == null) {
				warn("L Record " + rec.get("") + " asks for unknown TV id " + rec.get("tvID") + "; skipping");
				continue;
			}

			Set<TaxonLabel> tls = knownTaxonLabel.get(taxonLabelString);

			if (tls == null) {
				warn("Unknown taxonlabel '" + taxonLabelString + "'");
			} else {
				for (TaxonLabel tl : tls) {
					if (tl.getTaxonVariant() == null || ! tl.getTaxonVariant().equals(tv)) {
						tl.setTaxonVariant(tv);
						uncomittedChanges++;
					}
				}
				if (uncomittedChanges >= maxUncommittedChanges) {
					sess.getTransaction().commit();
					sess.beginTransaction();
					uncomittedChanges = 0;
				}
			}

			if (tlFH.recNumber % 1000 == 0) {
				warn("L " + tlFH.recNumber + " : " + taxonLabelString);
			}
		}
		sess.getTransaction().commit();
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.util.JUNK#checkTaxonLabels(org.cipres.treebase.util.OptionalHashFieldReader, java.util.Map)
	 */
	public void checkTaxonLabels(OptionalHashFieldReader tlFH,
			Map<String, TaxonVariant> taxonVariant) {
		Map<String, Set<TaxonLabel>> knownTaxonLabel = knownTaxonLabelCache();

		for (Map<String,String> rec : tlFH) {
			String taxonLabelString = rec.get("taxonlabel");

			if (knownTaxonLabel.get(taxonLabelString) == null) {
				warn("File specifies mapping for nonexistent taxon label '" + taxonLabelString + "'");
				continue;
			}
			
			TaxonVariant tv = taxonVariant.get(rec.get("tvID"));
			
			if (tv == null) {
				warn("Dump file maps TL '" + taxonLabelString + "' to tv with unknownlegacy ID " + rec.get("tvID"));
				continue;
			}
			
			for (TaxonLabel tl : knownTaxonLabel.get(taxonLabelString)) {
				if (tl.getTaxonVariant() == null) {
					warn("Taxonlabel Tl" + tl.getId() + " is missing mapping to Tv" + tv.getId());
				} else if (! tl.getTaxonVariant().equals(tv)) {
					warn("Taxonlabel Tl" + tl.getId() + " points to Tv" + tl.getTaxonVariant().getId() +
							" but it should be Tv" + tv.getId());
				}
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.util.JUNK#knownTaxonLabelCache()
	 */
	public Map<String, Set<TaxonLabel>> knownTaxonLabelCache() {
		Map<String, Set<TaxonLabel>> knownTaxonLabel = new HashMap<String, Set<TaxonLabel>> ();
		for (TBPersistable tX : taxonHome.findAll(TaxonLabel.class)) {
			TaxonLabel tl = (TaxonLabel) tX;
			String label = tl.getTaxonLabel();
			if (label != null) {
				if (! knownTaxonLabel.containsKey(label))
					knownTaxonLabel.put(label, new HashSet<TaxonLabel> ());
				knownTaxonLabel.get(label).add(tl);
			}
		}
		return knownTaxonLabel;
	}


	public void populateTaxaLegacyIDs(OptionalHashFieldReader fh) {
		Map<String,Taxon> knownTaxon = knownTaxonCache();
		Map<String,Taxon> taxon = new HashMap<String, Taxon> (423533*4/3);

		for (Map<String,String> rec : fh) {
			String oldID = rec.get("tID");
			Integer oldIDInteger = Integer.decode(oldID);

			Long uBioId;
			{
				String uBioIdString = rec.get("uBIO");
				uBioId = uBioIdString.equals("") ? null : Long.decode(uBioIdString);
			}

			Integer ncbiTaxID;
			{
				String ncbiTaxidString = rec.get("NCBI");
				ncbiTaxID =  ncbiTaxidString == null    ? null : 
					ncbiTaxidString.equals("") ? null : Integer.decode(ncbiTaxidString);
			}
			String taxonName = rec.get("nameString");

			if (taxonName == null && ncbiTaxID == null)
				die("Taxon " + oldID + " has neither name nor NCBI id");

			String key = taxonName + "," + ncbiTaxID;
			Taxon t = knownTaxon.get(key);
			if (t == null) { 
				warn("File calls for missing taxon with TB1id=" + oldID);
				continue;
			}

			t.setTB1LegacyId(oldIDInteger);
			if (fh.recNumber % 100 == 0) {
				warn("T " + fh.recNumber + " : " + taxonName);
			}
		}
	}


	private String idListString(Long... ids) {
		StringBuilder s = new StringBuilder();
		for (int i=0; i < ids.length; i++) {
			s.append(ids[i].toString());
			if (i < ids.length-1) s.append(", ");
		}
		return s.toString();
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.util.JUNK#getTaxonHome()
	 */
	public TaxonHome getTaxonHome() {
		return taxonHome;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.util.JUNK#setTaxonHome(org.cipres.treebase.domain.taxon.TaxonHome)
	 */
	public void setTaxonHome(TaxonHome taxonHome) {
		this.taxonHome = taxonHome;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.util.JUNK#getTaxonLabelHome()
	 */
	public TaxonLabelHome getTaxonLabelHome() {
		return taxonLabelHome;
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.util.JUNK#setTaxonLabelHome(org.cipres.treebase.domain.taxon.TaxonLabelHome)
	 */
	public void setTaxonLabelHome(TaxonLabelHome taxonLabelHome) {
		this.taxonLabelHome = taxonLabelHome;
	}

	public MergeDuplicateTaxaInterface getMergeDuplicateTaxaService() {
		return (MergeDuplicateTaxaInterface) ContextManager.getBean("mergeDuplicateTaxa");
	}

}
