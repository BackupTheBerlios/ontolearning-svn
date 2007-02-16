/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.util;

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
		protected static Stemmer defLem = null; // TODO create instance
		
		/**
		 * @return a default stemmer
		 */
		public static Stemmer getInstance (){
			return defLem;
		}
	}
}
