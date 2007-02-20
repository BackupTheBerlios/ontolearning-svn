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
import java.util.List;

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
	
	public String getLeftNP(String myString) {
		String myResult = null;
		
		if((myString!=null) && (myString.length()>0)) {
			Tree myTree = getNP(getTree(myString), true);
	
			if(myTree!=null) {
				myResult = getStringRepresentation(myTree);
			}
		}
		
		return myResult;
	}
	
	public String getRightNP(String myString) {
		String myResult = null;		
		
		if((myString!=null) && (myString.length()>0)) {
			Tree myTree = getNP(getTree(myString), false);
	
			if(myTree!=null) {
				myResult = getStringRepresentation(myTree);
			}
		}
		
		return myResult;
	}
	
	public String getStringRepresentation(Tree myTree) {
		List<Tree> leafList = myTree.getLeaves();
		
		String myResult = "";
		
		for (int i = 0, mySize = leafList.size(); i < mySize; i++) {
			myResult = myResult + leafList.get(i).value() + " ";
		}
		
		return myResult.trim();
	}
	
	public Tree getTree(String myString) {
		this.myLexParser.parse(myString);
		Tree myTree = this.myLexParser.getBestParse();
		
		return myTree;
	}
	
	public Tree getNP(Tree myTree, boolean fromLeft) {
		/*System.out.println("=== START ===");
		System.out.println("Tree value: "+myTree.value()+" has label: "+myTree.label().toString());
		myTree.printLocalTree();
		myTree.pennPrint();
		System.out.println("=== END ===");
		System.out.println("");*/
		
		List<Tree> myNPList = myTree.getChildrenAsList();
		
		int end;
		int cur;
		int increment;
		Tree myResult = null;
		
		if(fromLeft) {
			cur = 0;
			end = myNPList.size();
			increment = 1;
		}
		else {
			cur = myNPList.size()-1;
			end = -1;
			increment = -1;			
		}
		
		while((myResult==null) && (cur!=end)) {
			Tree anNP =  myNPList.get(cur);
			if(anNP.numChildren()>0) {
				myResult = getNP(anNP, fromLeft);
			}		
			cur+=increment;
		}
		
		if((myTree.value().trim().compareTo("NP")==0) && (myResult==null)) {
			myResult = myTree;
		}
		
		return myResult;
	}
}
