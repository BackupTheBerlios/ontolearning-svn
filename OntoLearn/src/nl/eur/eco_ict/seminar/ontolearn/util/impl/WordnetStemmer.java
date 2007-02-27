/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.util.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;
import nl.eur.eco_ict.seminar.ontolearn.util.Lemmatizer;
import nl.eur.eco_ict.seminar.ontolearn.util.Stemmer;

/**
 * @author Jasper
 *
 */
public class WordnetStemmer implements Stemmer, Lemmatizer {
	protected static WordnetStemmer stemmer = null;
	protected final String propfile = "data"+File.separatorChar+"wordnet"+File.separatorChar+"properties.xml";
	
	protected WordnetStemmer () {
		
		try {
			JWNL.initialize(new FileInputStream(this.propfile));
		} catch (FileNotFoundException e) {
			System.err.println ("Error accessing the jwordnet properties file: " + this.propfile);
			e.printStackTrace();
		} catch (JWNLException e) {
			e.printStackTrace();
		}
	}
	
	public static WordnetStemmer get (){
		if (stemmer == null){
			stemmer = new WordnetStemmer ();
		}
		return stemmer;
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.util.Stemmer#stem(java.lang.String)
	 */
	public String stem (String word){
		IndexWord iword;
		try {
			iword = Dictionary.getInstance().getIndexWord (POS.NOUN, word);
			return iword.getLemma ();
		} 
		catch (JWNLException e) {
			e.printStackTrace();
		}
		return word;
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.util.Lemmatizer#getLemma(java.lang.String)
	 */
	public String getLemma (String word) {
		return this.stem (word);
	}

}
