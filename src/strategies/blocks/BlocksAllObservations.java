package strategies.blocks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import abduction.Observations;
import common.Atom;
import experiments.ExperimentBlock;
import program.Clause;
import program.Program;
import strategies.StrategiesAbducibles;
import syllogisms.Syllogism;

public class BlocksAllObservations extends BlocksRunnable {

	@Override
	public List<ExperimentBlock> getExperimentBlocks(Syllogism syllogism) {
		List<ExperimentBlock> experimentBlocks = new ArrayList<ExperimentBlock>();
		
		Program program = syllogism.getProgram();
		List<Clause> originalClauses = program.getClauses();
		
		
		Set<Atom> syllTrueObservations = new HashSet<Atom>();
		//Create new program without rule that created observations.
		List<Clause> newClauses = new ArrayList<Clause>();
		newClauses.addAll(originalClauses);
		
		for(Clause clause: originalClauses) {

			if(program.isClauseForObservations(clause)) {
				syllTrueObservations.add(clause.getHeadAtom());
				newClauses.remove(clause);
			}
		}
		
		Program newProgram = new Program(newClauses);
		//Create Experiment Block and add it to the list.
		experimentBlocks.add(new ExperimentBlock(new Observations(syllTrueObservations), 
				newProgram, 
				StrategiesAbducibles.getAbducibles(newProgram)));

		
		return experimentBlocks;
	}
	
	@Override
	public String description() {
		return "\n[Strategy - Experiment Blocks - "+ this.getClass().getName() +"] \n"
				+ generalBlocksDescription()
				+ "Specific implementation:"
				+ "\n> An experiment block aggregates all the experiments of all observations."
				+ "\n> Use program without observation related claused to generate abducibles."
				+ "\n> All observations are considered together."
				+ "\n> NOTE: only FOUNDED EXISTENTIAL IMPORTS are considered as observations.";
		
	}
	

}

