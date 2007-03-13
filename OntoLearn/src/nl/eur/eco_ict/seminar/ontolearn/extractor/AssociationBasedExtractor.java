
/**
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.extractor;

import java.sql.SQLException;
import java.util.List;

import nl.eur.eco_ict.seminar.ontolearn.Extractor;
import nl.eur.eco_ict.seminar.ontolearn.association.Occurance;
import nl.eur.eco_ict.seminar.ontolearn.association.AssociationDatabase;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.Document;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.impl.CorrOcc;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.impl.Correlation;
import nl.eur.eco_ict.seminar.ontolearn.util.PartOfSpeechTagger;
import nl.eur.eco_ict.seminar.ontolearn.util.Tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntProperty;

/**
 * @author Remy
 */
public class AssociationBasedExtractor implements Extractor {

	PartOfSpeechTagger posTagger = PartOfSpeechTagger.Factory.getStanfordLexer ();
	Tokenizer tokenizer = Tokenizer.Factory.getInstance ();
	AssociationDatabase waardeDB = new AssociationDatabase ();
	
	int abstractCounter;
	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Extractor#parse(nl.eur.eco_ict.seminar.ontolearn.datatypes.Document,
	 *      nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology)
	 */
	
	public AssociationBasedExtractor() {
		this.waardeDB.setStorage (true);
		this.waardeDB.cleanDB();
	}
	
	public AssociationBasedExtractor(boolean cleanDB) {
		if(cleanDB) {
			this.waardeDB.cleanDB();
		}
	}
	
	public void parse (Document doc, Ontology ontology) throws Throwable {
		this.abstractCounter = 0;
		
		Iterator<BufferedReader> abstracts = doc.readAbstracts ().iterator ();
		while(abstracts.hasNext()){			
			this.abstractCounter++;
			this.parse (abstracts.next(), ontology, doc);
		}
	}
	
	protected void parse (BufferedReader reader, Ontology ontology, Document doc) throws Throwable {
		
		try {
			List<String> myList = this.tokenizer.toSentences(reader);

			for (int x = 0, mySize = myList.size (); x < mySize; x++) {
				String mySentence = myList.get (x);
				
				String y = this.posTagger.tagInternal (mySentence);
				
				Scanner scanner = new Scanner (y).useDelimiter ("\\s");
				
				while (scanner.hasNext ()) {
					String oneWordPOS = scanner.next ();
					if (oneWordPOS.contains ("/NN")) {
						int endWordPosition = oneWordPOS.indexOf ("/");
						String test = oneWordPOS.substring (0, endWordPosition);
						String test2 = test.toLowerCase ().replaceAll("\\x5C","");
						
						this.storeWord(this.getDocumentName (doc), test2);
					}
				}
			}
			System.out.println(this.tostring ());
		} catch (IOException e) {
			System.out.println ("Error: " + e);
		} catch (SQLException e) {
			System.out.println ("Error: " + e);
		}
	}
	
	public void storeWord(String docName, String word) {
		Occurance tempOccurance = this.getOccurance(docName, word);
		
		if(tempOccurance == null) {
			// This word/docName combination has not been recorded yet. Do so now:
			tempOccurance = new Occurance();
			tempOccurance.setWord(word);
			tempOccurance.setDocumentName(docName);
			tempOccurance.setWordCount(1);
			try {
				this.waardeDB.addConcept (tempOccurance);
			} catch (SQLException e) {
				System.err.println ("Something went wrong adding a concept to the database. " + e.getMessage ());
			}
		}
		else {
			// This word/docName combination exists. Increment wordcount:
			tempOccurance.setWordCount(tempOccurance.getWordCount()+1);
		}		
	}
	
	public Occurance getOccurance (String docName, String word) {
		return this.waardeDB.getOccurance (docName, word);
	}

	public void add (String word, Document doc) throws SQLException {
		Occurance oc = new Occurance ();
		oc.word = word;
		oc.documentName = doc.getName ();
		oc.wordCount = 1;
		this.waardeDB.addConcept (oc);
	}

	public Occurance getOccurance (String word, Document doc) {
		return this.waardeDB.getOccurance (this.getDocumentName(doc), word);
	}
	
	protected String getDocumentName (Document doc){
		return doc.getName ()+"#"+this.abstractCounter;
	}

	public String tostring () throws SQLException {
		return this.getName () + " knows about " + this.waardeDB.getDocCount () + " documents containing " + this.waardeDB.getWordCount () + " nouns."; 
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Extractor#getName()
	 */
	public String getName () {
		return "Association based extractor";
	}

	
	protected double correlation (String wordX, String wordY) throws SQLException {
		double avgx = this.waardeDB.getAvgWord (wordX);
		double avgy = this.waardeDB.getAvgWord (wordY);
		// double docx = this.waardeDB.getWordcountPerDocument(wordX, doc);
		// double docy = this.waardeDB.getWordcountPerDocument(wordY, doc);
		
		double result =0;
		// Laad data in CorrOcc data structuur
		Collection<CorrOcc> data = this.waardeDB.getCorrOcc(wordX, wordY);
		
		Iterator<CorrOcc> i = data.iterator ();
		CorrOcc current = null;
		double teller=0, noemerx=0, noemery=0, noemer=0;
		
		while (i.hasNext()){
			current  = i.next ();
			teller += (current.getXCount() - avgx) * (current.getYCount() - avgy);
			noemerx += Math.pow (current.getXCount() - avgx, 2);
			noemery += Math.pow (current.getYCount() - avgy, 2);
		}
		
		noemer = Math.sqrt (noemerx*noemery);
			
		result = teller / noemer;
			
		return result;
	}
	
	
	
	public Correlation getCorrelation (String wordA, String wordB) throws SQLException {
		Correlation result = new Correlation();
		
		// Get number of rows in the table --> (number of documents.)
		double tableRows = this.waardeDB.getDocCount();
		// System.out.println("tableRows: "+tableRows);
		
		// Get number of co-occurances in the table.
		double numAandB = this.waardeDB.getCoOccCount(wordA, wordB);
		// System.out.println("numAandB: "+numAandB);	
		
		// Get number of occurances for both words in the table.
		double numA = this.waardeDB.getWordCount(wordA);
		// System.out.println("numA: "+numA);
		double numB = this.waardeDB.getWordCount(wordB);	
		// System.out.println("numB: "+numB);
		
		// Calculate support: co-occurances / rows in the table
		// calculate support for A-->B:
		double supportAtoB = numAandB / tableRows;
		// System.out.println("supportAtoB: "+supportAtoB);
		result.setSupportAtoB(supportAtoB);
		// calculate support for B-->A:
		double supportBtoA = numAandB / tableRows;
		// System.out.println("supportBtoA: "+supportBtoA);
		result.setSupportBtoA(supportBtoA);
			
		// Calculate confidence: co-occurances / occurances of word A in the table.
		// calculate confidence for A-->B:
		double confidenceAtoB = numAandB / numA;
		// System.out.println("confidenceAtoB: "+confidenceAtoB);
		result.setConfidenceAtoB(confidenceAtoB);
		// calculate confidence for B-->A:
		double confidenceBtoA = numAandB / numB;
		// System.out.println("confidenceBtoA: "+confidenceBtoA);
		result.setConfidenceBtoA(confidenceBtoA);
		
		// Calculate lift: confidence / occurances of word B.
		// calculate lift for A-->B:
		double liftAtoB = confidenceAtoB / numB;
		// System.out.println("liftAtoB: "+liftAtoB);
		result.setLiftAtoB(liftAtoB);
		// calculate lift for B-->A:
		double liftBtoA = confidenceBtoA / numA;
		// System.out.println("liftBtoA: "+liftBtoA);
		result.setLiftBtoA(liftBtoA);
				
		return result;
	}
	
	public void parseWordPair(String wordA, String wordB, Ontology ontology) {
		double pearsonThreshold = 0;
		double confidenceThreshold = 0.05;
		double supportThreshold = 0.05;		

		
		try {
			double pearsonCoefficient = Math.abs(this.correlation(wordA, wordB));
			Correlation wezelMethod = this.getCorrelation(wordA, wordB);
			
			double confidence = 0;
			double support = 0;
			String wordX;
			String wordY;
			
			if(wezelMethod.getConfidenceAtoB() >= wezelMethod.getConfidenceBtoA()) {
				confidence = wezelMethod.getConfidenceAtoB();
				support = wezelMethod.getSupportAtoB();
				wordX = wordA;
				wordY = wordB;
			}
			else {
				confidence = wezelMethod.getConfidenceBtoA();
				support = wezelMethod.getSupportBtoA();
				wordX = wordB;
				wordY = wordA;
			}
			
			if((pearsonCoefficient > pearsonThreshold) && (confidence > confidenceThreshold) && (support > supportThreshold)) {
				// This is where wordX-->wordY relations should be added to the Ontology:
				System.out.println("====================");
				System.out.println("This relation is added: "+ wordX + " --> " + wordY);
				System.out.println("vicore: " + pearsonCoefficient);
				System.out.println("Confidence (" + confidence + ") Support ("+support+")");
				this.addRelation (wordX, wordY, Math.pow (pearsonCoefficient*confidence*support,-2), ontology);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * The following relation is added to the ontology
	 * wordX ---strength---> wordY
	 * @param wordX
	 * @param wordY
	 * @param strength
	 * @param ontology
	 */
	protected void addRelation (String wordX, String wordY, double strength, Ontology ontology){
		final String wordRelation = "associatedWith";
		final String relationstrength = "hasStrength";
		
		OntProperty p = ontology.getProperty (wordRelation);
		OntProperty association = ontology.getProperty (relationstrength);
		OntClass x = ontology.getOClass (wordX);
		OntClass y = ontology.getOClass (wordY);
		
		if (p == null){
			p = ontology.addDataProperty (wordRelation);
			p.addComment ("", null);
		}
		if (x == null){
			x = ontology.addOClass (wordX);
		}
		if (y == null){
			y = ontology.addOClass (wordY);
		}
		if (association == null){
			association = ontology.addDataProperty (relationstrength);
			association.addComment ("value between 0-1", null);
		}
		p.addProperty (association, ""+strength);
		x.addProperty (p, y);
		x.addComment (wordRelation + " with " + y + " strength: " + strength, null);
	}
	
	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Extractor#onFinish(nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology)
	 */
	public void onFinish (Ontology ontology) {
		// AssociationsResult endResults = new AssociationsResult();
		System.out.println("Running onFinish() for the Association-based extractor.");
		
		String[] documentString = null;
		String[] wordsPerDocument = null;

		
		try {
			System.out.println("Getting document list.");
			documentString = this.waardeDB.getSignificantWordsPerDocument ();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if (documentString != null) {
				for (int k = 0; k < documentString.length ; k++) {
					System.out.println("Parsing document: "+documentString[k]);
					wordsPerDocument = this.waardeDB.getAllWordsPerDocument(documentString[k]);
					if (wordsPerDocument != null) {

						for (int i = 0; i < wordsPerDocument.length; i++) {
							if (wordsPerDocument[i] != null) {
								for (int l = i + 1; l < wordsPerDocument.length ; l++) {
									if (wordsPerDocument[l] != null) {
										parseWordPair(wordsPerDocument[i], wordsPerDocument[l], ontology);
									}									
								}
							}
						} 
					}
				}	
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
