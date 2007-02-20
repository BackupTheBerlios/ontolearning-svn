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
	protected DBSettings dbsettings = null;
	protected String ontNamespace = null;
	
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
	
	public DBSettings getDBinfo (){
		if (this.dbsettings == null){
			this.dbsettings = new DBSettings ();
			this.dbsettings.servertype = "HSQL";
			this.dbsettings.server = "jdbc:hsqldb:mem:OntologyStorage";
			this.dbsettings.username = "sa";
			this.dbsettings.password = "";
			this.dbsettings.driverclass = "org.hsqldb.jdbcDriver";
		}
		return this.dbsettings;
	}
	
	public void setDBInfo (DBSettings info){
		this.dbsettings = info;
	}
	
	public String getOntNamespace (){
		if (this.ontNamespace == null){
			this.ontNamespace = "";
		}
		return this.ontNamespace;
	}
	
	public void setOntNamespace (String namespace){
		
	}
}