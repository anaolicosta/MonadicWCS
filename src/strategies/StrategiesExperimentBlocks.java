package strategies;


import java.util.List;

import experiments.ExperimentBlock;
import main.Main;
import strategies.blocks.BlocksAllObservationFilterAbducibles;
import strategies.blocks.BlocksAllObservations;
import strategies.blocks.BlocksSingleObservation;
import strategies.blocks.StrategyExperimentBlocks;
import syllogisms.Syllogism;
import syllogisms.SyllogismEnum;

public class StrategiesExperimentBlocks {

	private static StrategyExperimentBlocks currentStrategy;
	
	private static StrategyExperimentBlocks getStrategy() {
		if(currentStrategy == null) {
			switch(Main.cp.getStrategiesBlocks()) {
			case "BlocksSingleObservation":
				currentStrategy = new BlocksSingleObservation(); break;
			case "BlocksAllObservationFilterAbducibles":
				currentStrategy = new BlocksAllObservationFilterAbducibles(); break;
			case "BlocksAllObservations":
				currentStrategy = new BlocksAllObservations(); break;
			default:
				currentStrategy = new BlocksSingleObservation(); break;
			}
		}
			
		return currentStrategy;
	}
	
	public static List<ExperimentBlock> getExperimentBlocks(final Syllogism syllogism) {
		//TODO: Think where to decide about the size of susbsets
		return getStrategy().getExperimentBlocks(syllogism);
	}
	
	public static String getDescription() {
		return getStrategy().description();
	}
	
	public static void runExperiments(SyllogismEnum syllogismKey, ExperimentBlock experimentBlock,
			final int numberBlock) {
		getStrategy().runExperiments(syllogismKey, experimentBlock, numberBlock);
	}
	
	//To keep it singleton
	private StrategiesExperimentBlocks(){}
	
}
