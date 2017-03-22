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

public class BlocksSamePredicateObservation extends BlocksRunnable {

	@Override
	public List<ExperimentBlock> getExperimentBlocks(Syllogism syllogism) {
		List<ExperimentBlock> experimentBlocks = new ArrayList<ExperimentBlock>();
		
		Program program = syllogism.getProgram();
		List<Clause> originalClauses = program.getClauses();
		
		Set<Atom> syllTrueObservations = new HashSet<Atom>();
		
		Set<Atom> syllTrueAObservations = new HashSet<Atom>();
		Set<Atom> syllTrueBObservations = new HashSet<Atom>();
		Set<Atom> syllTrueCObservations = new HashSet<Atom>();
		
		
		List<Clause> newAClauses = new ArrayList<Clause>();
		newAClauses.addAll(originalClauses);
		List<Clause> newBClauses = new ArrayList<Clause>();
		newBClauses.addAll(originalClauses);
		List<Clause> newCClauses = new ArrayList<Clause>();
		newCClauses.addAll(originalClauses);


		
		for(Clause clause: originalClauses) {
			
			
			if(program.isClauseForObservations(clause)) {
				
				Atom atomInHead = clause.getHeadAtom();
				syllTrueObservations.add(atomInHead);
				
				Predicate aPredicate = atomInHead.getPredicate();
				
				if(aPredicate.equals(Predicate.A)) {
					syllTrueAObservations.add(atomInHead);
					newAClauses.remove(clause);
				}
				
				if(aPredicate.equals(Predicate.B)) {
					syllTrueBObservations.add(atomInHead);
					newBClauses.remove(clause);
				}
				
				if(aPredicate.equals(Predicate.C)) {
					syllTrueCObservations.add(atomInHead);
					newCClauses.remove(clause);
				}
			}
		}
		
		if(!newAClauses.isEmpty())
			experimentBlocks.add(creatExperimenBlock(newAClauses,syllTrueAObservations));
		
		if(!newBClauses.isEmpty())
			experimentBlocks.add(creatExperimenBlock(newBClauses,syllTrueBObservations));
		
		if(!newCClauses.isEmpty())
			experimentBlocks.add(creatExperimenBlock(newCClauses,syllTrueCObservations));
		
		
		return experimentBlocks;
	}
	
	
	private ExperimentBlock creatExperimenBlock(List<Clause> newClauses, Set<Atom> trueObservations) {
		//Create observation for a Experiment Block.
		Observations observations = new Observations(trueObservations);
		
		//Create new program without the existential import.
		Program newProgram = new Program(newClauses);
		
		//Get Abducibles for this experiment block
		//according to the new program built above.
		Abducibles abducibles = StrategiesAbducibles.getAbducibles(newProgram);
		
		Set<Integer> objReferences = observations.getObjectReferences();
		Set<Clause> newAbducibles =  new HashSet<Clause>();

		for(Clause a: abducibles.getAbduciblesAsSet()) {
			Atom atomAbd = a.getHeadAtom();
			//Exclude observation
			if(!trueObservations.contains(atomAbd)) {
				if(objReferences.contains(atomAbd.getObjectRef())) 
					newAbducibles.add(a);
			}
		}
					
		
		return new ExperimentBlock(observations, newProgram, new Abducibles(newAbducibles));
	}
		
	
	
	@Override
	public String description() {
		//TODO!
		return "\n[Strategy - Experiment Blocks - " + this.getClass().getName() +"] \n"
				+ generalBlocksDescription()
				+ "Specific implementation:"
				+ "\n> Each block consider observations with the same predicate."
				+ "\n> Use program without observation related clauses to generate abducibles."
				;
	}
	


}
