/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.testzone;

import nl.eur.eco_ict.seminar.ontolearn.util.Stemmer;

/**
 * @author Mr H
 *
 */
public class TestStemmer {
	
	public static  void main (String[] args){
		System.out.println ("Testing stemmer:");
		
		String word = "jumping";
		System.out.println (word + "-->" + Stemmer.Factory.getInstance().stem (word));
		word = "birds";
		System.out.println (word + "-->" + Stemmer.Factory.getInstance().stem (word));
		word = "econometry";
		System.out.println (word + "-->" + Stemmer.Factory.getInstance().stem (word));
		word = "economic";
		System.out.println (word + "-->" + Stemmer.Factory.getInstance().stem (word));
		word = "economies";
		System.out.println (word + "-->" + Stemmer.Factory.getInstance().stem (word));
		word = "went";
		System.out.println (word + "-->" + Stemmer.Factory.getInstance().stem (word));
		word = "were";
		System.out.println (word + "-->" + Stemmer.Factory.getInstance().stem (word));
		word = "bought";
		System.out.println (word + "-->" + Stemmer.Factory.getInstance().stem (word));
	}
}
