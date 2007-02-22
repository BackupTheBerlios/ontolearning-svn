/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.util;

import java.util.Map;

import nl.eur.eco_ict.seminar.ontolearn.util.impl.StanfordMaxentPOSTagger;
import nl.eur.eco_ict.seminar.ontolearn.util.impl.StanfordParser;

/**
 * @author jasper
 *
 */
public interface PartOfSpeechTagger {
	
	/**
	 * @param sentence
	 * @return the same sentence only marked up with the relevant tags
	 * @throws Exception 
	 */
	public String tagInternal (String sentence) throws Exception;
	
	/**
	 * @param sentence
	 * @return a map with the original sentence parts as key and the tags as value
	 */
	public Map<String, String> tag (String sentence);
	
	/**
	 * @author jasper
	 */
	public final class Factory{
		protected static PartOfSpeechTagger stanfordTagger = new StanfordMaxentPOSTagger() ;
		protected static PartOfSpeechTagger stanfordlexer = new StanfordParser();
		protected static PartOfSpeechTagger defTagger = stanfordlexer;
		
		
		/**
		 * @return a default part of speech tagger
		 */
		public static PartOfSpeechTagger getInstance(){
			return defTagger;
		}
		
		/**
		 * @return a part of speech tagger provided by stanford 
		 */
		public static PartOfSpeechTagger getStanfordInstance(){
			return stanfordTagger;
		}
		
		public static PartOfSpeechTagger getStanfordLexer (){
			return stanfordlexer;
		}
	}
}
