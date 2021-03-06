package src.nl.eur.eco_ict.seminar.ontlearning.kuovosvaasti;

import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PatternFinder {
	private static String PATTERNSFILE = System.getProperty("user.dir")+"\\data\\patterns\\patterns.txt";
    ArrayList patterns;
    
	public PatternFinder() {
		// Loading of the patterns from file etc. will start here.
		
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
		
		String regNP0 = "([a-zA-Z0-9 ]+)";
		String regNPx = "(([a-zA-Z0-9 ]+)(, [a-zA-Z0-9 ]+)*( and [a-zA-Z0-9 ]*)*)";
		String regSpacer = "((, {1})|( {1}))";		
		
		for ( int i=0; i<=5; i++ ) {
			String pattern = (String)patterns.get(i);

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
				String regSplitNPx = "(( , {1})|( and ))";
				NPx = NPx.replaceAll(regSplitNPx, ":splithere:");
				
				String[] cleanNPx = NPx.split(":splithere:");
				
				for(int j=0;j<cleanNPx.length; j++) {
					cleanNPx[j] = cleanNPx[j].trim();
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
		
		String regValidMatch = "([a-zA-Z0-9 ]+)+";
		Pattern regPattern = Pattern.compile(regValidMatch);
		
		// int value used to populate cleanMatches[]
		int x = 0;
		
		for(int i=0;i<roughMatches.length; i++) {
			Matcher roughMatcher = regPattern.matcher(roughMatches[i]);
			
			if(roughMatcher.find()) {
				cleanMatches[x] = roughMatches[i].trim();
				x++;
			}
		}
		
		return cleanMatches;
	}
}
