package nl.eur.eco_ict.seminar.ontolearn.testzone;

import java.sql.SQLException;

import nl.eur.eco_ict.seminar.ontolearn.association.AssociationDatabase;

public class AssociationsResult{
	AssociationDatabase waardeDB = new AssociationDatabase ();
	public static void main (String args[] ) throws SQLException{
		AssociationsResult associationresult = new AssociationsResult();
		associationresult.deleteUselessResults ();
	}
	public void AssocationResult() throws SQLException {
		this.deleteUselessResults();	 
	}
	public void deleteUselessResults() throws SQLException {
		this.waardeDB.deleteLessUsedWords ();
	}
}