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
public interface Lemmatizer {
	
	/**
	 * Easy access to lemmatizers
	 * @author Jasper
	 */
	public final class Factory {
		protected static Lemmatizer defLem = null; //TODO create instance
		
		/**
		 * @return a default lemmatizer
		 */
		public static Lemmatizer getInstance (){
			return defLem;
		}
	}

}
