/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.util;

import nl.eur.eco_ict.seminar.ontolearn.util.impl.EnglishStemmerKnowceans;

/**
 * @author Jasper
 *
 */
public interface Stemmer {
	
	/**
	 * @param word a single word if more words are supplied only the first is stemmed
	 * @return the stem (root morpheme) of the supplied word
	 */
	public String stem (String word);

	/**
	 * Easy access to Stemmers
	 * @author Jasper
	 */
	public final class Factory {
		protected static Stemmer defStem = new EnglishStemmerKnowceans ();
		
		/**
		 * @return a default stemmer
		 */
		public static Stemmer getInstance (){
			return defStem;
		}
	}
}
