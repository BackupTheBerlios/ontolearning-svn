/**
 * OntoLearn a seminar project of: - Remy Stibbe - Hesing Kuo - Nico Vaatstra -
 * Jasper Voskuilen
 */
package nl.eur.eco_ict.seminar.ontolearn.util.impl;

import java.util.Collection;
import java.util.Iterator;

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
public class PrunerStub implements Pruner {

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
		
		while(i.hasNext()){
			// TODO check/debug
			oc = i.next();
			lemma = Stemmer.Factory.getInstance ().stem(oc.getLocalName ());
			oc.setLabel (lemma, null);
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
	protected void pruneOrphanIndividuals (Ontology ontology) {

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
