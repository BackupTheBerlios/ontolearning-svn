/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.util;

import java.io.Reader;
import java.util.List;

import nl.eur.eco_ict.seminar.ontolearn.util.impl.StanfordMaxEntTokenizer;

/**
 * @author jasper
 *
 */
public interface Tokenizer {
	/**
	 * @param s a piece of text
	 * @return the sentences identified within <code>s</code>
	 */
	public List<String> toSentences (String s);
	
	/**
	 * @param r a reader which provides access to some piece of text
	 * @return the sentences identified within the text supplied by <code>r</code>
	 */
	public List<String> toSentences (Reader r);
	
	/**
	 * Easy access to the appropiate instances
	 * @author jasper
	 */
	public final class Factory {
		protected static Tokenizer stanfordTok = new StanfordMaxEntTokenizer();
		protected static Tokenizer defTok = stanfordTok;
		
		/**
		 * @return get a default Tokenizer
		 */
		public static Tokenizer getInstance (){
			return defTok;
		}
		
		/**
		 * @return get the Tokenizer provide by stanford
		 */
		public static Tokenizer getStanfordInstance (){
			return stanfordTok;
		}
	}
}
