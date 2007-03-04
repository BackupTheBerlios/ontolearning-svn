/**
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.extractor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.io.*;

import com.hp.hpl.jena.ontology.OntClass;
// Not used, removable?
// import com.hp.hpl.jena.ontology.OntProperty;

import nl.eur.eco_ict.seminar.ontolearn.Extractor;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.Document;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.impl.CorrOcc;
import nl.eur.eco_ict.seminar.ontolearn.util.Tokenizer;

/**
 * @author Nico
 *
 */
public class HearstExtractor implements Extractor {
	Patternator myPatternator;
	private static String COMMONWORDSFILE =  System.getProperty("user.dir") + "/data/patterns/common_words.txt";
    Collection<String> commonWords;
	
	public HearstExtractor() {
		this.myPatternator = new Patternator();
		loadCommonWords();
	}
	
	public void loadCommonWords() {
		BufferedReader br;
	    String line;
	    
	    this.commonWords = new HashSet<String>();
	    
	    File commonWordsFile = new File(COMMONWORDSFILE);
	    
	    try {
	    	br = new BufferedReader(new FileReader(commonWordsFile));
	    
		    while ((line = br.readLine()) != null) {
		    	// load each pattern into the ArrayList
		    	this.commonWords.add(line.toLowerCase());
			}
	    }
		catch(IOException e) {
			System.out.println("ERROR: IOException"+e);
		}
	}
	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Extractor#parse(nl.eur.eco_ict.seminar.ontolearn.datatypes.Document, nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology)
	 */
	public void parse (Document doc, Ontology ontology) throws Throwable {
		Iterator<BufferedReader> abstracts = doc.readAbstracts ().iterator ();
		while(abstracts.hasNext()){
			this.parse (abstracts.next(), ontology);
		}
	}
	
	
	protected void parse (BufferedReader reader, Ontology ontology) throws Throwable {
		try {
			List<String> l = Tokenizer.Factory.getInstance().toSentences(reader);
			
			for (int j = 0, sz = l.size(); j < sz; j++) {
				  String s =  l.get(j);
				  // Each sentence in each line in the abstract can now be parsed individually
				  
				  // Run the pattern finder on the sentence
				  // foundPatterns contains the String NP0 as key, and the String[] NPx as value(s) <-- NPx is an array!
				  
				  HashMap<String, String[]> foundPatterns = this.myPatternator.parseString(s);
				  
				  // Test to see if the HashMap works
				  
				  Collection<Entry<String, String[]>> collection = foundPatterns.entrySet();
				  Iterator<Entry<String, String[]>> iterator = collection.iterator();
				  
				  Entry<String, String[]> entry = null;
				 
				  while(iterator.hasNext())
				  { 
				      entry = iterator.next();
				      String key = entry.getKey();
				      String[] value = entry.getValue();
				      
				      if(checkNP(key)) {
				    	  System.out.println("Adding the following key: "+key);
					      
					      OntClass myKey = ontology.addOClass (key);
					      
					      for(int i = 0; i < value.length; i++) {
					    	  if(checkNP(value[i])) {
						    	  OntClass myValue = ontology.addOClass (value[i]);						
								  myValue.setSuperClass (myKey);
					    	  }
					      }
				      }
				      
				      // Display NP0 and NPx from foundPatterns (testing)
				      /*
					  System.out.println("NP0: "+key);
					  System.out.println("NPx: "+Arrays.asList(value));
					  */
					  
				  }
				  
				  this.myPatternator.scanNewPatterns(s, ontology);
			}
		}
		catch (Exception e) {
			System.out.println("Error: "+e);
			for (int i = 0; i < 4 && i <e.getStackTrace ().length;i++){
				System.err.println (e.getStackTrace ()[i]);
			}
		}
	}
	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Extractor#getName()
	 */
	public String getName () {
		return "Hearst extractor";
	}
	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.Extractor#onFinish(nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology)
	 */
	public void onFinish (Ontology ontology) {}
	
	public boolean checkNP (String myNP) {
		boolean correctNP = true;
		
		if((myNP==null) || (myNP.compareTo("null")==0) || (myNP.compareTo("")==0)) {
			System.out.println("null NP found!: "+myNP);
			return false;
		}
		
		// Check de commonWords Collection<String> voor matches. Een match betekend wrong NP
		Iterator<String> i = this.commonWords.iterator ();
		
		while (i.hasNext()){
			if(i.next().compareToIgnoreCase(myNP)==0) {
				System.out.println("common-words NP found!: "+myNP);
				return false;
			}
		}
		
		return true;
	}

}
