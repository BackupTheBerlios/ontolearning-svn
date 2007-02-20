/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.util.impl;

import java.io.File;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

/**
 * @author Nico
 *
 */
public class StanfordParser {
	LexicalizedParser myLexParser;
	
	public StanfordParser() {
		String lexParserFile = System.getProperty("user.dir") + File.separatorChar + "data"+File.separatorChar+"stanford"+File.separatorChar+"stanford-parser"+File.separatorChar+"englishPCFG.ser.gz";
		this.myLexParser = new LexicalizedParser(lexParserFile);		
	}
	/*
	public String lexicalize() {
		if(this.myLexParser.parse(key)) {
			Tree myTree = this.myLexParser.getBestParse();
			myTree.printLocalTree();
			myTree.pennPrint();
			System.out.println("Tree: "+myTree.toString());
		  }
		  else {
			  System.out.println("LexParser: niet true :(");
		  }
	}*/
}
