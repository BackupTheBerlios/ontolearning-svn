/**
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.extractor;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.*;

import edu.stanford.nlp.ling.Sentence;
import nl.eur.eco_ict.seminar.ontolearn.Extractor;
import nl.eur.eco_ict.seminar.ontolearn.Ontology;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.Document;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * @author 300353jv
 *
 */
public class HearstExtractor implements Extractor {

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Extractor#parse(nl.eur.eco_ict.seminar.ontolearn.datatypes.Document, nl.eur.eco_ict.seminar.ontolearn.Ontology)
	 */
	public void parse (Document doc, Ontology ontology) {
		// TODO Auto-generated method stub
		System.out.println("HearstExtractor is parsing.");
		
		try {
			List l = MaxentTagger.tokenizeText(doc.readAbstracts());
				
			for (int j = 0, sz = l.size(); j < sz; j++) {
				  Sentence s = (Sentence) l.get(j);
				  // Each sentence in each line in the abstract can now be parsed individually
				  
				  // Run the pattern finder on the sentence
				  // foundPatterns contains the String NP0 as key, and the String[] NPx as value(s) <-- NPx is an array!
				  Patternator myPatternator = new Patternator();
				  HashMap foundPatterns = myPatternator.parseString(s.toString());
				  
				  // Test to see if the HashMap works
				  
				  Collection collection = foundPatterns.entrySet();
				  Iterator iterator = collection.iterator();
				  
				  while(iterator.hasNext())
				  { 
				      Map.Entry entry = (Map.Entry)iterator.next();
				      String key = (String)entry.getKey();
				      String[] value = (String[])entry.getValue();
				      
				      // Display NP0 and NPx from foundPatterns (testing)
					  System.out.println("NP0: "+key);
					  System.out.println("NPx: "+Arrays.asList(value));
				  }
				  
				  
				  // Run the POS tagger on the sentence
				  // COMMENTED OUT TO TEST PATTERN FUNCTIONALITY!!!!
				  // System.out.println(posTagger.doTag(s) + " \r\n");
				  // String x = posTagger.doTag(s); <-- x = pos tagged version of sentence s
		    }
		}
		catch (FileNotFoundException e) {
			System.out.println("Error: "+e);
		}
	}

}
