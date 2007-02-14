package src.nl.eur.eco_ict.seminar.ontlearning.kuovosvaasti;

import java.io.*;
import java.util.List;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSTaggerWrapper {
	// HOLDERFILE: location of the .holder file for the stanford tagger.
	//private static String HOLDERFILE = System.getProperty("user.dir")+"\\APIs\\postagger\\wsj3t0-18-bidirectional\\train-wsj-0-18.holder";
	private static String HOLDERFILE = "D:\\java\\libs\\Stanford POS tagger\\2006-05-21\\wsj3t0-18-bidirectional\\train-wsj-0-18.holder";
	MaxentTagger tagger;
	TokenizerFactory tokenizerFactory;
	
	public POSTaggerWrapper(File abstractFile) {
		System.out.println("POSTaggerWrapper: POS tagging file: "+abstractFile.getName());
		
		tagger = new MaxentTagger();
		
		try {
			tagger.init(HOLDERFILE);
		}
		catch(Exception e) {
			System.out.println("ERROR: "+e);
		}

	    BufferedReader br;
	    String line;
	    /*
	    try {
	    	br = new BufferedReader(new FileReader(abstractFile));
	    
		    while ((line = br.readLine()) != null) {
		    	// Change this to possible return these strings, instead of outputting them, so they can be processed.
		    	System.out.println(tagString(line));
				System.out.println();
			}
	    }
		catch(IOException e) {
			System.out.println("ERROR: IOException"+e);
		}*/
	}
	
	public List tokenizeText(Reader r2) {
		List l = tagger.tokenizeText(r2, tokenizerFactory);
		
		return l;
	}
	
	public String doTag(Sentence s) {
		return tagger.tagSentence(s).toString(false);
	}
	/*
	public String tagString(String myString) {
		String result = new String();
		Reader r2 = new StringReader(myString);
		List l = tagger.tokenizeText(r2, tokenizerFactory);
		for (int j = 0, sz = l.size(); j < sz; j++) {
		  Sentence s = (Sentence) l.get(j);
		  result += tagger.tagSentence(s).toString(false) + " \r\n";
		  // System.out.println(tagger.tagSentence(s).toString(false) + " ");
		}
		
		return result;
	}
*/
}
