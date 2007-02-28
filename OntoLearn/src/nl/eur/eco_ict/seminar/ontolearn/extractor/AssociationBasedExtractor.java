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
		List<CorrOcc> data = this.waardeDB.getCorrOcc(wordX, wordY);
		
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
	
	
	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Extractor#onFinish(nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology)
	 */
	public void onFinish (Ontology ontology) {
		// AssociationsResult endResults = new AssociationsResult();
		System.out.println("Running onFinish() for the Association-based extractor.");
		
		try {
			// Test: Check the correlation between "workers" and "growth":
			double testResult = correlation("workers", "growth");
			System.out.println("Correlation between workers and growth: "+testResult);

			// Test: Check the correlation between "monkeys" and "growth":
			testResult = correlation("monkeys", "growth");
			System.out.println("Correlation between monkeys and growth: "+testResult);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
