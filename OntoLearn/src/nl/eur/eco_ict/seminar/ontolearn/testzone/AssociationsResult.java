/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.testzone;

import java.sql.SQLException;

import nl.eur.eco_ict.seminar.ontolearn.association.AssociationDatabase;
/**
 * @author remy
 *
 */
public class AssociationsResult {
	AssociationDatabase waardeDB = new AssociationDatabase ();
	
	public AssociationsResult() throws SQLException {
		this.getDatabaseInfo();
	}
	/**
	 * @param args
	 * @throws SQLException 
	 */
	public void main (String[] args) throws SQLException {
	// TODO Auto-generated method stub
		this.getDatabaseInfo();
	}
	public void getDatabaseInfo() throws SQLException {
		this.waardeDB.deleteLessUsedWords ();
	}
}
