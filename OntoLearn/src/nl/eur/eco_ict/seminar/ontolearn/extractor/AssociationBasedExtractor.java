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
import nl.eur.eco_ict.seminar.ontolearn.util.PartOfSpeechTagger;
import nl.eur.eco_ict.seminar.ontolearn.util.impl.StanfordMaxentPOSTagger;
import nl.eur.eco_ict.seminar.ontolearn.util.Tokenizer;
import java.io.IOException;
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
	PartOfSpeechTagger myTagger = new StanfordMaxentPOSTagger();
	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Extractor#parse(nl.eur.eco_ict.seminar.ontolearn.datatypes.Document,
	 *      nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology)
	 */
	public void parse (Document doc, Ontology ontology) throws Throwable {
		try {
			// PartOfSpeechTagger posTagger = PartOfSpeechTagger.Factory.getInstance ();
			Tokenizer tokenizer = Tokenizer.Factory.getInstance ();
			List<String> myList = tokenizer.toSentences (doc.readAbstracts ());

			for (int x = 0, mySize = myList.size (); x < mySize; x++) {
				String mySentence = myList.get (x);
				// System.out.println ("mySentence: "+mySentence);
				// String myPOSString = posTagger.tagInternal (mySentence);

				// System.out.println ("mySentence: "+ myPOSString);

				// System.out.println(posTagger.tagInternal(mySentence.toString())
				// + " \r\n");
				// String y = posTagger.tagInternal (mySentence.toString ());
				String y = this.myTagger.tagInternal (mySentence.toString ());
				
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
		System.out.println (this.tostring ());
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Extractor#getName()
	 */
	public String getName () {
		return "Association based extractor";
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Extractor#onFinish(nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology)
	 */
	public void onFinish (Ontology ontology) {}
}
