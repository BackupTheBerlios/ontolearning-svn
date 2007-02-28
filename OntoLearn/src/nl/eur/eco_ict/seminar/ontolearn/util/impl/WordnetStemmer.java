/**
 * OntoLearn a seminar project of: - Remy Stibbe - Hesing Kuo - Nico Vaatstra -
 * Jasper Voskuilen
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
 */
public class WordnetStemmer implements Stemmer, Lemmatizer {
	protected static WordnetStemmer stemmer = null;

	protected final String propfile = "data" + File.separatorChar + "wordnet"
			+ File.separatorChar + "properties.xml";

	protected Dictionary dict = null;

	protected WordnetStemmer () {
		if (!JWNL.isInitialized ()){
			this.init ();
		}
	}
	
	public void init (){
		java.util.Locale.setDefault (java.util.Locale.ENGLISH);

		try {
			JWNL.initialize (new FileInputStream (this.propfile));
			this.dict = Dictionary.getInstance ();
		} catch (FileNotFoundException e) {
			System.err
					.println ("Error accessing the jwordnet properties file: "
							+ this.propfile);
			e.printStackTrace ();
		} catch (JWNLException e) {
			e.printStackTrace ();
		}
	}

	public static WordnetStemmer get () {
		if (stemmer == null) {
			stemmer = new WordnetStemmer ();
		}
		return stemmer;
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.util.Stemmer#stem(java.lang.String)
	 */
	public String stem (String word) {
		final POS[] types = new POS[]{POS.NOUN, POS.VERB, POS.ADVERB, POS.ADJECTIVE};
		String result = null;
		
		for (int i = 0; i < types.length && result == null; i++){
			try{
				result = this.getLemma (word, types[i]);
				if (result.equals (word)){
					result = null;
				}
			}catch(Exception e){
				result = null;
			}
		}
		if (result == null){
			result = word;
		}

		return result;
	}
	
	protected String getLemma (String word, POS pos) throws Exception{
		IndexWord iword;
		
		if (!JWNL.isInitialized ()){
			this.init ();
		}
		
		iword = this.dict.lookupIndexWord (pos,word);
		return iword.getLemma ();
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.util.Lemmatizer#getLemma(java.lang.String)
	 */
	public String getLemma (String word) {
		return this.stem (word);
	}

}
