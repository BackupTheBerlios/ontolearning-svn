/**
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn;

import java.net.URI;
import java.net.URISyntaxException;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.Document;

/**
 * @author 300353jv
 *
 */
public class DocumentCrawler {
	protected URI documentroot = null;
	
	public DocumentCrawler (String uri) throws URISyntaxException{
		this(new URI(uri));
	}
	
	public DocumentCrawler (URI uri){
		this.documentroot = uri;
	}
	
	/**
	 * @return the next unparsed document/file
	 */
	public Document getNext(){
		return null;
	}
	
	/**
	 * @return true when there are more documents available to be processed 
	 */
	public boolean hasNext(){
		return false;
	}
}
