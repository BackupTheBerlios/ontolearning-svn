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
	
	public CorrOcc(int xcount, int ycount) {
		this.xCount = xcount;
		this.yCount = ycount;
	}
	
	public void setXCount(int xcount) {
		this.xCount = xcount;
	}
	
	public void setYCount(int ycount) {
		this.yCount = ycount;
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

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode () {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.document == null) ? 0 : this.document.hashCode ());
		result = PRIME * result + this.xCount;
		result = PRIME * result + this.yCount;
		return result;
	}
}
