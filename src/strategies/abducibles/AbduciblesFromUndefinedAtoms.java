package strategies.abducibles;

import java.util.HashSet;
import java.util.Set;

import abduction.Abducibles;
import common.Atom;
import Program.Clause;
import Program.ClauseType;
import Program.Program;

public class AbduciblesFromUndefinedAtoms implements StrategyAbducibles {

	@Override
	public Abducibles getAbducibles(Program program) {
		
		Set<Atom> defAtoms = program.getProgramHeadAtoms();
		Set<Atom> allPossibleAtoms = program.allPossibleAtoms();
		
		//Creates a set of Abducibles with atoms that do not occur in the program.
		//Atoms are added as both positive and negative Abducibles.
		Set<Clause> abducibles = new HashSet<Clause>();
		for(Atom a: allPossibleAtoms) {
			if(!defAtoms.contains(a) ) {
				abducibles.add(new Clause(a, ClauseType.FACT));
				abducibles.add(new Clause(a, ClauseType.ASSUMPTION));
			}
		}
			
		return new Abducibles(abducibles);
	}

	@Override
	public String description() {
		return "\n[Strategy - Abducibles - AbduciblesFromUndefinedAtoms] \n"
				+ "Abdducibles are logic program clauses."
				+ "\nFor each atom 'a(o)' that do not occur in the head of a rule (undefined atom) "
				+ "we add abducibles of the form: a(o):-t and a(o):-f.";
	}

}
