package src.nl.eur.eco_ict.seminar.ontlearning.kuovosvaasti;

import java.io.*;
import java.util.*;

import com.hp.hpl.jena.ontology.OntClass;

import edu.stanford.nlp.ling.Sentence;

public class PatternBasedExtractor {
	// ABSTRACTSPATH: Directory holding the abstracts to be processed
	private static String ABSTRACTSPATH = System.getProperty("user.dir")+"\\abstracts\\";
	
	public static void main(String[] args) {
		System.out.println("PatternBasedExtractor: Running...");
		OntologyManager.get (OntologyManager.OpenMode.NEWONT_NAMED.setName ("http://voskuilen.org/ontlearning/eco/"));
		// Get a list of all the supplied abstracts for processing
		File abstractsDir = new File(ABSTRACTSPATH);
		File[] abstractsArray = abstractsDir.listFiles();
		
		// Process each abstract individually
		for(int i=0; i<abstractsArray.length; i++)
		{	
			// Run the Stanford POS tagger on the abstract
			// Note: This does not currently return the info to here, but outputs to the System.out for testing purposes
			POSTaggerWrapper posTagger = new POSTaggerWrapper(abstractsArray[i]);
			
			// Initiliase for line-by-line processing
			BufferedReader br;
		    String line;
		    
		    try {
		    	br = new BufferedReader(new FileReader(abstractsArray[i]));
		    
		    	// Process the abstracts line-by-line
			    while ((line = br.readLine()) != null) {
			    	Reader r2 = new StringReader(line);
					List<Sentence> l = posTagger.tokenizeText(r2);
					for (int j = 0, sz = l.size(); j < sz; j++) {
					  Sentence s = l.get(j);
					  // Each sentence in each line in the abstract can now be parsed individually
					  
					  // Run the pattern finder on the sentence
					  // foundPatterns contains the String NP0 as key, and the String[] NPx as value(s) <-- NPx is an array!
					  PatternFinder myPatternFinder = new PatternFinder();
					  HashMap<String, String[]> foundPatterns = myPatternFinder.parseString(s.toString());
					  
					  // Put the data in the ontology
					  Iterator<String> it = foundPatterns.keySet().iterator();
					  String  key = null;
					  String [] values = null;
					  while(it.hasNext()){
						  key = it.next();
					  OntClass oclass = OntologyManager.get (null).addClass (key);
					  		values = foundPatterns.get (key);
					  		for (int g=0; values != null && g < values.length; g++){
					  			OntologyManager.get (null).addIndividual (oclass, values[g]);
					  		}
					  }
					  
					  
					  
					  // Test to see if the HashMap works
					  /**/
					  Collection collection = foundPatterns.entrySet();
					  Iterator iterator = collection.iterator();
					  
					  while(iterator.hasNext())
					  { 
					      Map.Entry entry = (Map.Entry)iterator.next();
					      String key2 = (String)entry.getKey();
					      String[] value = (String[])entry.getValue();
					      
					      // Display NP0 and NPx from foundPatterns (testing)
						  System.out.println("NP0: "+key2);
						  System.out.println("NPx: "+Arrays.asList(value));
					  }
					  /**/
					  
					  // Run the POS tagger on the sentence
					  // COMMENTED OUT TO TEST PATTERN FUNCTIONALITY!!!!
					  // System.out.println(posTagger.doTag(s) + " \r\n");
					  // String x = posTagger.doTag(s); <-- x = pos tagged version of sentence s
					 }
				}
		    }
			catch(IOException e) {
				System.out.println("ERROR: IOException"+e);
			}		
		}
		System.out.println ("The collected data: ");
		System.out.print (OntologyManager.get (null));
	}
}
