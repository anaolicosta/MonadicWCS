package strategies.experiments;

import java.util.List;

import experiments.Experiment;
import experiments.ExperimentBlock;
import strategies.Strategy;

public interface StrategyExperiments extends Strategy {
	public List<Experiment> getExperiments(ExperimentBlock experimentBlock);
}
