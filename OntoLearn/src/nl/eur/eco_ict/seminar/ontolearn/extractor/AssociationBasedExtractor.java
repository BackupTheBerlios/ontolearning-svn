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
import nl.eur.eco_ict.seminar.ontolearn.testzone.AssociationsResult;
import nl.eur.eco_ict.seminar.ontolearn.util.PartOfSpeechTagger;
import nl.eur.eco_ict.seminar.ontolearn.util.Tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author Remy
 */
public class AssociationBasedExtractor implements Extractor {

	protected Collection<Occurance> occuranceMatrix = new HashSet<Occurance> ();

	AssociationDatabase waardeDB = new AssociationDatabase ();
	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Extractor#parse(nl.eur.eco_ict.seminar.ontolearn.datatypes.Document,
	 *      nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology)
	 */
	
	public AssociationBasedExtractor() {
		this.waardeDB.cleanDB();
	}
	
	public AssociationBasedExtractor(boolean cleanDB) {
		if(cleanDB) {
			this.waardeDB.cleanDB();
		}
	}
	
	public void parse (Document doc, Ontology ontology) throws Throwable {
		Iterator<BufferedReader> abstracts = doc.readAbstracts ().iterator ();
		while(abstracts.hasNext()){
			this.parse (abstracts.next(), ontology, doc);
		}
	}
	
	protected void parse (BufferedReader reader, Ontology ontology, Document doc) throws Throwable {
		try {
			PartOfSpeechTagger posTagger = PartOfSpeechTagger.Factory.getStanfordInstance();
			Tokenizer tokenizer = Tokenizer.Factory.getInstance ();
			// List<String> myList = tokenizer.toSentences (doc.readAbstracts());
			List<String> myList = tokenizer.toSentences(reader);

			for (int x = 0, mySize = myList.size (); x < mySize; x++) {
				String mySentence = myList.get (x);
				// System.out.println ("mySentence: "+mySentence);
				//String myPOSString = posTagger.tagInternal (mySentence);

				// System.out.println ("mySentence: "+ myPOSString);

				// System.out.println(posTagger.tagInternal(mySentence.toString())
				// + " \r\n");
				String y = posTagger.tagInternal (mySentence);
				//String y = this.myTagger.tagInternal (mySentence.toString ());
				
				Scanner scanner = new Scanner (y).useDelimiter ("\\s");

				while (scanner.hasNext ()) {
					String oneWordPOS = scanner.next ();
					if (oneWordPOS.contains ("/NN")) {
						int endWordPosition = oneWordPOS.indexOf ("/");
						String test = oneWordPOS.substring (0, endWordPosition);
						String test2 = test.toLowerCase ().replaceAll("\\x5C","");
						if (this.getOccurance (test2, doc) == null) {
							this.add (test2, doc);
						} else {
							int ocWordcount = this.getOccurance (test2, doc).wordCount++;
							this.waardeDB.updateConcept (doc.getName(), test2, new Integer(ocWordcount));
						}
					}
				}
			}
			this.conceptsToDatabase ();
		} catch (IOException e) {
			System.out.println ("Error: " + e);
		} catch (SQLException e) {
			System.out.println ("Error: " + e);
		}
	}

	public void add (String word, Document doc) throws SQLException {
		Occurance oc = new Occurance ();
		oc.word = word;
		oc.documentName = doc.getName ();
		oc.wordCount = 1;
		this.occuranceMatrix.add (oc);

		// add to database

		int i = oc.wordCount;
		this.waardeDB.addConcept (oc.documentName, oc.word, new Integer(i));

	}

	public Occurance getOccurance (String word, Document doc) {
		Iterator<Occurance> i = this.occuranceMatrix.iterator ();
		Occurance oc;
		while (i.hasNext ()) {
			oc = i.next ();
			if (oc.documentName.equals (doc.getName ())
					&& oc.word.equals (word)) {
				return oc;
			}
		}
		return null;
	}

	public String tostring () {
		Iterator<Occurance> i = this.occuranceMatrix.iterator ();
		Occurance oc;
		String result = "";
		while (i.hasNext ()) {
			oc = i.next ();
			result += oc.word + " " + oc.documentName + " " + oc.wordCount;
			result += "\n";
		}
		return result;
	}

	public void conceptsToDatabase () {
		// AssociationDatabase waardeDB = new AssociationDatabase();
		// waardeDB.addConcepts ();
		// System.out.println (this.tostring ());
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
		// System.out.println("Word A: "+wordA);
		// System.out.println("Word B: "+wordB);
		Correlation result = new Correlation();
		
		// Get number of rows in the table --> (number of documents.)
		double tableRows = this.waardeDB.getDocCount();
		// System.out.println("No. of docs: "+tableRows);
		
		// Get number of co-occurances in the table.
		double numAandB = this.waardeDB.getCoOccCount(wordA, wordB);
		// System.out.println("No. of co-occurances: "+numAandB);		
		
		// Get number of occurances for both words in the table.
		double numA = this.waardeDB.getWordCount(wordA);
		// System.out.println("No. of occurances A: "+numA);
		double numB = this.waardeDB.getWordCount(wordB);
		// System.out.println("No. of occurances B: "+numB);	
		
		// Calculate support: co-occurances / rows in the table
		// calculate support for A-->B:
		double supportAtoB = numAandB / tableRows;
		result.setSupportAtoB(supportAtoB);
		// System.out.println("Support A-->B: "+supportAtoB);
		// calculate support for B-->A:
		double supportBtoA = numAandB / tableRows;
		result.setSupportBtoA(supportBtoA);
		// System.out.println("Support B-->A: "+supportBtoA);
			
		// Calculate confidence: co-occurances / occurances of word A in the table.
		// calculate confidence for A-->B:
		double confidenceAtoB = numAandB / numA;
		result.setConfidenceAtoB(confidenceAtoB);
		// System.out.println("Confidence A-->B: "+confidenceAtoB);
		// calculate confidence for B-->A:
		double confidenceBtoA = numAandB / numB;
		result.setConfidenceBtoA(confidenceBtoA);
		// System.out.println("Confidence B-->A: "+confidenceBtoA);
		
		// Calculate lift: confidence / occurances of word B.
		// calculate lift for A-->B:
		double liftAtoB = confidenceAtoB / numB;
		result.setLiftAtoB(liftAtoB);
		// System.out.println("Lift A-->B: "+liftAtoB);
		// calculate lift for B-->A:
		double liftBtoA = confidenceBtoA / numA;
		result.setLiftBtoA(liftBtoA);
		// System.out.println("Lift B-->A: "+liftBtoA);
				
		return result;
	}
	
	public void parseWordPair(String wordA, String wordB) {
		double pearsonThreshold = 0;
		double confidenceThreshold = 0.6;
		double supportThreshold = 0.5;		
		
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
				System.out.println("====================");
				System.out.println("This relation should be added: "+ wordX + " --> " + wordY);
				System.out.println("vicore: " + pearsonCoefficient);
				System.out.println("Confidence (" + confidence + ") Support ("+support+")");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
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
			documentString = this.waardeDB.getSignificantWordsPerDocument ();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if (documentString != null) {
				for (int k = 0; k < documentString.length ; k++) {
					wordsPerDocument = this.waardeDB.getAllWordsPerDocument(documentString[k]);
					if (wordsPerDocument != null) {

						for (int i = 0; i < wordsPerDocument.length; i++) {
							if (wordsPerDocument[i] != null) {
								for (int l = i + 1; l < wordsPerDocument.length ; l++) {
									if (wordsPerDocument[l] != null) {
										parseWordPair(wordsPerDocument[i], wordsPerDocument[l]);
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
