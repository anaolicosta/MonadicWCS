package strategies.blocks;

import java.util.List;

import experiments.ExperimentBlock;
import strategies.Strategy;
import syllogisms.Syllogism;
import syllogisms.SyllogismEnum;

public interface StrategyExperimentBlocks extends Strategy{
	public List<ExperimentBlock> getExperimentBlocks(final Syllogism syllogism);
	public void runExperiments(SyllogismEnum syllogismKey, ExperimentBlock experimentBlock, final int numberBlock) ;
	
}
