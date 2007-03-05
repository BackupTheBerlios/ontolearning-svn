/**
 * OntoLearn a seminar project of: - Remy Stibbe - Hesing Kuo - Nico Vaatstra -
 * Jasper Voskuilen
 */
package nl.eur.eco_ict.seminar.ontolearn.util.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;

import nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology;
import nl.eur.eco_ict.seminar.ontolearn.util.Pruner;
import nl.eur.eco_ict.seminar.ontolearn.util.Stemmer;

/**
 * A pruner based on:<br>
 * <a href="http://www.springerlink.com/content/l1ecm0w93aj2jtgc/">Jordi Conesa
 * and Antoni Olive (2004). A General Method for Pruning OWL Ontologies. In
 * CoopIS/DOA/ODBASE 2004, LNCS pp. 981–998.</a>
 * @author Jasper
 */
public class OWLPruner implements Pruner {

	protected ConceptsOfInterestSelection cdi_selector = null;

	/**
	 * @see nl.eur.eco_ict.seminar.ontolearn.util.Pruner#prune(nl.eur.eco_ict.seminar.ontolearn.datatypes.Ontology)
	 */
	public void prune (Ontology ontology) {
		// rename concepts to their base lemma / stem
		this.lemmatize (ontology);
		Collection<OntClass> cdi = this.getConceptsOfDirectInterest (ontology);
		/*
		 * Once all the concepts of direct interest have been selected
		 * (selection activity), our algorithm obtains OP in four steps. The
		 * algorithm begins with an initial ontology O0 which is exactly Ob
		 * (that is, O0 := Ob) and obtains OP. The steps are:
		 */
		if (!cdi.isEmpty ()) {
			// - Pruning irrelevant concepts and constraints. The result is the
			// ontology O1.
			this.pruneIrrelevantConcepts (ontology, cdi);
			// - Pruning unnecessary parents. The result is the ontology O2.
			this.pruneUnnecessaryParents (ontology, cdi);
			// - Pruning unnecessary generalization paths. The result
			this.pruneUnnecessaryGeneralizationPaths (ontology, cdi);
			// - Pruning orphan individuals. The result is OP.
			this.pruneOrphanIndividuals (ontology);
		}
	}

	public void setCDISelector (ConceptsOfInterestSelection cdis) {
		this.cdi_selector = cdis;
	}

	/**
	 * rename concepts to their base lemma / stem, if it can be determined
	 * @param ontology
	 */
	protected void lemmatize (Ontology ontology) {
		Iterator<OntClass> i = ontology.getClasses ();
		OntClass oc = null;
		String lemma = null;

		while (i.hasNext ()) {
			try {
				oc = i.next ();
				lemma = Stemmer.Factory.getInstance ()
						.stem (oc.getLocalName ());
				// leave the multiword concepts alone & also do it only when it would be different
				if (!oc.getLocalName ().contains (" ") && !lemma.equals (oc.getLocalName ())) {
					OntClass repl = ontology.getOClass (lemma);
					if (repl == null){
						repl = ontology.addOClass (lemma);
					}
					repl.addLabel ("Original definition: " + oc.getLocalName (), null);
					oc.addEquivalentClass (repl);
					repl.addEquivalentClass (oc);
					ontology.replace (oc, repl);
				}
			} catch (Exception e) {
				System.err.println (e.getMessage ());
			}
		}
	}

	/**
	 * The concepts of direct interest for the final ontology are given in the
	 * set CoI, and G(CoI) is the set of concepts in which someone is directly
	 * or indirectly interested in. However, O0 may include other concepts,
	 * which are irrelevant for the IS. Therefore, in this step we prune from O0
	 * all concepts which are not in G(CoI). Pruning a concept implies the
	 * pruning of all generalization and classification relationships in which
	 * that concept participates. In OWL Full its sameAs relationships must be
	 * also deleted. Note that we cannot prune its instances, because they can
	 * also be instance of other relevant concepts. Similarly, we prune the
	 * constraints in O0 that are not relevant for the final ontology, because
	 * they constrain one or more concepts not in G(CoI). As a result we obtain
	 * an ontology called O1, which is the result of subtracting the irrelevant
	 * concepts and constraints from O0.
	 * @param ontology
	 * @param cdi
	 */
	protected void pruneIrrelevantConcepts (Ontology ontology,
			Collection<OntClass> cdi) {
		Iterator<OntClass> i = ontology.getClasses ();
		Collection<OntClass> toRemove = new HashSet<OntClass> ();
		OntClass current = null;

		while (i.hasNext ()) {
			current = i.next ();
			if (!cdi.contains (current)) {
				toRemove.add (current);
			}
		}

		i = toRemove.iterator ();
		while (i.hasNext ()) {
			ontology.remove (i.next ());
		}
	}

	/**
	 * After the previous step, the concepts of the resulting ontology (O1) are
	 * exactly G(CoI). However, not all of them are needed in OP. The concepts
	 * strictly needed (NeededConcepts) are given by the union of the concepts
	 * of direct interest and the constrained concepts of the remaining
	 * constraints. The other concepts are potentially not needed. We can prune
	 * the parents of NeededConcepts which are not children of some other
	 * concept in NeededConcepts. As we have said before, the pruning of a
	 * concept implies the pruning of all generalizations and classifications in
	 * which that concept participates.
	 * @param ontology
	 * @param cdi
	 */
	protected void pruneUnnecessaryParents (Ontology ontology,
			Collection<OntClass> cdi) {

	}

	/**
	 * In some cases, the ontology O2 may contain generalization paths between
	 * two concepts such that not all their components are necessary. The
	 * purpose of the third step is to prune these paths. We say that there is a
	 * generalization path between C1 and Cn if:<br> - C1 and Cn are two
	 * concepts from O2,<br> - IsA+(C1,Cn) and <br> - The path includes two or
	 * more generalization relationships IsA(C1,C2), …,<br>
	 * IsA(Cn-1,Cn). A generalization path IsA(C1,C2), …, IsA(Cn-1,Cn) between
	 * C1 and Cn is potentially redundant if none of the intermediate concepts
	 * C2, …, Cn-1:<br> - Is member of the set CoI ? CC(O2) <br> - Is the super
	 * or the sub of other generalization relationships.<br>
	 * A potentially redundant generalization path between concepts C1 and Cn is
	 * redundant if there are other generalization paths between the same pair
	 * of concepts. In this case, we prune the concepts C2, …, Cn-1 and all
	 * generalization relationships in which they participate. Note that, in the
	 * general case, this step is not determinist.<br>
	 * @param ontology
	 * @param cdi
	 */
	protected void pruneUnnecessaryGeneralizationPaths (Ontology ontology,
			Collection<OntClass> cdi) {
		Iterator<OntClass> i = ontology.getClasses ();
		Iterator<OntClass> j = null;
		OntClass ci = null, cj = null;
		while(i.hasNext ()){
			ci = i.next ();
			j = ontology.getClasses ();
			while (j.hasNext ()){
				cj = j.next ();
				if (ci != cj){
					this.pruneUnnecessaryGeneralizationPaths (ci, cj, cdi);
				}
			}
		}
	}
	
	protected void pruneUnnecessaryGeneralizationPaths (OntClass c1, OntClass c2, Collection<OntClass> cdi){
		if (this.hasGeneralizationPath (c1, c2)){
			Collection<List<OntClass>> paths = this.getGeneralizationPaths (c1, c2);
			Iterator<List<OntClass>> i = paths.iterator ();
			List<OntClass> path = null;
			Iterator<OntClass> classes = null;
			OntClass oclass = null;
			
			// remove the nonredundant paths
			while(i.hasNext ()){
				path = i.next ();
				path.remove (c1);
				path.remove (c2);
				// must have more than one intermediate step
				if (!path.isEmpty ()){
					classes = path.iterator ();
					boolean none = true;
					// all classes must not be part of cdi and not have more than one sub or super class relationship
					while (classes.hasNext ()){
						oclass = classes.next ();
						none = none && !cdi.contains (oclass);
						none = none && oclass.listSubClasses (true).toSet ().size () == 1;
						none = none && oclass.listSuperClasses (true).toSet ().size () == 1;
					}
					if (!none){
						i.remove ();
					}
				}else{
					i.remove ();
				}
			}
			
			// remove all but one redundant path
			i = paths.iterator ();
			while (paths.size () > 1 && i.hasNext ()){
				path = i.next ();
				classes = path.iterator ();
				while (classes.hasNext ()){
					classes.next ().remove ();
				}
			}
		}
	}
	
	protected Collection<List<OntClass>> getGeneralizationPaths (OntClass c1, OntClass c2){
		Collection<List<OntClass>> paths = new HashSet<List<OntClass>>();
		List<OntClass> path = null;
		path = new ArrayList<OntClass> ();
		path.add (c1);
		paths.add (path);
		
		this.getGeneralizationPaths (c1, c2, paths, path);
		
		// prune incomplete paths
		Iterator<List<OntClass>> i = paths.iterator();
		path = null;
		while(i.hasNext ()){
			path = i.next ();
			if (!path.contains (c1) || !path.contains (c2)){
				i.remove ();
			}
		}
		
		return paths;
	}
	
	@SuppressWarnings("unchecked")
	protected Collection<List<OntClass>> getGeneralizationPaths (OntClass c1, OntClass c2, Collection<List<OntClass>> paths, List<OntClass> curpath){
		Iterator<OntClass> i = c1.listSubClasses (true);
		OntClass temp = null;
		List<OntClass> path = null;
		
		while (i.hasNext ()){
			temp = i.next ();
			path = new ArrayList<OntClass>();
			path.addAll (curpath);
			path.add (temp);
			paths.add (path);
			if (temp.equals (c2)){
				return paths;
			}
			this.getGeneralizationPaths (temp, c2, paths, path);
		}
		
		return paths;
	}
	
	@SuppressWarnings("unchecked")
	protected boolean hasGeneralizationPath (OntClass c1, OntClass c2){
		Iterator<OntClass> i = c1.listSubClasses (false);
		while (i.hasNext ()){
			if (i.next ().equals (c2)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Once the previous steps have pruned the concepts of the ontology, the
	 * individuals of the ontology must be pruned as well. This step removes the
	 * instances of the ontology such that all its classifiers (classes or
	 * properties) have been deleted in the previous steps. When an instance of
	 * a class is deleted, all its value properties and sameAs relationships are
	 * deleted as well.
	 * @param ontology
	 */
	@SuppressWarnings("unchecked")
	protected void pruneOrphanIndividuals (Ontology ontology) {
		Iterator<Individual> i = ontology.getIndividuals ();
		Collection<Individual> toRemove = new HashSet<Individual> ();
		Individual ind = null;

		while (i.hasNext ()) {
			ind = i.next ();
			try {
				if (ind.getIsDefinedBy () == null) {
					toRemove.add (ind);
				}
			} catch (Exception e) {
				// System.err.println (e.getMessage());
			}
		}

		i = toRemove.iterator ();
		while (i.hasNext ()) {
			ontology.remove (i.next ());
		}
	}

	/**
	 * @return the collection of concepts wich may not be removed from the
	 *         ontology
	 */
	protected Collection<OntClass> getConceptsOfDirectInterest (
			Ontology ontology) {
		if (this.cdi_selector == null) {
			this.cdi_selector = ConceptsOfInterestSelection.Factory
					.getSelector ();
		}
		return this.cdi_selector.getConceptsOfInterest (ontology);
	}

}
