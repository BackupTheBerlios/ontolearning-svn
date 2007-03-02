/**
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.datatypes.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.Document;

/**
 * Represents documents stored on disk
 * @author Jasper
 * @author Nico
 */
public class DocumentFile implements Document {
	private File file = null;
	
	private Collection<String> abstracts = null;

	public DocumentFile (File f) {
		this.file = f;
	}

	/**
	 * @return read the whole document/file
	 * @throws FileNotFoundException
	 */
	public BufferedReader read () throws FileNotFoundException {
		return new BufferedReader (new FileReader (this.file));
	}

	/**
	 * @return the abstracts contained in this document (file)
	 * @throws IOException
	 */
	public Collection<BufferedReader> readAbstracts () throws IOException {
		if (this.getAbstracts ().isEmpty () && this.isRedifDocument ()) {
			this.extractAbstracts ();
		} else {
			if (this.getAbstracts ().isEmpty ()) {
				this.getAbstracts ().add ("");
			}
		}
		return this.toReader (this.getAbstracts ());
	}
	
	protected Collection<BufferedReader> toReader (Collection<String> strings){
		Collection<BufferedReader> result = new HashSet<BufferedReader> ();
		Iterator<String> i = strings.iterator ();
		
		while(i.hasNext()){
			result.add (new BufferedReader(new StringReader (i.next())));
		}
		
		return result;
	}

	/**
	 * @return the name of the document
	 */
	public String getName () {
		//return this.getURI ().getPath ();
		return this.getURI ().toASCIIString ();
	}

	/**
	 * @return it's a redif document when it starts with 'Template-Type:
	 *         ReDIF-Paper'
	 * @throws FileNotFoundException
	 */
	protected boolean isRedifDocument () throws FileNotFoundException {
		final String header = "Template-Type: ReDIF-Paper";
		if (this.file != null && this.file.canRead ()) {
			BufferedReader br = new BufferedReader (new FileReader (this.file));
			String line = null;
			try {
				if ((line = br.readLine ()) != null) {
					return line.contains (header);
				}
			} catch (IOException e) {
				e.printStackTrace ();
			}
		}
		return false;
	}

	/**
	 * Assumes it's a REDIF file and searches the file for abstracts. When an
	 * abstract is found it is put into the global StringBuffer (a cache for
	 * repeated requests)
	 * @throws FileNotFoundException
	 */
	protected void extractAbstracts () throws FileNotFoundException {
		final String abstractHeader = "Abstract:";
		final String regEndAbstract = "^([a-zA-Z\\x2D]+)(\\x3A{1})( )(.)*$";
		StringBuffer abstrct = new StringBuffer ();
		BufferedReader br = new BufferedReader (new FileReader (this.file));
		String line = null;
		boolean extract = false;

		try {
			while ((line = br.readLine ()) != null) {
				
				// check if an abstract is being read and whether it is the last line of the abstract
				if (extract && line.matches (regEndAbstract)) {
					extract = false;
					this.getAbstracts ().add (abstrct.toString ());
				}
				// if an abstract is being read add the line
				if (extract) {
					abstrct.append (line.trim () + " ");
				}
				// check when not currently reading an abstract if one is starting
				if (!extract && line.startsWith (abstractHeader)) {
					extract = true; // take the next lines
					abstrct = new StringBuffer ();
					if (line.length () > 9) {
						abstrct.append (line
								.substring (10, line.length ()).trim ()
								+ " ");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}
	}
	
	protected Collection<String> getAbstracts (){
		if (this.abstracts == null){
			this.abstracts = new HashSet<String>();
		}
		return this.abstracts;
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.datatypes.Document#getURI()
	 */
	public URI getURI () {
		if (this.file != null){
		return this.file.toURI ();
		}
		return URI.create ("#");
	}
}
