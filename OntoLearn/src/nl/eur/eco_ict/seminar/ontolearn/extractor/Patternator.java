/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.extractor;

import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collection;

import com.hp.hpl.jena.ontology.OntClass;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology;
import nl.eur.eco_ict.seminar.ontolearn.util.PartOfSpeechTagger;
import nl.eur.eco_ict.seminar.ontolearn.util.impl.StanfordParser;
/**
 * @author Nico Vaatstra
 *
 * TODO: OO-ize this. Too much done in patternator methods:
 * - the parse() method.
 */
public class Patternator {
	private static String PATTERNSFILE =  System.getProperty("user.dir") + "/data/patterns/patterns.txt";
	private static String PATTERNSMERONYMSFILE =  System.getProperty("user.dir") + "/data/patterns/patternsMeronyms.txt";
    ArrayList<String> patterns;
    Collection<String> patternsMeronyms;
    StanfordParser myStanfordParser;
    
	public Patternator() {
		// Loading of the patterns from file etc. will start here.
		this.myStanfordParser = (StanfordParser)PartOfSpeechTagger.Factory.getStanfordLexer ();
		
	    BufferedReader br;
	    String line;

	    File patternsFile = new File(PATTERNSFILE);
	    File patternsMeronymsFile = new File(PATTERNSMERONYMSFILE);
	    
	    this.patterns = new ArrayList(100);
	    this.patternsMeronyms = new HashSet<String>();
	    
	    try {
	    	br = new BufferedReader(new FileReader(patternsFile));
		    
		    while ((line = br.readLine()) != null) {
		    	// load each pattern into the ArrayList
		    	this.patterns.add(line);
			}
		    
		    br = new BufferedReader(new FileReader(patternsMeronymsFile));
		    
		    while ((line = br.readLine()) != null) {
		    	// load each pattern into the ArrayList
		    	this.patternsMeronyms.add(line);
			}
	    }
		catch(IOException e) {
			System.out.println("ERROR: IOException"+e);
		}
	}
	
	public boolean isKnownPattern(String myString, String NP0, String NPx) {
		boolean isKnownPattern = false;
		
		String regNP0 = "([a-zA-Z0-9\\- ]+)";
		String regNPx = "(([a-zA-Z0-9\\- ]+)(, [a-zA-Z0-9\\- ]+)*( and [a-zA-Z0-9\\- ]*)*)";
		String regSpacer = "((, {1,}+)|( , {1,}+)|( {1,}+))";
		
		for ( int i=0; i<this.patterns.size (); i++ ) {
			String pattern = this.patterns.get(i);

			pattern = pattern.replaceAll("NP0", regNP0);
			pattern = pattern.replaceAll("NPx", regNPx);
			pattern = pattern.replaceAll(":connector:", regSpacer);
			
			Pattern p = Pattern.compile(pattern);
			
			// Create Matcher:
			Matcher m = p.matcher(myString);
		
			// Loop the matcher for matches
			if(m.find()) {
				isKnownPattern = true;
			}
			
		}
		
		return isKnownPattern;
	}
	
	public void addPattern(String newPattern) {
		// Add pattern to currently loaded patterns:
		this.patterns.add(newPattern);
		
		// Add pattern to the patterns.txt file:
		try {
			File f = new File(PATTERNSFILE);
	        
	        if(f.exists())
	        {
	            long fileLength = f.length();
	            RandomAccessFile raf = new RandomAccessFile(f, "rw");
	            raf.seek(fileLength);
	            
	            raf.writeBytes("\r\n"+newPattern);
	            
	            raf.close(); 
	        }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Patternator: Added pattern: "+newPattern);
	}
	
	public void identifyUnknownPattern(String myString, String NP0, String NPx) {
		String newPattern = null;
		
		if(myString.indexOf(NP0) < myString.indexOf(NPx)) {
			// Pattern: NP0 --> pattern word (like, such as, etc..) --> NPx
			newPattern = myString.substring(myString.indexOf(NP0) + NP0.length(), myString.indexOf(NPx)).trim().replaceAll(", ","");

			System.out.println("newPattern.split.length ding: "+newPattern.split(" ").length);
			if(newPattern.split(" ").length<3) {
				newPattern = "NP0:connector:("+newPattern+"):connector:NPx";
				addPattern(newPattern);
			}
			
			
		}
		else {
			// Pattern: NPX --> pattern word (like, such as, etc..) --> NP0
			newPattern = myString.substring(myString.indexOf(NPx) + NPx.length(), myString.indexOf(NP0)).trim().replaceAll(", ","");

			System.out.println("newPattern.split.length ding: "+newPattern.split(" ").length);
			if(newPattern.split(" ").length<3) {
				newPattern = "NPx:connector:("+newPattern+"):connector:NP0";
				addPattern(newPattern);
			}
		}
		
	}
	
	public void stripNewPattern(String myString, String className, String subClassName) {
		if(!isKnownPattern(myString, className, subClassName)) {
			System.out.println("className: "+className+" --> subClass: "+subClassName);
			System.out.println("UNKNOWN PATTERN: "+myString);
			identifyUnknownPattern(myString, className, subClassName);
			System.out.println();
		}
	}
	
	public void scanNewPatterns(String myString, Ontology myOntology) {
		Iterator<OntClass> myClasses = myOntology.getClasses ();
		Iterator<OntClass> mySubClasses = null;
		OntClass tempClass = null;
		OntClass tempSubClass = null;
		String className = null;
		String subClassName = null;
		
		if(myClasses != null) {
			while(myClasses.hasNext ()) {
				tempClass = myClasses.next ();
				
				// TODO: Dit meot natuurlijk niet zo :P 
				// .getLocalName() werkt niet :( Die pakt alleen de tekst na de laatste spatie.
				className = tempClass.getURI().replaceAll("http://someplace.somewhere/someontology/", "");
				
				if((myString.contains(className)) && (tempClass != null) && (tempClass.hasSubClass())) {
					mySubClasses = tempClass.listSubClasses();	
					
					while(mySubClasses.hasNext ()) {
						tempSubClass = mySubClasses.next ();
						subClassName = tempSubClass.getURI().replaceAll("http://someplace.somewhere/someontology/", "");
						
						if(myString.contains(subClassName)) {
							stripNewPattern(myString, className, subClassName);
						}
					}
				}
			}
		}
	}
	
	public HashMap parseString(String myString) {
		HashMap foundPairs = new HashMap();
		
		String regNP0 = "([a-zA-Z0-9\\- ]+)";
		String regNPx = "(([a-zA-Z0-9\\- ]+)(, [a-zA-Z0-9\\- ]+)*( and [a-zA-Z0-9\\- ]*)*)";
		String regSpacer = "((, {1,}+)|( {1,}+))";		
		
		for ( int i=0; i<this.patterns.size (); i++ ) {
			String pattern = this.patterns.get(i);

			pattern = pattern.replaceAll("NP0", regNP0);
			pattern = pattern.replaceAll("NPx", regNPx);
			pattern = pattern.replaceAll(":connector:", regSpacer);
			
			Pattern p = Pattern.compile(pattern);
			
			// Create Matcher:
			Matcher m = p.matcher(myString);
		
			// Loop the matcher for matches
			while (m.find()) {
				// Grab the string satisfying the pattern
				String myMatch = myString.substring(m.start(), m.end());
				
				// Parse the string to find NP0 and NPx according to the pattern
				// Split the string around the ( and ) tags:
				
				String regBrackets = "((\\x28{1})([a-zA-Z0-9 ]+)+(\\x29{1}))";
				
				String usedPattern = this.patterns.get(i);	
				
				Pattern p2 = Pattern.compile(regBrackets);
				Matcher m2 = p2.matcher(usedPattern);
				
				while (m2.find()) {
					String splitString = usedPattern.substring(m2.start()+1, m2.end()-1);
					myMatch = myMatch.replaceAll(splitString, ":splithere:");
				}				
				
				String[] splitMatches = myMatch.split(":splithere:");
								
				// Clean the splitted array
				String[] cleanMatches = cleanArray(splitMatches);

				// Populate NP0 and NPx from cleanMatches
				String NP0 = new String();
				String NPx = new String();
				
				if(usedPattern.indexOf("NP0") < usedPattern.indexOf("NPx")) {
					NP0 = cleanMatches[0];
					NPx = cleanMatches[1];
				}
				else {
					NP0 = cleanMatches[1];
					NPx = cleanMatches[0];
				}
				
				// NPx can contain NP1, NP2, etc.. Split them and clean the array
				String regSplitNPx = "(( , {1})|( and )|( or ))";

				String NPxnew = NPx.replaceAll(regSplitNPx, ":splithere:");
				
				String[] cleanNPx = NPxnew.split(":splithere:");
				
				if(usedPattern.indexOf("NP0") < usedPattern.indexOf("NPx")) {
					NP0 = this.myStanfordParser.getRightNP(NP0);
					for(int j=0;j<cleanNPx.length; j++) {
						cleanNPx[j] = this.myStanfordParser.getLeftNP(cleanNPx[j]);
					}
				}
				else {
					NP0 = this.myStanfordParser.getLeftNP(NP0);
					for(int j=0;j<cleanNPx.length; j++) {
						cleanNPx[j] = this.myStanfordParser.getRightNP(cleanNPx[j]);
					}
				}
				
				if(NP0 != null) {
					NP0 = NP0.replaceAll("(\\x2D{1})([a-zA-Z0-9]+)(\\x2D{1})","");
				}
				
				for(int j=0;j<cleanNPx.length; j++) {
					if(cleanNPx[j]!=null) {
						cleanNPx[j] = cleanNPx[j].replaceAll("(\\x2D{1})([a-zA-Z0-9]+)(\\x2D{1})","");
					}
				}
				
				foundPairs.put(NP0, cleanNPx);
				
				// Display NP0 and NPx (testing)
				System.out.println("NP0: "+NP0);
				System.out.println("NPx: "+Arrays.asList(cleanNPx));
			}
		}
		
		return foundPairs;		
	}
	
	public HashMap parseString(String myString, String type) {
		HashMap<String, String[]> foundPairs = new HashMap<String, String[]>();
		
		Collection<String> myPatterns = new HashSet<String>();
		
		if(type.compareTo("hyponym")==0) {
			// Look for hyponyms:
			myPatterns = this.patterns;
		}
		else if(type.compareTo("meronym")==0) {
			// Look for meronyms:
			myPatterns = this.patternsMeronyms;
		}
		
		String regNP0 = "([a-zA-Z0-9\\- ]+)";
		String regNPx = "(([a-zA-Z0-9\\- ]+)(, [a-zA-Z0-9\\- ]+)*( and [a-zA-Z0-9\\- ]*)*)";
		String regSpacer = "((, {1,}+)|( {1,}+))";		
		
		Iterator<String> i = myPatterns.iterator ();
		
		while (i.hasNext()){
			String pattern = i.next();
			String patternCopy = new String(pattern);
			
			pattern = pattern.replaceAll("NP0", regNP0);
			pattern = pattern.replaceAll("NPx", regNPx);
			pattern = pattern.replaceAll(":connector:", regSpacer);
			
			Pattern p = Pattern.compile(pattern);
			
			// Create Matcher:
			Matcher m = p.matcher(myString);
		
			// Loop the matcher for matches
			while (m.find()) {
				// Grab the string satisfying the pattern
				String myMatch = myString.substring(m.start(), m.end());
				
				// Parse the string to find NP0 and NPx according to the pattern
				// Split the string around the ( and ) tags:
				
				String regBrackets = "((\\x28{1})([a-zA-Z0-9 ]+)+(\\x29{1}))";
				
				String usedPattern = patternCopy;	
				
				Pattern p2 = Pattern.compile(regBrackets);
				Matcher m2 = p2.matcher(usedPattern);
				
				while (m2.find()) {
					String splitString = usedPattern.substring(m2.start()+1, m2.end()-1);
					myMatch = myMatch.replaceAll(splitString, ":splithere:");
				}				
				
				String[] splitMatches = myMatch.split(":splithere:");
								
				// Clean the splitted array
				String[] cleanMatches = cleanArray(splitMatches);

				// Populate NP0 and NPx from cleanMatches
				String NP0 = new String();
				String NPx = new String();
				
				if(usedPattern.indexOf("NP0") < usedPattern.indexOf("NPx")) {
					NP0 = cleanMatches[0];
					NPx = cleanMatches[1];
				}
				else {
					NP0 = cleanMatches[1];
					NPx = cleanMatches[0];
				}
				
				// NPx can contain NP1, NP2, etc.. Split them and clean the array
				String regSplitNPx = "(( , {1})|( and )|( or ))";

				String NPxnew = NPx.replaceAll(regSplitNPx, ":splithere:");
				
				String[] cleanNPx = NPxnew.split(":splithere:");
				
				if(usedPattern.indexOf("NP0") < usedPattern.indexOf("NPx")) {
					NP0 = this.myStanfordParser.getRightNP(NP0);
					for(int j=0;j<cleanNPx.length; j++) {
						cleanNPx[j] = this.myStanfordParser.getLeftNP(cleanNPx[j]);
					}
				}
				else {
					NP0 = this.myStanfordParser.getLeftNP(NP0);
					for(int j=0;j<cleanNPx.length; j++) {
						cleanNPx[j] = this.myStanfordParser.getRightNP(cleanNPx[j]);
					}
				}
				
				if(NP0 != null) {
					NP0 = NP0.replaceAll("(\\x2D{1})([a-zA-Z0-9]+)(\\x2D{1})","");
				}
				
				for(int j=0;j<cleanNPx.length; j++) {
					if(cleanNPx[j]!=null) {
						cleanNPx[j] = cleanNPx[j].replaceAll("(\\x2D{1})([a-zA-Z0-9]+)(\\x2D{1})","");
					}
				}
				
				foundPairs.put(NP0, cleanNPx);
				
				// Display NP0 and NPx (testing)
				// System.out.println("NP0: "+NP0);
				// System.out.println("NPx: "+Arrays.asList(cleanNPx));
			}
		}
		
		return foundPairs;		
	}
	
	public String[] cleanArray(String[] roughMatches) {
		String[] cleanMatches = new String[2];
		cleanMatches[0] = "";
		cleanMatches[1] = "";
		
		String regValidMatch = "([a-zA-Z0-9 ]+)+";
		Pattern regPattern = Pattern.compile(regValidMatch);
		
		// int value used to populate cleanMatches[]
		int x = 0;
		
		for(int i=0;i < roughMatches.length; i++) {
			Matcher roughMatcher = regPattern.matcher(roughMatches[i]);
			
			if(roughMatcher.find()) {
				cleanMatches[x] = roughMatches[i].trim();
				x++;
			}
		}
		
		return cleanMatches;
	}
}
