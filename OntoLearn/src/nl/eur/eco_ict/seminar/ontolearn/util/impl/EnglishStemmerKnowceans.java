/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.util.impl;

import net.sf.snowball.ext.EnglishStemmer;
import nl.eur.eco_ict.seminar.ontolearn.util.Stemmer;

/**
 * @author Mr H
 *
 */
public class EnglishStemmerKnowceans implements Stemmer {
	
	private EnglishStemmer stemmer = null;

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.util.Stemmer#stem(java.lang.String)
	 */
	public String stem (String word) {
		this.getStemmer ().setCurrent (word);
		
		boolean success = this.getStemmer ().stem ();
		if (success){
		return this.getStemmer ().getCurrent ();
		}
		return word;
	}
	
	protected EnglishStemmer getStemmer (){
		if (this.stemmer == null){
			this.stemmer = new EnglishStemmer ();
		}
		return this.stemmer;
	}

}
