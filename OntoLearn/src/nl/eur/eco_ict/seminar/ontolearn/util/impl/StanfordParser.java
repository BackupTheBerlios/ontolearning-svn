/**
 * OntoLearn a seminar project of: - Remy Stibbe - Hesing Kuo - Nico Vaatstra -
 * Jasper Voskuilen
 */
package nl.eur.eco_ict.seminar.ontolearn.util.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.eur.eco_ict.seminar.ontolearn.util.PartOfSpeechTagger;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

/**
 * @author Nico
 */
public class StanfordParser implements PartOfSpeechTagger {
	protected LexicalizedParser myLexParser;

	public StanfordParser () {
		String lexParserFile = System.getProperty ("user.dir")
				+ File.separatorChar + "data" + File.separatorChar + "stanford"
				+ File.separatorChar + "stanford-parser" + File.separatorChar
				+ "englishPCFG.ser.gz";
		this.myLexParser = new LexicalizedParser (lexParserFile);

		this.myLexParser.parse ("Restrictions");
		Tree myTree = this.myLexParser.getBestParse ();
	}

	public String getLeftNP (String myString) {
		String myResult = null;

		if ((myString != null) && (myString.length () > 0)) {
			Tree myTree = getNP (getTree (myString), true);

			if (myTree != null) {
				myResult = getStringRepresentation (myTree);
			}
		}

		return myResult;
	}

	public String getRightNP (String myString) {
		String myResult = null;

		if ((myString != null) && (myString.length () > 0)) {
			Tree myTree = getNP (getTree (myString), false);

			if (myTree != null) {
				myResult = getStringRepresentation (myTree);
			}
		}

		return myResult;
	}

	public String getStringRepresentation (Tree myTree) {
		List<Tree> leafList = myTree.getLeaves ();

		String myResult = "";

		for (int i = 0, mySize = leafList.size (); i < mySize; i++) {
			myResult = myResult + leafList.get (i).value () + " ";
		}

		return myResult.trim ();
	}

	public Tree getTree (String myString) {
		this.myLexParser.parse (myString);
		Tree myTree = this.myLexParser.getBestParse ();

		return myTree;
	}

	public Tree getNP (Tree myTree, boolean fromLeft) {
		/*
		 * System.out.println("=== START ==="); System.out.println("Tree value:
		 * "+myTree.value()+" has label: "+myTree.label().toString());
		 * myTree.printLocalTree(); myTree.pennPrint(); System.out.println("===
		 * END ==="); System.out.println("");
		 */

		List<Tree> myNPList = myTree.getChildrenAsList ();

		int end;
		int cur;
		int increment;
		Tree myResult = null;

		if (fromLeft) {
			cur = 0;
			end = myNPList.size ();
			increment = 1;
		} else {
			cur = myNPList.size () - 1;
			end = -1;
			increment = -1;
		}

		while ((myResult == null) && (cur != end)) {
			Tree anNP = myNPList.get (cur);
			if (anNP.numChildren () > 0) {
				myResult = getNP (anNP, fromLeft);
			}
			cur += increment;
		}

		if ((myTree.value ().trim ().compareTo ("NP") == 0)
				&& (myResult == null)) {
			myResult = myTree;
		}

		return myResult;
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.util.PartOfSpeechTagger#tag(java.lang.String)
	 */
	public Map<String, String> tag (String sentence) {
		Tree tree = this.getTree (sentence);
		Iterator<Tree> i = tree.iterator ();
		Map<String, String> result = new HashMap<String, String> ();
		while (i.hasNext ()) {
			tree = i.next ();
			if (tree.isPreTerminal ()){
				result.put (tree.getChild (0).nodeString (), tree.value ());
			}
		}
		return result;
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.util.PartOfSpeechTagger#tagInternal(java.lang.String)
	 */
	public String tagInternal (String sentence) throws Exception {
		Tree tree = this.getTree (sentence);
		String s = this.tagTree (tree, false);
		return s;
	}

	/**
	 * A recursive method wich takes a tree and produces a tagged string
	 * @param t the tree to be tagged
	 * @param tagphrases
	 * @return A string representing the tree with words followed by tags separated by '/' e.g. in/IN and optionally including tagged subsentences e.g. (in/IN house/NN)/NP. The subsentence consists of the words between '()'
	 */
	protected String tagTree (Tree t, boolean tagphrases) {
		StringBuffer buffer = new StringBuffer ();
		if (t.isLeaf () || t.isPreTerminal ()) {
			if (t.isLeaf ()) {
				buffer.append (t.nodeString ());
			}
			if (t.isPreTerminal ()) {
				buffer.append (t.getChild (0).nodeString ());
			}
			buffer.append ("/");
			buffer.append (t.value ());
			buffer.append (" ");
		} else {
			Iterator<Tree> children = t.getChildrenAsList ().listIterator ();
			if (tagphrases) {
				buffer.append ("(");
			}
			while (children.hasNext ()) {
				buffer.append (this.tagTree (children.next (), tagphrases));
			}
			if (tagphrases) {
				buffer.deleteCharAt (buffer.length () - 1);
				buffer.append (")/");
				buffer.append (t.value ());
				buffer.append (" ");
			}
		}
		return buffer.toString ();
	}
}
