package strategies.blocks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import abduction.Abducibles;
import abduction.Observations;
import common.Atom;
import experiments.ExperimentBlock;
import program.Clause;
import program.Program;
import strategies.StrategiesAbducibles;
import syllogisms.Syllogism;

public class BlocksAllObservationFilterAbducibles extends BlocksRunnable {

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
		
		Abducibles abducibles = StrategiesAbducibles.getAbducibles(program);
		Observations observations = new Observations(syllTrueObservations);
		
		Set<Integer> objReferences = observations.getObjectReferences();
		
		Set<Clause> filteredAbducibles = new HashSet<Clause>();
		for(Clause abducible: abducibles.getAbduciblesAsSet()) {
			if(objReferences.contains(abducible.getHeadAtom().getObjectRef())) {
				filteredAbducibles.add(abducible);
			}
		}
		
		//Create Experiment Block and add it to the list.
		experimentBlocks.add(new ExperimentBlock(observations, 
				newProgram, 
				new Abducibles(filteredAbducibles)));

		return experimentBlocks;
	}
	
	@Override
	public String description() {
		return "\n[Strategy - Experiment Blocks - "+ this.getClass().getName() +"] \n"
				+ generalBlocksDescription()
				+ "Specific implementation:"
				+ "\n> An experiment block aggregates all experiments for ALL observations in a program."
				+ "\n> Only abducibles with the same objects as observation are considered."
				+ "\n> Use orginal program to generate abducibles.";
				
	}
	

}


