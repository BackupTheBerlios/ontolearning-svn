/**
 * 
 */
package src.nl.eur.eco_ict.seminar.ontlearning.kuovosvaasti;

import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import nl.rsm.util.db.Database;
import nl.rsm.util.db.drivers.DBDriver;
import nl.rsm.util.db.drivers.MySQLDriver;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.db.ModelRDB;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * @author JVoskuilen
 *
 */
public class OntologyManager {
	protected static OntologyManager manager = null;
	protected OntModel model = null;
	protected OpenMode openMode = null;
	protected String defNS = null;
	
	public enum OpenMode {
		NEWONT_NAMED, OPENONT_NAMED; 
		
		private OpenMode (){}
		private String name = "http://someurl/somedir/";
		
		public String getName (){
			return this.name;
		}
		
		public OpenMode setName (String s){
			this.name =s;
			return this;
		}
	}
	
	protected OntologyManager (OpenMode mode){
		this.openMode = mode;
		this.getModel (mode);
	}
	
	public static OntologyManager get (OpenMode mode){
		if (manager == null || (manager != null && manager.model == null)){
			manager = new OntologyManager(mode);
		}
		return manager;
	}
	
	public Collection<OntClass> getClasses (){
		Collection<OntClass> classes = new HashSet<OntClass> ();
		ExtendedIterator i = this.model.listClasses ();
		Object o = null;
		
		while (i.hasNext()){
			o = i.next();
			if (o instanceof OntClass){
				classes.add ((OntClass)o);
			}
		}
		i.close();
		
		return Collections.unmodifiableCollection (classes);
	}
	
	public Collection<ObjectProperty> getProperties (){
		Collection<ObjectProperty> properties = new HashSet<ObjectProperty> ();
		ExtendedIterator i = this.model.listObjectProperties ();
		Object o = null;
		
		while (i.hasNext()){
			o = i.next();
			if (o instanceof ObjectProperty){
				properties.add ((ObjectProperty)o);
			}
		}
		i.close();
		
		return Collections.unmodifiableCollection (properties);
	}
	
	public Collection<Individual> getIndividuals (){
		Collection<Individual> individuals = new HashSet<Individual> ();
		ExtendedIterator i = this.model.listIndividuals ();
		Object o = null;
		
		while (i.hasNext()){
			o = i.next();
			if (o instanceof Individual){
				individuals.add ((Individual)o);
			}
		}
		i.close();
		
		return Collections.unmodifiableCollection (individuals);
	}
	
	public OntClass addClass (String name){
		if (this.defNS != null){
			return this.model.createClass (this.defNS + name);
		}
		return this.model.createClass (name);
	}
	
	public OntClass addClass (String namespace, String name){
		return this.model.createClass (namespace + name);
	}
	
	public Individual addIndividual (OntClass pClass, String value){
		return this.model.createIndividual (value, pClass);
	}
	
	public ObjectProperty addProperty (String namespace, String value){
		return this.model.createObjectProperty (namespace + value);
	}
	
	public ObjectProperty addProperty (String value){
		if (this.defNS != null){
			return this.model.createObjectProperty (this.defNS + value);
		}
		return this.model.createObjectProperty (value);
	}
	
	public void setNamespace (String ns){
		this.defNS = ns;
	}
	
	protected Model getModel (OpenMode mode){
		if (this.model == null){
			Model temp = null;
//			 database URL
			String className = "org.hsqldb.jdbcDriver";       // path of driver class
                   // Load the Driver
			String DB_URL =    "jdbc:hsqldb:file:test";   // URL of database 
			String DB_USER =   "sa";                          // database user id
			String DB_PASSWD = "";                            // database password
			String DB =        "HSQL";       
			try {
				Class.forName(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			/*Database db = new Database();
			// String DBType = p.getProperty("database_type");
			String url = M_DB_URL;
			DBDriver driver = new MySQLDriver();
			driver.setConnectionURL(url);
			db.setDriver(driver);
			
			try {
				System.out.print ("Establishing connection to backend database... ");
				db.connect ();
				System.out.println ("Connected!");
			} catch (SQLException e1) {
				System.err.println ("Couldn't connect to: "+ url);
				e1.printStackTrace();
			}*/
						
//			 create a database connection
			IDBConnection conn = new DBConnection ( DB_URL, DB_USER, DB_PASSWD, DB );
			try {
				conn.cleanDB ();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		/*	switch (mode){
				case NEWONT_NAMED:
//			 or create a named model
			temp = ModelRDB.createModel(conn, mode.getName ());
			break;
				case OPENONT_NAMED:
//			 or open a previously created named model
			temp = ModelRDB.open(conn,mode.getName ());
			break;
			default: 
				temp = ModelRDB.createModel(conn);
			}*/
			
			temp = ModelRDB.createModel(conn);
			
			try{
			System.out.print ("Loading ontology ("+mode.getName ()+") from database...");
			 this.model = ModelFactory.createOntologyModel(new OntModelSpec( OntModelSpec.OWL_MEM ),  temp);
			 this.model.read (mode.getName ());
			 System.out.println (" done!");
			}catch(Exception e){
				System.err.println (e.getMessage ());
			}
			 
	}
		return this.model;
	}
	
	protected OntModelSpec getModelSpec( ModelMaker maker ) {
		OntModelSpec spec = new OntModelSpec( OntModelSpec.OWL_MEM );
		
        spec.setImportModelMaker( maker );

        return spec;
	}
	
	public String toString (){
		StringWriter sw = new StringWriter();
		this.model.write (sw);
		return sw.toString();
	}
}
