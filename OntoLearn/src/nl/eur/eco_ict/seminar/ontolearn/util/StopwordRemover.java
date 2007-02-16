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
public interface StopwordRemover {
	/**
	 * @param sentence
	 * @return the same sentence without stopwords
	 */
	public String removeStopWords (String sentence);
	
	/**
	 * Easy access to stopword removers
	 * @author Jasper
	 */
	public final class Factory {
		protected static StopwordRemover defRemover = null; // TODO create instance
		
		/**
		 * @return a default stopword remover
		 */
		public static StopwordRemover getInstance (){
			return defRemover;
		}
	}
}
