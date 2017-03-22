package strategies.abducibles;

import java.util.HashSet;
import java.util.Set;

import abduction.Abducibles;
import common.Atom;
import program.Clause;
import program.ClauseType;
import program.Program;

public class AbduciblesFromUndefinedAtomsAndAssumptions implements StrategyAbducibles {
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
		
		for(Clause clause: program.getClauses()) {
			if(clause.isAssumption() && program.isOnlyClauseWithThisHead(clause)) {
				abducibles.add(new Clause(clause.getHeadAtom(), ClauseType.FACT));
			}
		}
			
		return new Abducibles(abducibles);
	}

	@Override
	public String description() {
		return "\n[Strategy - Abducibles -" + this.getClass().getName() +"] \n"
				+ "Abdducibles are logic program clauses."
				+ "\nFor each atom 'a(o)' that do not occur in the head of a rule (undefined atom) "
				+ "we add abducibles of the form: a(o):-t and a(o):-f."
				+ "\nAdditonally we add abducibles of the form 'a(o):-t' for each assumption"
				+ "(a(o):-f) that occurs in the program that cannot be rewriten by other rule.";
	}

}
