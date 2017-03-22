package strategies.experiments;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import Program.Clause;
import Program.ClauseType;
import abduction.Abducibles;
import common.Atom;
import common.Predicate;
import experiments.Experiment;
import experiments.ExperimentBlock;

public class ExperimentAbduciblesSubsetWithAbdnormalities implements StrategyExperiments {

	@Override
	public List<Experiment> getExperiments(ExperimentBlock experimentBlock) {
		
		Abducibles abducibles = experimentBlock.getCurrentAbducibles();
		List<Experiment> listForExperiment = new ArrayList<Experiment>();
		
		
		//Get all subsets with the desired size.
		Set<Abducibles> listAbducibles = abducibles.getSubsetsOfSize(experimentBlock.getCurrentSizeExperiment());
		
		//For each subset add negative abducibles 
		//for all the abnormal predicates of each
		//element in the original subset.
		for(Abducibles abduciblesOfBlock: listAbducibles) {

			Set<Clause> abduciblesWithoutAbnormal = abduciblesOfBlock.getAbduciblesAsSet();
			Set<Clause> abduciblesWithAbnormal = abduciblesOfBlock.getAbduciblesAsSet();
			
			for(Clause abd: abduciblesWithoutAbnormal) {
				Atom atom = abd.getHeadAtom();
				int objectRef = atom.getObjectRef();
				Set<Predicate> abnPredicates = atom.getPredicate().getRelatedAbnormalities();
				
				for(Predicate p: abnPredicates) {
					abduciblesWithAbnormal.add(new Clause(new Atom(p, objectRef), ClauseType.ASSUMPTION));
				}
			}
			
			abduciblesOfBlock.setAbducibles(abduciblesWithAbnormal);
			
			
			listForExperiment.add(new Experiment(abduciblesOfBlock, experimentBlock.getObservations()));
		}
		
		
		return listForExperiment;
	}
	
	@Override
	public String description() {
		return "\n[Strategy - Experiments - ExperimentAbduciblesSubsetWithAbdnormalities] \n"
				+ "For each abducible with head atom 'a(o)' add clauses with abnormalities "
				+ "related to that atom in the head and body 'f'";
	}

}
