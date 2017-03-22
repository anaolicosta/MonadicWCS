package strategies;

import java.util.List;

import experiments.Experiment;
import experiments.ExperimentBlock;
import main.Main;
import strategies.experiments.ExperimentAbducibleSubset;
import strategies.experiments.ExperimentAbduciblesFilter;
import strategies.experiments.StrategyExperiments;

public class StrategiesExperiment {
	
	private static StrategyExperiments currentStrategy;
	
	private static StrategyExperiments getStrategy() {
		if(currentStrategy == null) {
			switch(Main.cp.getStrategiesExperiments()) {
			case "ExperimentAbduciblesFilter":
				currentStrategy = new ExperimentAbduciblesFilter(); break;
			case "ExperimentAbducibleSubset":
				currentStrategy = new ExperimentAbducibleSubset(); break;
			default:
				currentStrategy = new ExperimentAbducibleSubset(); break;
			}
		}
		
		return currentStrategy;
	}
	
	public static List<Experiment> getExperiments(final ExperimentBlock experimentBlock) {
		return getStrategy().getExperiments(experimentBlock);
	}
	
	public static String getDescription() {
		return getStrategy().description();
	}
	
	//To keep it singleton
	private StrategiesExperiment(){}
	
}
