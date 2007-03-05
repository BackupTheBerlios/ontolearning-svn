/**
 * OntoLearn a seminar project of: - Remy Stibbe - Hesing Kuo - Nico Vaatstra -
 * Jasper Voskuilen
 */
package nl.eur.eco_ict.seminar.ontolearn.util.impl;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.net.URI;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.impl.Settings;

/**
 * @author Jasper
 */
public class SettingsWindow extends JDialog implements ActionListener,
		FocusListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3572267753886869825L;

	protected Settings settings = null;

	protected JButton browseOntoOutput = null;

	protected JButton browseStartOnto = null;

	protected JButton browseDocRoot = null;

	protected JButton jbStart = null;

	protected JTextField tfDocRoot = null;

	protected JTextField tfStartOnto = null;

	protected JTextField tfOntoOutput = null;

	protected JTextField tfNamespace = null;
	
	protected JFileChooser fchooser = null;

	public SettingsWindow (Settings sett) {
		super ();
		this.settings = sett;
		this.init ();
		this.pack ();
		this.setVisible (true);
	}

	protected void initComponents () {
		this.jbStart = this.createJB ("Start");
		this.browseDocRoot = this.createJB ("Browse...");
		this.browseOntoOutput = this.createJB ("Browse...");
		this.browseStartOnto = this.createJB ("Browse...");

		this.tfDocRoot = this.createTF (this.settings.getDocumentroot ());
		this.tfNamespace = this.createTF (this.settings.getOntNamespace ());
		this.tfOntoOutput = this.createTF (this.settings.getOutputLocation ());
		this.tfStartOnto = this.createTF (this.settings.getStartingOntology ()
				.toString ());
	}

	protected void init () {
		this.initComponents ();
		JPanel jp = null;
		Box box = Box.createVerticalBox ();

		this.setTitle ("Configure ontology learner");
		this.setModal (true);
		this.setAlwaysOnTop (true);
		
		jp = new JPanel ();
		jp.setLayout (new FlowLayout (FlowLayout.LEADING));
		jp.add (new JLabel ("Folder with the data: "));
		jp.add (this.tfDocRoot);
		jp.add (this.browseDocRoot);
		box.add (jp);
		
		jp = new JPanel ();
		jp.setLayout (new FlowLayout (FlowLayout.LEADING));
		jp.add (new JLabel ("Write ontology to: "));
		jp.add (this.tfOntoOutput);
		jp.add (this.browseOntoOutput);
		box.add (jp);
		
		jp = new JPanel ();
		jp.setLayout (new FlowLayout (FlowLayout.LEADING));
		jp.add (new JLabel ("Use the following ontology: "));
		jp.add (this.tfStartOnto);
		jp.add (this.browseStartOnto);
		box.add (jp);
		
		jp = new JPanel ();
		jp.setLayout (new FlowLayout (FlowLayout.LEADING));
		jp.add (new JLabel ("The namespace to use for the ontology: "));
		jp.add (this.tfNamespace);
		box.add (jp);

		jp = new JPanel ();
		jp.setLayout (new FlowLayout (FlowLayout.TRAILING));
		jp.add (this.jbStart);
		box.add (jp);

		this.add (box);
	}

	protected JTextField createTF (String value) {
		JTextField tf = new JTextField ();
		if (value != null) {
			tf.setText (value);
		}
		tf.setColumns (30);
		tf.addFocusListener (this);
		return tf;
	}

	protected JButton createJB (String text) {
		JButton jb = new JButton (text);
		jb.addActionListener (this);
		return jb;
	}

	protected void setDocRoot (URI root) {
		this.settings.setDocumentroot (root.toString ());
		this.tfDocRoot.setText (root.toString ());
	}

	protected void setOntoOutput (String outputloc) {
		this.settings.setOutputLocation (outputloc);
		this.tfOntoOutput.setText (outputloc);
	}

	protected void setNamespace (String namespace) {
		this.settings.setOntNamespace (namespace);
		this.tfNamespace.setText (namespace);
	}

	protected void setStartOnto (URI startloc) {
		this.settings.setStartingOntology (startloc);
		this.tfStartOnto.setText (startloc.toString ());
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed (ActionEvent e) {
		JFileChooser jf = this.getFileChooser ();
		if (e.getSource () == this.browseDocRoot) {
			jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			jf.setCurrentDirectory (new File (this.settings.getDocumentroot ()));
			jf.showOpenDialog (this);
			File dir = jf.getCurrentDirectory ();
			this.setDocRoot (dir.toURI ());
		}
		if (e.getSource () == this.browseOntoOutput) {
			jf.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			jf.setCurrentDirectory (new File (this.settings.getOutputLocation ()));
			jf.showSaveDialog (this);
			File file = jf.getSelectedFile ();
			this.setOntoOutput (file.getAbsolutePath ());
		}
		if (e.getSource () == this.browseStartOnto) {
			jf.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			jf.setCurrentDirectory (new File (this.settings.getStartingOntology ().toString ()));
			jf.showOpenDialog (this);
			File file = jf.getSelectedFile ();
			this.setStartOnto (file.toURI ());
		}
		if (e.getSource () == this.jbStart) {
			this.dispose ();
		}
	}

	/**
	 * Ignored
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained (FocusEvent e) {}

	/**
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost (FocusEvent e) {
		if (e.getSource () == this.tfDocRoot) {
			try{
				URI uri = new URI (this.tfDocRoot.getText ());
				this.setDocRoot (uri);
			}catch (Exception ex){
				this.tfDocRoot.setFocusable (true);
				this.showError (""+ this.tfDocRoot.getText () + " isn't a valid URI");
			}
		}
		if (e.getSource () == this.tfNamespace) {
			try{
				URI uri = new URI (this.tfNamespace.getText ());
				if (!uri.isAbsolute ()){
					throw new IllegalArgumentException ();
				}
				this.setNamespace (uri.toString ());
			}catch (Exception ex){
				this.tfNamespace.setFocusable (true);
				this.showError (""+ this.tfNamespace.getText () + " isn't a valid absolute URI");
			}
		}
		if (e.getSource () == this.tfOntoOutput) {
			try{
				File f = new File (this.tfOntoOutput.getText ());
				if (!f.canWrite ()){
					throw new IllegalArgumentException ();
				}
				this.setOntoOutput (f.toString ());
			}catch (Exception ex){
				this.tfOntoOutput.setFocusable (true);
				this.showError ("Can't write on this location " + this.tfOntoOutput.getText ());
			}
		}
		if (e.getSource () == this.tfStartOnto) {
			try{
				URI uri = new URI (this.tfStartOnto.getText ());
				this.setStartOnto (uri);
			}catch (Exception ex){
				this.tfStartOnto.setFocusable (true);
				this.showError (""+ this.tfStartOnto.getText () + " isn't a valid URI");
			}
		}
	}
	
	protected void showError (String msg){
		JOptionPane.showMessageDialog (this, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	protected JFileChooser getFileChooser (){
		if (this.fchooser == null){
			this.fchooser = new JFileChooser ();
		}
		this.fchooser.setCurrentDirectory (null);
		return this.fchooser;
	}
	
	

}
