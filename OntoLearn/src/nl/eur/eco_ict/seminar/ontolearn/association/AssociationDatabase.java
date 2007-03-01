/**
 * OntoLearn a seminar project of:
 * - Remy Stibbe
 * - Hesing Kuo
 * - Nico Vaatstra
 * - Jasper Voskuilen
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.association;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.impl.CorrOcc;
/**
 * @author remy
 *
 */
public class AssociationDatabase {

	Connection con;
	String url;
	Statement stmt;
	
	protected Collection <Occurance> occuranceMatrix = new HashSet<Occurance>();
	
	public AssociationDatabase() {
		try {
			//Register the JDBC driver for MySQL.
			Class.forName("com.mysql.jdbc.Driver");

			//Define URL of database server for
			// database named JunkDB on the localhost
			// with the default port number 3306.
			this.url = "jdbc:mysql://localhost:3306/ontolearn";

			//Get a connection to the database for a
			// user named auser with the password
			// drowssap, which is password spelled
			// backwards.
			this.con = DriverManager.getConnection(this.url,"ontolearn", "ontolearn");

			//Display URL and connection information
			// System.out.println("URL: " + this.url);
			// System.out.println("Connection: " + this.con);
			
			//Get a Statement object
			this.stmt = this.con.createStatement();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}

	}
	public void addConcept(String document, String word, Integer wordCount) throws SQLException {
		// System.out.println ("INSERT INTO `association_abstract` (`document`, `word`, `wordcount`) VALUES('" + document + "','" + word + "', '" + wordCount + "')");
		this.stmt.executeUpdate("INSERT INTO `association_abstract` (`document`, `word`, `wordcount`) VALUES('" + document + "','" + word + "', '" + wordCount + "')");
	}
	public void updateConcept(String document, String word, Integer wordCount) throws SQLException {
		// System.out.println ("UPDATE `association_abstract` SET `wordcount` = '" + wordCount + "' WHERE `document` = '" + document + "' and `word` = '" + word + "'");
		this.stmt.executeUpdate("UPDATE `association_abstract` SET `wordcount` = '" + wordCount + "' WHERE `document` = '" + document + "' and `word` = '" + word + "'");
	}
	public void deleteLessUsedWords() throws SQLException {
		// get distinct information per word
		ResultSet rs;
		String [] iWord = null;
		int i = 0;
		
		rs = this.stmt.executeQuery("SELECT `word`, COUNT(`wordcount`) as `wordcount` FROM `association_abstract` GROUP BY `word`");
		System.out.println(rs); 
		System.out.println("Display all results:");
		while(rs.next()){
			if (rs.getInt("wordcount") < 2) {
				i = i + 1;
			}
		}
		System.out.println("Number of grouped words:" + i);
		iWord = new String [i];
		int j = 0;
		rs.first ();
		while(rs.next()) {
			if (rs.getInt("wordcount") < 2) {
				iWord[j] = rs.getString("word");
				j = j + 1;
			}
		}
		for (int k = 0; k < iWord.length ; k++) {
			System.out.println(iWord[k]);
			this.deleteAllKindOfWord (iWord[k]);
		}
	}
	public String[] getSignificantWordsPerDocument() throws SQLException {
		String[] documentString = null;
		ResultSet rs;
	
		
		int i = 0;
		rs = this.stmt.executeQuery("SELECT DISTINCT `document` FROM `association_abstract` ");
		while(rs.next()) {
			i = i + 1;
		}
		documentString = new String [i];
		int j = 0;
		rs.first ();
		while(rs.next()) {
				documentString[j] = rs.getString("document");
				j = j + 1;
		}
		for (int k = 0; k < documentString.length ; k++) {
			System.out.println(documentString[k]);
			this.getAllWordsPerDocument(documentString[k]);
		}
		
		return documentString;
	}
	public void deleteAllKindOfWord(String word) throws SQLException {
		this.stmt.executeUpdate("DELETE FROM `association_abstract` WHERE `word` = '" + word + "'");		
	}
	public String[] getAllWordsPerDocument(String document) throws SQLException {
		ResultSet rs;
		ResultSet rsAvg;
		
		String [] words = null;
		
		int i = 0;
		int avgWordCount = 0;
		int totalRows = 0;
		
		// String resultString = new String();
		// int totalWordcount = 0;
		
		rsAvg = this.stmt.executeQuery("SELECT MAX( `wordcount` ) as wc FROM `association_abstract` WHERE `document` = '" +  document + "'");
		while (rsAvg.next ()) {
			avgWordCount = avgWordCount + rsAvg.getInt ("wc");
		}
		if (avgWordCount > 1) {
			rs = this.stmt.executeQuery("SELECT `word`,`wordcount` FROM `association_abstract` WHERE `document` = '" +  document + "'");
		
			rs.last();
			totalRows = rs.getRow ();
			
			words = new String[totalRows];
			
			rs.first (); // eerste rij
			
			while(rs.next()) {
				if (rs.getInt ("wordcount") > ( avgWordCount - (avgWordCount * 0.35))) {
					i = i + 1;
					words[i] = rs.getString("word");
					// resultString = resultString + ", " + rs.getString("word");
					// System.out.println("Word: " + rs.getString("word") + ", #: " + rs.getInt ("wordcount"));
				}
			}
			// System.out.println("Relation between: " + resultString + " : Max Wordcount = " + avgWordCount );	
		}
		return words;
	}
	public double getAvgWord(String word) throws SQLException {
		ResultSet rsAvg;
		
		double avgWordCount = Double.NaN;
		
		// int totalWordcount = 0;
		rsAvg = this.stmt.executeQuery("SELECT AVG( `wordcount` ) as avgwc FROM `association_abstract` WHERE `word` = '" + word + "'");
		while (rsAvg.next ()) {
			avgWordCount = rsAvg.getDouble ("avgwc");
		}
		return avgWordCount;
	}
	public double getWordcountPerDocument(String word, String document) throws SQLException {
		ResultSet rsAvg;
		
		double wordCount = Double.NaN;
		
		// int totalWordcount = 0;
		rsAvg = this.stmt.executeQuery("SELECT `wordcount` FROM `association_abstract` WHERE `word` = '" + word + "' and `document` = '" + document + "'");
		while (rsAvg.next ()) {
			wordCount = rsAvg.getDouble ("wordcount");
		}
		return wordCount;
	}
	public void test() throws SQLException {
		ResultSet rs;
		
		try{
			this.stmt.executeUpdate("DROP TABLE myTable");
		}catch(Exception e){
			System.out.print(e);
			System.out.println("No existing table to delete");
		}//end catch

		//Create a table in the database named
		// myTable.
		this.stmt.executeUpdate(
				"CREATE TABLE myTable(test_id int," +
		"test_val char(15) not null)");

		//Insert some values into the table
		this.stmt.executeUpdate(
				"INSERT INTO myTable(test_id, " +
		"test_val) VALUES(1,'One')");
		this.stmt.executeUpdate(
				"INSERT INTO myTable(test_id, " +
		"test_val) VALUES(2,'Two')");
		this.stmt.executeUpdate(
				"INSERT INTO myTable(test_id, " +
		"test_val) VALUES(3,'Three')");
		this.stmt.executeUpdate(
				"INSERT INTO myTable(test_id, " +
		"test_val) VALUES(4,'Four')");
		this.stmt.executeUpdate(
				"INSERT INTO myTable(test_id, " +
		"test_val) VALUES(5,'Five')");

		//Get another statement object initialized
		// as shown.
		this.stmt = this.con.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);

		//Query the database, storing the result
		// in an object of type ResultSet
		rs = this.stmt.executeQuery("SELECT * " +
		"from myTable ORDER BY test_id");

		//Use the methods of class ResultSet in a
		// loop to display all of the data in the
		// database.
		System.out.println("Display all results:");
		while(rs.next()){
			int theInt= rs.getInt("test_id");
			String str = rs.getString("test_val");
			System.out.println("\ttest_id= " + theInt
					+ "\tstr = " + str);
		}//end while loop

		//Display the data in a specific row using
		// the rs.absolute method.
		System.out.println(
		"Display row number 2:");
		if( rs.absolute(2) ){
			int theInt= rs.getInt("test_id");
			String str = rs.getString("test_val");
			System.out.println("\ttest_id= " + theInt
					+ "\tstr = " + str);
		}//end if

		//Delete the table and close the connection
		// to the database
		// stmt.executeUpdate("DROP TABLE myTable");
		this.con.close();
	}
	
	public Collection<CorrOcc> getCorrOcc(String wordX, String wordY) throws SQLException {
		Collection<CorrOcc> data = new HashSet<CorrOcc>();
		
		ResultSet rsOccurence;
		
		// int totalWordcount = 0;
		rsOccurence = this.stmt.executeQuery("SELECT `document`, `word`, `wordcount` FROM `association_abstract` WHERE `word` = '" + wordX + "' or `word` = '" + wordY + "' order by document ASC");
		
		
		rsOccurence.next();
		
		CorrOcc tempCorrOcc = new CorrOcc();
		tempCorrOcc.setDocument(rsOccurence.getString("document"));
		if(rsOccurence.getString("word").compareTo(wordX)==0) {
			// We are processing an occurence of wordX:
			tempCorrOcc.setXCount(rsOccurence.getInt("wordcount"));
		}
		else {
			// We are processing an occurence of wordY:
			tempCorrOcc.setYCount(rsOccurence.getInt("wordcount"));
		}
		
		while (rsOccurence.next ()) {
			if((rsOccurence.getString("document").compareTo(tempCorrOcc.getDocument())!=0)) {
				// Last occurence was not the same doc, we can commit the previous CorrOcc:
				
				data.add(tempCorrOcc);
				
				// Make a new CorrOcc:
				tempCorrOcc = new CorrOcc();
				tempCorrOcc.setDocument(rsOccurence.getString("document"));
				
				if(rsOccurence.getString("word").compareTo(wordX)==0) {
					// We are processing an occurence of wordX:
					tempCorrOcc.setXCount(rsOccurence.getInt("wordcount"));
				}
				else {
					// We are processing an occurence of wordY:
					tempCorrOcc.setYCount(rsOccurence.getInt("wordcount"));
				}	
			}
			else {
				if(rsOccurence.getString("word").compareTo(wordX)==0) {
					// We are processing an occurence of wordX:
					tempCorrOcc.setXCount(rsOccurence.getInt("wordcount"));
				}
				else {
					// We are processing an occurence of wordY:
					tempCorrOcc.setYCount(rsOccurence.getInt("wordcount"));
				}
			}
		}
		
		return data;
	}
	
	public int getDocCount() throws SQLException {
		int result = 0;
		
		ResultSet rsDocCount;
		String qryDocCount = "SELECT COUNT(*) as numDocs FROM `association_abstract` GROUP BY `document`";
		rsDocCount = this.stmt.executeQuery(qryDocCount);
		
		if(rsDocCount.next()) {
			result = rsDocCount.getInt("numDocs");
		}
		
		return result;
	}
	
	public int getCoOccCount(String wordA, String wordB) throws SQLException {
		int result = 0;
		
		ResultSet rsCoOccCount;
		String qryWordA = "SELECT COUNT(*) as numDocs FROM `association_abstract` WHERE `word`='"+wordA+"' and document IN ";
		String qryWordB = "(SELECT document FROM `association_abstract` WHERE `word`='"+wordB+"')";
		
		rsCoOccCount = this.stmt.executeQuery(qryWordA+qryWordB);
		
		if(rsCoOccCount.next()) {
			result = rsCoOccCount.getInt("numDocs");
		}
		
		return result;
	}
	
	public int getWordCount(String word) throws SQLException {
		int result = 0;
		
		ResultSet rsWordCount;
		String qryWordCount = "(SELECT COUNT(*) as numDocs FROM `association_abstract` WHERE `word`='"+word+"')";
		
		rsWordCount = this.stmt.executeQuery(qryWordCount);
		
		if(rsWordCount.next()) {
			result = rsWordCount.getInt("numDocs");
		}
		
		return result;
	}
}

