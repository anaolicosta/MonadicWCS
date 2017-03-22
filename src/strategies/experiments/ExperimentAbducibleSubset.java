package strategies.experiments;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import abduction.Abducibles;
import experiments.Experiment;
import experiments.ExperimentBlock;

public class ExperimentAbducibleSubset implements StrategyExperiments {

	@Override
	public List<Experiment> getExperiments(ExperimentBlock experimentBlock) {
		Abducibles abducibles = experimentBlock.getCurrentAbducibles();
		List<Experiment> listForExperiment = new ArrayList<Experiment>();

		Set<Abducibles> listAbducibles = abducibles.getSubsetsOfSize(experimentBlock.getCurrentSizeExperiment());
		
		for(Abducibles abd: listAbducibles) {
			listForExperiment.add(new Experiment(abd, experimentBlock.getObservations()));
		}
		
		
		return listForExperiment;
	}

	@Override
	public String description() {
		return "\n[Strategy - Experiments - ExperimentAbducibleSubset] \n"
				+ "Create an experiment for each subset of experiment block abducibles.";
	}

}
