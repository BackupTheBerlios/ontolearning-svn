/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.Reader;
import java.io.StringReader;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * @author Jasper
 *
 */
public class StanfordMaxEntTokenizer implements Tokenizer {

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.util.Tokenizer#toSentences(java.lang.String)
	 */
	public List<String> toSentences (String s) {
		return this.toSentences (new StringReader(s));
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.util.Tokenizer#toSentences(java.io.Reader)
	 */
	@SuppressWarnings("unchecked")
	public List<String> toSentences (Reader r) {
		List<Sentence> sentences = MaxentTagger.tokenizeText(r);
		List<String> results = new ArrayList<String>();
		Iterator<Sentence> i = sentences.iterator ();
		while(i.hasNext()){
			results.add (i.next ().toString());
		}
		return results;
	}

}
