/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.datatypes.impl;

/**
 * @author 300353jv
 *
 */
public class Settings {
	protected String documentroot = null;
	protected String outputloc = null;
	
	public void setDocumentroot (String s){
		this.documentroot = s;
	}
	
	public String getDocumentroot (){
		return this.documentroot;
	}
	
	public void setOutputLocation (String s){
		this.outputloc = s;
	}
	
	public String getOutputLocation(){
		return this.outputloc;
	}
}
