/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.util;

import nl.eur.eco_ict.seminar.ontolearn.util.impl.WordnetStemmer;

/**
 * @author Jasper
 *
 */
public interface Lemmatizer {
	
	/**
	 * @param word
	 * @return the base form of the given word or the word itself if a base form can not be found
	 */
	public String getLemma (String word);
	
	/**
	 * Easy access to lemmatizers
	 * @author Jasper
	 */
	public final class Factory {
	
		/**
		 * @return a default lemmatizer
		 */
		public static Lemmatizer getInstance (){
			return WordnetStemmer.get();
		}
	}

}
