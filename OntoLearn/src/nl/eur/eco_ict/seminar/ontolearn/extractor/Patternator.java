/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.extractor;

import java.util.Arrays;
import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.HashMap;

import nl.eur.eco_ict.seminar.ontolearn.util.impl.StanfordParser;
/**
 * @author Nico Vaatstra
 *
 * TODO: OO-ize this. Too much done in patternator methods.
 */
public class Patternator {
	private static String PATTERNSFILE =  System.getProperty("user.dir") + "/data/patterns/patterns.txt";
    ArrayList patterns;
    StanfordParser myStanfordParser;
    
	public Patternator() {
		// Loading of the patterns from file etc. will start here.
		this.myStanfordParser = new StanfordParser();
		
	    BufferedReader br;
	    String line;
	    
	    File patternsFile = new File(PATTERNSFILE);
	    
	    patterns = new ArrayList(100);
	    
	    try {
	    	br = new BufferedReader(new FileReader(patternsFile));
	    
		    while ((line = br.readLine()) != null) {
		    	// load each pattern into the ArrayList
		    	patterns.add(line);
			}
	    }
		catch(IOException e) {
			System.out.println("ERROR: IOException"+e);
		}
	}
	
	public HashMap parseString(String myString) {
		HashMap foundPairs = new HashMap();
		
		String regNP0 = "([a-zA-Z0-9\\- ]+)";
		String regNPx = "(([a-zA-Z0-9\\- ]+)(, [a-zA-Z0-9\\- ]+)*( and [a-zA-Z0-9\\- ]*)*)";
		String regSpacer = "((, {1,}+)|( {1,}+))";		
		
		for ( int i=0; i<this.patterns.size (); i++ ) {
			String pattern = (String)this.patterns.get(i);

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
				
				String usedPattern = (String)patterns.get(i);	
				
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
	
	public String[] cleanArray(String[] roughMatches) {
		String[] cleanMatches = new String[2];
		cleanMatches[0] = "";
		cleanMatches[1] = "";
		
		String regValidMatch = "([a-zA-Z0-9 ]+)+";
		Pattern regPattern = Pattern.compile(regValidMatch);
		
		// int value used to populate cleanMatches[]
		int x = 0;
		
		for(int i=0;i<2; i++) {
			Matcher roughMatcher = regPattern.matcher(roughMatches[i]);
			
			if(roughMatcher.find()) {
				cleanMatches[x] = roughMatches[i].trim();
				x++;
			}
		}
		
		return cleanMatches;
	}
}
