package strategies.experiments;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import abduction.Abducibles;
import abduction.Observations;
import experiments.Experiment;
import experiments.ExperimentBlock;
import program.Clause;
import program.ClauseType;

public class ExperimentAbduciblesFilter implements StrategyExperiments {

	@Override
	public List<Experiment> getExperiments(ExperimentBlock experimentBlock) {
		Abducibles abducibles = experimentBlock.getCurrentAbducibles();
		List<Experiment> listForExperiment = new ArrayList<Experiment>();
			
		Set<Abducibles> listAbducibles = abducibles.getSubsetsOfSize(experimentBlock.getCurrentSizeExperiment());
	
		if(!listAbducibles.isEmpty()) {	
			listAbducibles = removeContradictingAducibles(listAbducibles);
			listAbducibles = allAbduciblesHaveOccurenceOfObjects(listAbducibles, experimentBlock.getObservations());
			for(Abducibles abd: listAbducibles) {
				listForExperiment.add(new Experiment(abd, experimentBlock.getObservations()));
			}
		} 
		
		return listForExperiment;
	}
	
	
	private Set<Abducibles> allAbduciblesHaveOccurenceOfObjects(final Set<Abducibles> listAbducibles, 
			final Observations observations) {
		
		Set<Integer> objReferences = observations.getObjectReferences();
		Set<Abducibles> filterAbducibles = new HashSet<Abducibles>();
		
		for(Abducibles abd: listAbducibles) {
			if(abd.getAllObjReferences().containsAll(objReferences))
				filterAbducibles.add(abd);
		}
		
		return filterAbducibles;
	}
	
	
	private Set<Abducibles> removeContradictingAducibles(final Set<Abducibles> listAbducibles) {
		
		Set<Abducibles> newSetAbducibles = new HashSet<Abducibles>();
		for(Abducibles abducibles: listAbducibles) {
			boolean hasContradition = false;
			Set<Clause> setAbducible = abducibles.getAbduciblesAsSet();
			for(Clause ab: setAbducible) {
				//Construct inverse
				ClauseType type = ab.isFact() ? ClauseType.ASSUMPTION : ClauseType.FACT;
				Clause invAbd = new Clause(ab.getHeadAtom(), type);
				hasContradition |= setAbducible.contains(invAbd);
			}
			
			if(!hasContradition) {
				newSetAbducibles.add(abducibles);
			}
		}
		
		
		return newSetAbducibles;
		
	}

	@Override
	public String description() {
		return "\n[Strategy - Experiments - " + this.getClass().getName() +"] \n"
				+ "Create an experiment for each subset of experiment block abducibles.\n"
				+ "Remove abducibles that contain a contradiction.\n"
				+ "Size of subset is determined by the current size set on the experiment block."
				;
	}

}
