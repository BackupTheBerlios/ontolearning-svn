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
 * @author Administrator
 *
 */
public class CorrOcc {
	public String document;
	public int xCount;
	public int yCount;
	
	public CorrOcc() {
		this.xCount = 0;
		this.yCount = 0;
		this.document = null;
	}
	
	public CorrOcc(int xCount, int yCount) {
		this.xCount = xCount;
		this.yCount = yCount;
	}
	
	public void setXCount(int xCount) {
		this.xCount = xCount;
	}
	
	public void setYCount(int yCount) {
		this.yCount = yCount;
	}
	
	public void setDocument(String document) {
		this.document = document;
	}
	
	public String getDocument() {
		return this.document;
	}
	
	public int getXCount() {
		return this.xCount;
	}
	
	public int getYCount() {
		return this.yCount;
	}
}
