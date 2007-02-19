/**
 * 
 */
package nl.eur.eco_ict.seminar.ontolearn.util.impl;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Queue;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.Document;
import nl.eur.eco_ict.seminar.ontolearn.datatypes.impl.DocumentFile;
import nl.eur.eco_ict.seminar.ontolearn.util.DocumentCrawler;

/**
 * 
 * A utility which crawls a directory and all of it's subdirectories for files (=documents)
 * @author Jasper
 *
 */
public class DiskCrawler implements DocumentCrawler {
	/**
	 * the pointer to the location where the fun begins. For now we assume it is a location on a filesystem, but it could also represent a URL
	 */
	protected URI documentroot = null;
	/**
	 * the files and subdirectories of the directory currently being processed
	 */
	protected File[] files = null;
	/**
	 * the index of the file array indicating which file/subdirectory of the currently processed directory was returned the last time
	 */
	protected int index = 0;
	/**
	 * A collection of directories which have been identified but have yet not been processed
	 */
	protected Queue<File> dirs = new LinkedList<File>();
	
	public DiskCrawler (String uri) throws URISyntaxException{
		this(new URI(uri));
	}
	
	public DiskCrawler (URI uri){
		this.documentroot = uri;
		this.init ();
	}
	
	protected void init (){
		File dir = null;
//		 if the crawler hasn't been accessed before prepare it by submitting a directory
		if(this.files == null && this.dirs.isEmpty ()){
			dir = new File (this.documentroot);
			if (dir.isFile ()){
				dir = dir.getParentFile ();
			}
			this.dirs.add (dir);
		}
	}
	
	/**
	 * @return the next unparsed document/file, if nothing suitable can be found a null will be returned
	 */
	public Document getNext(){
		File doc = null;
		File dir = null;
				
		// normal course of operations, either there are files to process or some directories. If that isn't the case we're done.
		if (this.files != null || !this.dirs.isEmpty ()){
			// there are still some files from the last directory left for processing
			if (this.files != null && this.index < this.files.length){
				doc = this.files[this.index];
				this.index++;
				if (doc.isDirectory ()){
					// darn, this file appears to be a directory, schedule it for later processing
					this.dirs.offer (doc);
					// recursive call
					return this.getNext ();
				}
				if (!doc.canRead ()){
					// darn, can't read it, let's hope the next file is something usefull
					return this.getNext();
				}
				// finally something we can harvest --> end of recursiveness
				return new DocumentFile(doc);
			}
			// if we get this far it means that all files of the last directory are processed. If there are more directories available for processing we can continue otherwise we will exit with a null value
			if (!this.dirs.isEmpty ()){
				dir = this.dirs.remove ();
				// initialize the directory's context
				this.index = 0;
				this.files = dir.listFiles ();
				// get the first file in this directory (recursively)
				return this.getNext ();
			}
		}
		// Nothing left to process
		return null;
	}
	
	/**
	 * @return true when there are more documents available to be processed, this is a prediction not a fact, it might still be that the getNext method returns null even though this method returned true... nothing is ever easy ;) 
	 */
	public boolean hasNext(){
		return !this.dirs.isEmpty () || (this.files != null && this.files.length - this.index > 0);
	}

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.util.DocumentCrawler#setRoot(java.net.URI)
	 */
	public void setRoot (URI root) {
		this.documentroot = root;
		this.init ();
	}
}
