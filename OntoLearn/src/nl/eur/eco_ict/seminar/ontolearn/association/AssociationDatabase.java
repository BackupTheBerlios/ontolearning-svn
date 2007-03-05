/**
 * OntoLearn a seminar project of: - Remy Stibbe - Hesing Kuo - Nico Vaatstra -
 * Jasper Voskuilen
 */
package nl.eur.eco_ict.seminar.ontolearn.association;

import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.impl.CorrOcc;

/**
 * @author remy
 */
public class AssociationDatabase {

	protected Connection con;

	protected String url;

	protected Statement stmt;

	protected boolean inmemory = false;

	//protected Collection<Occurance> occuranceMatrix = new HashSet<Occurance> ();
	
	protected Map<String, Collection<Occurance>> wordOccurances = new HashMap<String, Collection<Occurance>>();
	protected Map<String, Collection<Occurance>> docuemntOccurances = new HashMap<String, Collection<Occurance>>();

	public AssociationDatabase () {
		if (!this.inmemory) {
			try {
				// Register the JDBC driver for MySQL.
				Class.forName ("com.mysql.jdbc.Driver");

				// Define URL of database server for
				// database named JunkDB on the localhost
				// with the default port number 3306.
				this.url = "jdbc:mysql://localhost:3306/ontolearn";

				// Get a connection to the database for a
				// user named auser with the password
				// drowssap, which is password spelled
				// backwards.
				this.con = DriverManager.getConnection (this.url, "ontolearn",
						"ontolearn");

				// Display URL and connection information
				// System.out.println("URL: " + this.url);
				// System.out.println("Connection: " + this.con);

				// Get a Statement object
				this.stmt = this.con.createStatement ();
			} catch (Exception e) {
				//e.printStackTrace ();
			}
		}

	}

	public void cleanDB () {
		if (!this.inmemory) {
			String sqlEmptyDB = "TRUNCATE TABLE `association_abstract`";

			try {
				this.stmt.executeQuery (sqlEmptyDB);
			} catch (Exception e) {
				e.printStackTrace ();
			}
		} else {
			//this.occuranceMatrix.clear ();
			this.wordOccurances.clear ();
			this.docuemntOccurances.clear ();
		}

	}
	
	public void addConcept (Occurance occ) throws SQLException{
		if (!this.inmemory){
			this.addConcept (occ.getDocumentName (), occ.getWord (), new Integer(occ.getWordCount ()));
		}else{
			if (!this.wordOccurances.containsKey (occ.getWord ())){
				this.wordOccurances.put (occ.getWord (), new HashSet<Occurance>());
			}
			this.wordOccurances.get (occ.getWord ()).add (occ);
			if (!this.docuemntOccurances.containsKey (occ.getDocumentName ())){
				this.docuemntOccurances.put (occ.getDocumentName (), new HashSet<Occurance>());
			}
			this.docuemntOccurances.get (occ.getDocumentName ()).add(occ);
		}
	}

	protected void addConcept (String document, String word, Integer wordCount)
			throws SQLException {
		if (!this.inmemory) {

			// System.out.println ("INSERT INTO `association_abstract`
			// (`document`, `word`, `wordcount`) VALUES('" + document + "','" +
			// word + "', '" + wordCount + "')");
			this.stmt
					.executeUpdate ("INSERT INTO `association_abstract` (`document`, `word`, `wordcount`) VALUES('"
							+ document
							+ "','"
							+ word
							+ "', '"
							+ wordCount
							+ "')");
		} else {
			Occurance occ = new Occurance ();
			occ.setDocumentName (document);
			occ.setWord (word);
			occ.setWordCount (wordCount.intValue ());
			this.addConcept (occ);
		}
	}

	public void updateConcept (String document, String word, Integer wordCount)
			throws SQLException {
		if (!this.inmemory) {
			// System.out.println ("UPDATE `association_abstract` SET
			// `wordcount` = '" + wordCount + "' WHERE `document` = '" +
			// document + "' and `word` = '" + word + "'");
			this.stmt
					.executeUpdate ("UPDATE `association_abstract` SET `wordcount` = '"
							+ wordCount
							+ "' WHERE `document` = '"
							+ document
							+ "' and `word` = '" + word + "'");
		} else {
			Occurance occ = this.getOccurance (document, word);
			occ.setWordCount (wordCount.intValue ());
		}
	}

	public Occurance getOccurance (String document, String word) {
		Collection<Occurance> words = new HashSet<Occurance>();
		Collection<Occurance> documents = new HashSet<Occurance>();
		
		if (this.wordOccurances.containsKey(word)){
			words.addAll (this.wordOccurances.get (word));
		}
		if (this.docuemntOccurances.containsKey (document)){
			documents.addAll (this.docuemntOccurances.get (document));
		}
		words.retainAll (documents);
		
		Iterator<Occurance> i = words.iterator ();
		Occurance temp = null;
		while (i.hasNext () && temp == null) {
			temp = i.next ();
			if (!temp.getDocumentName ().equals (document)
					|| !temp.getWord ().equals (word)) {
				temp = null;
			}
		}
		return temp;
	}

	public void deleteLessUsedWords () throws SQLException {
		if (!this.inmemory) {
			// get distinct information per word
			ResultSet rs;
			String[] iWord = null;
			int i = 0;

			rs = this.stmt
					.executeQuery ("SELECT `word`, COUNT(`wordcount`) as `wordcount` FROM `association_abstract` GROUP BY `word`");
			System.out.println (rs);
			System.out.println ("Display all results:");
			while (rs.next ()) {
				if (rs.getInt ("wordcount") < 2) {
					i = i + 1;
				}
			}
			System.out.println ("Number of grouped words:" + i);
			iWord = new String[i];
			int j = 0;
			rs.first ();
			while (rs.next ()) {
				if (rs.getInt ("wordcount") < 2) {
					iWord[j] = rs.getString ("word");
					j = j + 1;
				}
			}
			for (int k = 0; k < iWord.length; k++) {
				System.out.println (iWord[k]);
				this.deleteAllKindOfWord (iWord[k]);
			}
		} else {
			// TODO
		}
	}

	public String[] getSignificantWordsPerDocument () throws SQLException {
		String[] documentString = null;
		if (!this.inmemory) {
			ResultSet rs;

			int i = 0;
			rs = this.stmt
					.executeQuery ("SELECT DISTINCT `document` FROM `association_abstract` ");
			while (rs.next ()) {
				i = i + 1;
			}
			documentString = new String[i];
			int j = 0;
			rs.first ();
			while (rs.next ()) {
				documentString[j] = rs.getString ("document");
				j = j + 1;
			}
			for (int k = 0; k < documentString.length; k++) {
				// System.out.println(documentString[k]);
				this.getAllWordsPerDocument (documentString[k]);
			}
		} else {
			// TODO
		}

		return documentString;
	}

	public void deleteAllKindOfWord (String word) throws SQLException {
		if (!this.inmemory) {
			this.stmt
					.executeUpdate ("DELETE FROM `association_abstract` WHERE `word` = '"
							+ word + "'");
		} else {
			Iterator<Occurance> i = this.wordOccurances.get (word).iterator ();
			Collection<Occurance> toRemove = new HashSet<Occurance>();
			Occurance occ = null;
			while (i.hasNext ()) {
				occ = i.next ();
				if (occ.getWord ().equals (word)) {
					toRemove.add (occ);
				}
			}
			i = toRemove.iterator ();
			while(i.hasNext ()){
				this.remove (i.next ());
			}
		}
	}
	
	public void remove (Occurance occ){
		if (!this.inmemory){
			// TODO
		}else{
			Collection<Occurance> occs = this.wordOccurances.get (occ.getWord ());
			if (occs.contains (occ)){
				occs.remove (occ);
			}
			occs = this.docuemntOccurances.get (occ.getDocumentName ());
			if (occs.contains (occ)){
				occs.remove (occ);
			}
		}
	}

	public String[] getAllWordsPerDocument (String document)
			throws SQLException {
		String[] words = null;
		if (!this.inmemory) {
			ResultSet rs;
			ResultSet rsAvg;

			int i = 0;
			int j = 0;
			int avgWordCount = 0;
			// int totalRows = 0;

			// String resultString = new String();
			// int totalWordcount = 0;

			rsAvg = this.stmt
					.executeQuery ("SELECT MAX( `wordcount` ) as wc FROM `association_abstract` WHERE `document` = '"
							+ document + "'");
			while (rsAvg.next ()) {
				avgWordCount = avgWordCount + rsAvg.getInt ("wc");
			}
			if (avgWordCount > 1) {
				rs = this.stmt
						.executeQuery ("SELECT `word`,`wordcount` FROM `association_abstract` WHERE `document` = '"
								+ document + "'");

				// rs.last();
				// totalRows = rs.getRow ();

				rs.first (); // eerste rij

				while (rs.next ()) {
					if (rs.getInt ("wordcount") > (avgWordCount - (avgWordCount * 0.35))) {
						if (rs.getString ("word") != null)
							i = i + 1;
						// words[i] = rs.getString("word");
						// resultString = resultString + ", " +
						// rs.getString("word");
						// System.out.println("Word: " + rs.getString("word") +
						// ", #: " + rs.getInt ("wordcount"));
					}
				}
				words = new String[i + 1];
				rs.first ();
				while (rs.next ()) {
					if (rs.getInt ("wordcount") > (avgWordCount - (avgWordCount * 0.35))) {
						if (rs.getString ("word") != null) {
							j = j + 1;
							words[j] = rs.getString ("word");
							// resultString = resultString + ", " +
							// rs.getString("word");
							// System.out.println("Word: " +
							// rs.getString("word") + ", #: " + rs.getInt
							// ("wordcount"));
						}
					}
				}
				// System.out.println("Relation between: " + resultString + " :
				// Max Wordcount = " + avgWordCount );
			}
		} else {
			HashSet<String> wordset = new HashSet<String>();
			Iterator<Occurance> i = this.docuemntOccurances.get (document).iterator ();
			Occurance occ = null;
			while (i.hasNext ()){
				occ = i.next ();
				if (occ.getDocumentName ().equals (document) && !wordset.contains (occ.getWord ())){
					wordset.add (occ.getWord ());
				}
			}
			words = wordset.toArray (new String[wordset.size ()]);
		}
		return words;
	}

	public double getAvgWord (String word) throws SQLException {
		double avgWordCount = Double.NaN;
		if (!this.inmemory) {
			ResultSet rsAvg;
			// int totalWordcount = 0;
			rsAvg = this.stmt
					.executeQuery ("SELECT AVG( `wordcount` ) as avgwc FROM `association_abstract` WHERE `word` = '"
							+ word + "'");
			while (rsAvg.next ()) {
				avgWordCount = rsAvg.getDouble ("avgwc");
			}
		} else {
			int count=0, sum=0;
			Iterator<Occurance> i = this.wordOccurances.get (word).iterator ();
			Occurance occ = null;
			while (i.hasNext ()){
				occ = i.next ();
				if (occ.getWord ().equals (word)){
					sum += occ.getWordCount ();
					count ++;
				}
			}
			avgWordCount = (double)sum / (double)count;
		}
		return avgWordCount;
	}

	public double getWordcountPerDocument (String word, String document)
			throws SQLException {
		double wordCount = Double.NaN;
		if (!this.inmemory) {
			ResultSet rsAvg;

			// int totalWordcount = 0;
			rsAvg = this.stmt
					.executeQuery ("SELECT `wordcount` FROM `association_abstract` WHERE `word` = '"
							+ word + "' and `document` = '" + document + "'");
			while (rsAvg.next ()) {
				wordCount = rsAvg.getDouble ("wordcount");
			}
		} else {
			Occurance occ = this.getOccurance (document, word);
			if (occ != null) {
				wordCount = occ.getWordCount ();
			}
		}
		return wordCount;
	}

	public void test () throws SQLException {
		ResultSet rs;

		try {
			this.stmt.executeUpdate ("DROP TABLE myTable");
		} catch (Exception e) {
			System.out.print (e);
			System.out.println ("No existing table to delete");
		}// end catch

		// Create a table in the database named
		// myTable.
		this.stmt.executeUpdate ("CREATE TABLE myTable(test_id int,"
				+ "test_val char(15) not null)");

		// Insert some values into the table
		this.stmt.executeUpdate ("INSERT INTO myTable(test_id, "
				+ "test_val) VALUES(1,'One')");
		this.stmt.executeUpdate ("INSERT INTO myTable(test_id, "
				+ "test_val) VALUES(2,'Two')");
		this.stmt.executeUpdate ("INSERT INTO myTable(test_id, "
				+ "test_val) VALUES(3,'Three')");
		this.stmt.executeUpdate ("INSERT INTO myTable(test_id, "
				+ "test_val) VALUES(4,'Four')");
		this.stmt.executeUpdate ("INSERT INTO myTable(test_id, "
				+ "test_val) VALUES(5,'Five')");

		// Get another statement object initialized
		// as shown.
		this.stmt = this.con.createStatement (
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		// Query the database, storing the result
		// in an object of type ResultSet
		rs = this.stmt.executeQuery ("SELECT * "
				+ "from myTable ORDER BY test_id");

		// Use the methods of class ResultSet in a
		// loop to display all of the data in the
		// database.
		System.out.println ("Display all results:");
		while (rs.next ()) {
			int theInt = rs.getInt ("test_id");
			String str = rs.getString ("test_val");
			System.out.println ("\ttest_id= " + theInt + "\tstr = " + str);
		}// end while loop

		// Display the data in a specific row using
		// the rs.absolute method.
		System.out.println ("Display row number 2:");
		if (rs.absolute (2)) {
			int theInt = rs.getInt ("test_id");
			String str = rs.getString ("test_val");
			System.out.println ("\ttest_id= " + theInt + "\tstr = " + str);
		}// end if

		// Delete the table and close the connection
		// to the database
		// stmt.executeUpdate("DROP TABLE myTable");
		this.con.close ();
	}

	public Collection<CorrOcc> getCorrOcc (String wordX, String wordY)
			throws SQLException {
		Collection<CorrOcc> data = new HashSet<CorrOcc> ();
		if (!this.inmemory) {

			ResultSet rsOccurence;

			// int totalWordcount = 0;
			rsOccurence = this.stmt
					.executeQuery ("SELECT `document`, `word`, `wordcount` FROM `association_abstract` WHERE `word` = '"
							+ wordX
							+ "' or `word` = '"
							+ wordY
							+ "' order by document ASC");

			rsOccurence.next ();

			CorrOcc tempCorrOcc = new CorrOcc ();
			tempCorrOcc.setDocument (rsOccurence.getString ("document"));
			if (rsOccurence.getString ("word").compareTo (wordX) == 0) {
				// We are processing an occurence of wordX:
				tempCorrOcc.setXCount (rsOccurence.getInt ("wordcount"));
			} else {
				// We are processing an occurence of wordY:
				tempCorrOcc.setYCount (rsOccurence.getInt ("wordcount"));
			}

			while (rsOccurence.next ()) {
				if ((rsOccurence.getString ("document").compareTo (
						tempCorrOcc.getDocument ()) != 0)) {
					// Last occurence was not the same doc, we can commit the
					// previous CorrOcc:

					data.add (tempCorrOcc);

					// Make a new CorrOcc:
					tempCorrOcc = new CorrOcc ();
					tempCorrOcc
							.setDocument (rsOccurence.getString ("document"));

					if (rsOccurence.getString ("word").compareTo (wordX) == 0) {
						// We are processing an occurence of wordX:
						tempCorrOcc
								.setXCount (rsOccurence.getInt ("wordcount"));
					} else {
						// We are processing an occurence of wordY:
						tempCorrOcc
								.setYCount (rsOccurence.getInt ("wordcount"));
					}
				} else {
					if (rsOccurence.getString ("word").compareTo (wordX) == 0) {
						// We are processing an occurence of wordX:
						tempCorrOcc
								.setXCount (rsOccurence.getInt ("wordcount"));
					} else {
						// We are processing an occurence of wordY:
						tempCorrOcc
								.setYCount (rsOccurence.getInt ("wordcount"));
					}
				}
			}
		} else {
			Map<String, Integer> xOcc = new HashMap<String, Integer>();
			Map<String, Integer> yOcc = new HashMap<String, Integer>();
			Collection<Occurance> ocss = new HashSet<Occurance>();
			ocss.addAll (this.wordOccurances.get (wordX));
			ocss.addAll (this.wordOccurances.get (wordY));			
			Iterator<Occurance> i = ocss.iterator ();
			Occurance occ = null;
			while (i.hasNext ()){
				occ = i.next ();
				if (occ.getWord ().equals(wordX)){
					xOcc.put (occ.getDocumentName (), new Integer(occ.getWordCount ()));
				}
				if (occ.getWord ().equals (wordY)){
					yOcc.put (occ.getDocumentName (), new Integer(occ.getWordCount ()));
				}
			}
			
			Iterator<String> docs = null;
			String doc = null;
			
			// Process all the dosuments in which wordX occurrs
			docs = xOcc.keySet ().iterator ();
			while(docs.hasNext ()){
				doc = docs.next ();
				CorrOcc cOcc = new CorrOcc ();
				cOcc.setDocument (doc);
				cOcc.setXCount (xOcc.get (doc).intValue ());
				if (yOcc.containsKey (doc)){
					cOcc.setYCount (yOcc.get (doc).intValue ());
				}else{
					cOcc.setYCount (0);
				}
				data.add (cOcc);
			}
			
			// get the documents in which wordY occurs but which not have been processed yet
			Collection<String> remdocs = new HashSet<String>(yOcc.keySet ());
			remdocs.removeAll (xOcc.keySet ());
			docs = remdocs.iterator ();
			while(docs.hasNext ()){
				doc = docs.next ();
				CorrOcc cOcc = new CorrOcc ();
				cOcc.setDocument (doc);
				cOcc.setYCount (yOcc.get (doc).intValue ());
				if (xOcc.containsKey (doc)){
					cOcc.setXCount (xOcc.get (doc).intValue ());
				}else{
					cOcc.setXCount (0);
				}
				data.add (cOcc);
			}
		}

		return data;
	}

	/**
	 * @return the number of distinct documents analyzed
	 * @throws SQLException
	 */
	public int getDocCount () throws SQLException {
		int result = 0;
		if (!this.inmemory) {
			ResultSet rsDocCount;
			String qryDocCount = "SELECT COUNT(DISTINCT `document`) as numDocs FROM `association_abstract`";
			rsDocCount = this.stmt.executeQuery (qryDocCount);

			if (rsDocCount.next ()) {
				result = rsDocCount.getInt ("numDocs");
			}
		} else {
			result = this.docuemntOccurances.keySet ().size ();
		}

		return result;
	}

	/**
	 * @param wordA
	 * @param wordB
	 * @return ??? the number of documents in which both words occur 
	 * OR the 
	 * @throws SQLException
	 */
	public int getCoOccCount (String wordA, String wordB) throws SQLException {
		int result = 0;
		if (!this.inmemory) {
			ResultSet rsCoOccCount;
			String qryWordA = "SELECT COUNT(*) as numDocs FROM `association_abstract` WHERE `word`='"
					+ wordA + "' and document IN ";
			String qryWordB = "(SELECT document FROM `association_abstract` WHERE `word`='"
					+ wordB + "')";

			rsCoOccCount = this.stmt.executeQuery (qryWordA + qryWordB);

			if (rsCoOccCount.next ()) {
				result = rsCoOccCount.getInt ("numDocs");
			}
		} else {
			Collection<Occurance> occs = new HashSet<Occurance>();
			occs.addAll (this.wordOccurances.get (wordA));
			occs.addAll (this.wordOccurances.get (wordB));
			Iterator<Occurance> i = occs.iterator ();
			Map<String, Integer> aOcc = new HashMap<String,Integer>();
			Map<String, Integer> bOcc = new HashMap<String,Integer>();
			Occurance temp = null;
			while(i.hasNext ()){
				temp = i.next ();
				if (temp.getWord ().equals (wordA)){
					aOcc.put (temp.getDocumentName (), new Integer(temp.getWordCount ()));
				}
				if (temp.getWord ().equals (wordB)){
					bOcc.put (temp.getDocumentName (), new Integer(temp.getWordCount ()));
				}
			}
			
			// for each document in which wordA occurred
			Iterator<String> d = aOcc.keySet ().iterator ();
			String doc = "";
			int cocount = 0;
			while(d.hasNext ()){
				doc = d.next ();
				// check if the bword also occured in this document
				if (bOcc.containsKey (doc)){
					// If you're only interested in the number of documents in which they co-occur uncomment the next line
					// cocount++;
					cocount = Math.min (aOcc.get (doc).intValue (), bOcc.get (doc).intValue ());
					result += cocount;
				}
			}
		}

		return result;
	}

	/**
	 * @param word
	 * @return ???? the number of documents the word appears in vs. the number of times the word is counted over all documents
	 * @throws SQLException
	 */
	public int getWordCount (String word) throws SQLException {
		int result = 0;
		if (!this.inmemory) {
			ResultSet rsWordCount;
			//FIXME should we return the number of documents in which the word occured or the number of times the word actually occurred in all the documents? e.g. SUM instead of count
			String qryWordCount = "(SELECT COUNT(*) as numDocs FROM `association_abstract` WHERE `word`='"
					+ word + "')";

			rsWordCount = this.stmt.executeQuery (qryWordCount);


			if (rsWordCount.next ()) {
				result = rsWordCount.getInt ("numDocs");
			}
		} else {
			Iterator<Occurance> i = this.wordOccurances.get (word).iterator ();
			Occurance temp = null;
			while(i.hasNext ()){
				temp = i.next ();
				if (temp.getWord ().equals (word)){
					// now the sum of all occurances is calculated change to +=1 to count only in how many documents it occurred
					result += temp.getWordCount ();
				}
			}
		}

		return result;
	}
	
	/**
	 * @param mem if true, data will be stored in memory otherwise it is written to a database
	 */
	public void setStorage (boolean mem){
		this.inmemory = mem;
	}
}
