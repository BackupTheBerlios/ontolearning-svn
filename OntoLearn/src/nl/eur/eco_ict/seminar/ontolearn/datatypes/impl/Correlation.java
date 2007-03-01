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
public class Correlation {
	double supportAtoB;
	double supportBtoA;
	double confidenceAtoB;
	double confidenceBtoA;
	double liftAtoB;
	double liftBtoA;
	
	public Correlation() {
	}

	/**
	 * @return the confidenceAtoB
	 */
	public double getConfidenceAtoB () {
		return this.confidenceAtoB;
	}

	/**
	 * @param confidenceAtoB the confidenceAtoB to set
	 */
	public void setConfidenceAtoB (double confidenceAtoB) {
		this.confidenceAtoB = confidenceAtoB;
	}

	/**
	 * @return the confidenceBtoA
	 */
	public double getConfidenceBtoA () {
		return this.confidenceBtoA;
	}

	/**
	 * @param confidenceBtoA the confidenceBtoA to set
	 */
	public void setConfidenceBtoA (double confidenceBtoA) {
		this.confidenceBtoA = confidenceBtoA;
	}

	/**
	 * @return the liftAtoB
	 */
	public double getLiftAtoB () {
		return this.liftAtoB;
	}

	/**
	 * @param liftAtoB the liftAtoB to set
	 */
	public void setLiftAtoB (double liftAtoB) {
		this.liftAtoB = liftAtoB;
	}

	/**
	 * @return the liftBtoA
	 */
	public double getLiftBtoA () {
		return this.liftBtoA;
	}

	/**
	 * @param liftBtoA the liftBtoA to set
	 */
	public void setLiftBtoA (double liftBtoA) {
		this.liftBtoA = liftBtoA;
	}

	/**
	 * @return the supportAtoB
	 */
	public double getSupportAtoB () {
		return this.supportAtoB;
	}

	/**
	 * @param supportAtoB the supportAtoB to set
	 */
	public void setSupportAtoB (double supportAtoB) {
		this.supportAtoB = supportAtoB;
	}

	/**
	 * @return the supportBtoA
	 */
	public double getSupportBtoA () {
		return this.supportBtoA;
	}

	/**
	 * @param supportBtoA the supportBtoA to set
	 */
	public void setSupportBtoA (double supportBtoA) {
		this.supportBtoA = supportBtoA;
	}		
}
