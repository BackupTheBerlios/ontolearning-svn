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

import nl.eur.eco_ict.seminar.ontolearn.datatypes.Document;

/**
 * Represents documents stored on disk
 * @author Jasper
 * @author Nico
 */
public class DocumentFile implements Document {
	private File file = null;
	private StringBuffer abstrct = null;

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
	 * @throws FileNotFoundException
	 */
	public BufferedReader readAbstracts () throws FileNotFoundException {
		// if (this.abstrct == null && this.isRedifDocument ()){
		// Nico edit: door if abstrct == null zit de StringReader die gereturned word bij de 2e extractor op de EOF en krijgt deze extractor geen data.
		if (this.isRedifDocument ()){
			this.extractAbstracts ();
		}else{
			this.abstrct = new StringBuffer();
		}
		return new BufferedReader (new StringReader (this.abstrct.toString ()
				));
	}
	
	/**
	 * @return the name of the document
	 */
	public String getName (){
		return this.file.getName ();
	}
	
	/**
	 * @return it's a redif document when it starts with 'Template-Type: ReDIF-Paper'
	 * @throws FileNotFoundException 
	 */
	protected boolean isRedifDocument () throws FileNotFoundException{
		final String header = "Template-Type: ReDIF-Paper";
		if (this.file != null && this.file.canRead ()){
			BufferedReader br = new BufferedReader (new FileReader(this.file));
			String line = null;
			try {
				if ((line=br.readLine ())!=null){
					return line.contains (header);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * Assumes it's a REDIF file and searches the file for abstracts. When an abstract is found it is put into the global StringBuffer (a cache for repeated requests)
	 * @throws FileNotFoundException
	 */
	protected void extractAbstracts () throws FileNotFoundException{
		final String abstractHeader = "Abstract:";
		final String regEndAbstract = "^([a-zA-Z\\x2D]+)(\\x3A{1})( )(.)*$";
		this.abstrct = new StringBuffer();
		BufferedReader br = new BufferedReader(new FileReader(this.file));
		String line = null;
		boolean extract = false;
		
		try{
		while((line = br.readLine ()) != null){
			if (extract && line.matches (regEndAbstract)){
				extract = false;
			}
			if (extract){
				this.abstrct.append (line.trim()+" ");
			}
			if (!extract && line.startsWith (abstractHeader)){
				extract = true; // take the next lines
				if(line.length() > 9) {
					this.abstrct.append (line.substring (10, line.length ()).trim()+" ");
				}
			}			
		}
		}catch(Exception e){
			e.printStackTrace ();
		}
	}
}
