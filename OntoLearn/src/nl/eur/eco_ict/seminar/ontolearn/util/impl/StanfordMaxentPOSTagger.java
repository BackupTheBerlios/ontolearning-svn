/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.util.impl;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.eur.eco_ict.seminar.ontolearn.util.PartOfSpeechTagger;

import edu.stanford.nlp.ling.HasTag;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * @author Jasper
 */
public class StanfordMaxentPOSTagger implements PartOfSpeechTagger {
	
	public StanfordMaxentPOSTagger (){
		try {
			MaxentTagger.init (System.getProperty("user.dir") + "data\\stanford\\wsj3t0-18-bidirectional\\train-wsj-0-18.holder");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.util.PartOfSpeechTagger#tag(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> tag (String sentence) {
		Map<String, String> results = new HashMap<String, String>();
		List<Sentence> sentences = MaxentTagger.tokenizeText (new StringReader(sentence));
		Sentence temp = null;
		HasWord word = null;
		HasTag tag = null;
		
		Iterator<Sentence> i = sentences.iterator ();
		while(i.hasNext ()){
			temp = i.next ();
			temp = MaxentTagger.tagSentence (temp);
			Iterator<HasWord> iw = temp.iterator ();
			while(iw.hasNext ()){
				word = iw.next ();
				if (word instanceof HasTag){
					tag = (HasTag) word;
					results.put (word.word (), tag.tag ());
				}else{
					results.put (word.word (), null);
				}
			}
		}
		
		return results;
	}

	/**
	 * @throws Exception 
	 * @see nl.eur.eco_ict.seminar.ontolearn.util.PartOfSpeechTagger#tagInternal(java.lang.String)
	 */
	public String tagInternal (String sentence) throws Exception {
		return MaxentTagger.tagString (sentence);
	}

}
