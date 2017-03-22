package strategies.blocks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import abduction.Abducibles;
import abduction.Observations;
import common.Atom;
import common.Predicate;
import Program.Clause;
import Program.Program;
import experiments.ExperimentBlock;
import strategies.StrategiesAbducibles;
import syllogisms.Syllogism;

public class BlocksSingleObservationWithAbnormalFalse extends BlocksRunnable {

	@Override
	public List<ExperimentBlock> getExperimentBlocks(Syllogism syllogism) {
		List<ExperimentBlock> experimentBlocks = new ArrayList<ExperimentBlock>();
		
		Program program = syllogism.getProgram();
		List<Clause> originalClauses = program.getClauses();
		
		Set<Atom> syllTrueObservations = new HashSet<Atom>();
		
		for(Clause clause: originalClauses) {
			
			if(program.isClauseForObservations(clause)) {
				
				Atom atomInHead = clause.getHeadAtom();
				
				//True observations for an Experiment Block:
				//This set will have only one element
				Set<Atom> trueObservation = new HashSet<Atom>();
				trueObservation.add(atomInHead);
				
				syllTrueObservations.add(atomInHead);
				
				//Each Experiment Block will have as false observations
				//all the abnormal atoms that are related with
				//the atom in the trueObservation single Set.
				Set<Atom> falseObservations = new HashSet<Atom>();
				Predicate aPredicate = atomInHead.getPredicate();
				int aObjRef = atomInHead.getObjectRef();
				for(Predicate p: aPredicate.getRelatedAbnormalities()) {
					falseObservations.add(new Atom(p,aObjRef));
				}
				
				//Create observation for a Experiment Block.
				Observations observations = new Observations(trueObservation);
				
				//Create new program without the existential import.
				List<Clause> newClauses = new ArrayList<Clause>();
				newClauses.addAll(originalClauses);
				newClauses.remove(clause);
				Program newProgram = new Program(newClauses);
				
				//Get Abducibles for this experiment block
				//according to the new program built above.
				Abducibles abducibles = StrategiesAbducibles.getAbducibles(newProgram);
				
				//Create Experiment Block and add it to the list.
				experimentBlocks.add(new ExperimentBlock(observations, newProgram, abducibles));
			}
		}

		//syllogism.setObservations(new Observations(syllTrueObservations));
		
		return experimentBlocks;
	}
	
	@Override
	public String description() {
		return "\n[Strategy - Experiment Blocks - BlocksSingleObservationWithAbnormalFalse] \n"
				+ generalBlocksDescription()
				+ "Specific implementation:"
				+ "\n> Each block has only one \'true' observation. "
				+ "\n> Each block have as false observations all the abnormal atoms that are related"
				+ "the true observation"
				;
	}
	

}
