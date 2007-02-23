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
	 * @return the same sentence only marked up with the relevant tags like this example: From/IN the/DT beginning/NN ,/, it/PRP took/VBD a/DT man/NN with/IN extraordinary/JJ qualities/NNS to/TO succeed/VB in/IN Mexico/NNP ,/, "/" says/VBZ Kimihide/NNP Takimura/NNP ,/, president/NN of/IN Mitsui/NNS/NNP group/NN 's/POS Kensetsu/NNP Engineering/NNP Inc./NNP unit/NN ./.
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
		protected static PartOfSpeechTagger stanfordTagger = null;
		protected static PartOfSpeechTagger stanfordlexer = null;
		
		
		/**
		 * @return a default part of speech tagger
		 */
		public static PartOfSpeechTagger getInstance(){
			return getStanfordLexer();
		}
		
		/**
		 * @return a part of speech tagger provided by stanford 
		 */
		public static PartOfSpeechTagger getStanfordInstance(){
			if (stanfordTagger == null){
				stanfordTagger = new StanfordMaxentPOSTagger() ;
			}
			return stanfordTagger;
		}
		
		public static PartOfSpeechTagger getStanfordLexer (){
			if (stanfordlexer == null){
				stanfordlexer = new StanfordParser();
			}
			return stanfordlexer;
		}
	}
}
