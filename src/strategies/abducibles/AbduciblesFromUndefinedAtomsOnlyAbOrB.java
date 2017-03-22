package strategies.abducibles;

import java.util.HashSet;
import java.util.Set;

import Program.Clause;
import Program.ClauseType;
import abduction.Abducibles;
import common.Atom;
import common.Predicate;
import Program.Program;

public class AbduciblesFromUndefinedAtomsOnlyAbOrB implements StrategyAbducibles {

	@Override
	public Abducibles getAbducibles(Program program) {
		Set<Atom> defAtoms = program.getProgramHeadAtoms();
		Set<Atom> allPossibleAtoms = program.allPossibleAtoms();
		
		//Creates a set of Abducibles with atoms that do not occur in the program.
		//Excludes atoms with abnormal predicates.
		//Atoms are added as both positive and negative Abducibles.
		Set<Clause> abducibles = new HashSet<Clause>();
		for(Atom a: allPossibleAtoms) {
			if(!defAtoms.contains(a)) {
				Predicate p = a.getPredicate();
				if(p.isAbnormalPredicate() || p.equals(Predicate.B)) {
					abducibles.add(new Clause(a, ClauseType.FACT));
					abducibles.add(new Clause(a, ClauseType.ASSUMPTION));
				}
			}
		}
			
		return new Abducibles(abducibles);
		
	}
	
	@Override
	public String description() {
		return "\n[Strategy - Abducibles - "+ this.getClass().getName() +"] \n"
				+ "Abdducibles are logic program clauses."
				+ "\nFor each atom 'a' that do not occur in the head of a rule (undefined atom) "
				+ "we add abducibles of the form: a:-t and a:-f."
				+ "\nWe ONLY consider abnormality and B atoms to build the abducibles.";
	}

}
