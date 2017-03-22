package strategies.blocks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import abduction.Abducibles;
import abduction.Observations;
import common.Atom;
import common.Predicate;
import experiments.ExperimentBlock;
import program.Clause;
import program.Program;
import strategies.StrategiesAbducibles;
import syllogisms.Syllogism;

public class BlocksSingleObservation extends BlocksRunnable {

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
				
				//Create observation for a Experiment Block.
				Observations observations = new Observations(trueObservation);
				
				//Create new program without the clause
				List<Clause> newClauses = new ArrayList<Clause>();
				newClauses.addAll(originalClauses);
				newClauses.remove(clause);
				Program newProgram = new Program(newClauses);
				
				//Get Abducibles for this experiment block
				//according to the new program built above.
				Abducibles abducibles = StrategiesAbducibles.getAbducibles(newProgram);
				
				Set<Clause> newAbducibles =  new HashSet<Clause>();
				int obsObjeRef = atomInHead.getObjectRef();
				Predicate obsPredicate  = atomInHead.getPredicate();
				
				Set<Integer> objReferences = observations.getObjectReferences();

				
				for(Clause a: abducibles.getAbduciblesAsSet()) {
					Atom atomAbd = a.getHeadAtom();
					//Exclude observation
					if(!(atomAbd.getPredicate().equals(obsPredicate) && atomAbd.getObjectRef() == obsObjeRef)) {
						//Only atoms with the same object
						if(objReferences.contains(atomAbd.getObjectRef())) 
							newAbducibles.add(a);
					}
				}
								
				//Create Experiment Block and add it to the list.
				experimentBlocks.add(new ExperimentBlock(observations, newProgram, new Abducibles(newAbducibles)));
			}
		}

		//syllogism.setObservations(new Observations(syllTrueObservations));
		
		return experimentBlocks;
	}
	
	@Override
	public String description() {
		return "\n[Strategy - Experiment Blocks - " + this.getClass().getName() +"] \n"
				+ generalBlocksDescription()
				+ "Specific implementation:"
				+ "\n> Each block has only one observation"
				+ "\n> Only abducibles with the same object as the observation."
				+ "\n> Does not consider the current observation as a possible abducible."
				;
	}
	

}
