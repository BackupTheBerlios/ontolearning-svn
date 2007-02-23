package nl.eur.eco_ict.seminar.ontolearn.testzone;

import nl.eur.eco_ict.seminar.ontolearn.association.AssociationDatabase;

public class AssociationsResult{
	AssociationDatabase waardeDB = new AssociationDatabase ();
	public static void main (String args[] ){
		deleteUselessResults();	
	}
	public void AssocationResult() {
		deleteUselessResults();	 
	}
	public static void deleteUselessResults() {
		this.waardeDB.deleteUselessWords ();
	}
}