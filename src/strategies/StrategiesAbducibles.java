package strategies;

import abduction.Abducibles;
import Program.Program;
import main.Main;
import strategies.abducibles.AbduciblesFromUndefinedAtoms;
import strategies.abducibles.AbduciblesFromUndefinedAtomsAndAssumptions;
import strategies.abducibles.AbduciblesFromUndefinedAtomsOnlyAbOrB;
import strategies.abducibles.StrategyAbducibles;

public class StrategiesAbducibles {
	
	private static StrategyAbducibles currentStrategy;
	
	
	private static StrategyAbducibles getStrategy() {
		if(currentStrategy == null) {
			String nameStrategy = Main.cp.getStrategiesAbducibles();
			switch (nameStrategy) {
			case "AbduciblesFromUndefinedAtoms":
				currentStrategy = new AbduciblesFromUndefinedAtoms();
				break;

			case "AbduciblesFromUndefinedAtomsOnlyAbOrB":
				currentStrategy = new AbduciblesFromUndefinedAtomsOnlyAbOrB();
				break;

			case "AbduciblesFromUndefinedAtomsAndAssumptions":
				currentStrategy = new AbduciblesFromUndefinedAtomsAndAssumptions(); 
				break;
				
			default:
				currentStrategy = new AbduciblesFromUndefinedAtoms();
				break;
			}
		}
			
		return currentStrategy;
	}
	
	public static Abducibles getAbducibles(final Program program) {
		return getStrategy().getAbducibles(program);
	}
	
	public static String getDescription() {
		return getStrategy().description();
	}
	
	
	
	
	//To keep it singleton
	private StrategiesAbducibles(){}
	
	
//	public static Abducibles getAbduciblesFromUndefinedAtomsWithAbFalse(final Program program) {
//		
//		Set<Atom> defAtoms = program.getProgramHeadAtoms();
//		Set<Atom> allPossibleAtoms = program.allPossibleAtoms();
//		
//		
//		//Creates a set of Abducibles with atoms that do not occur in the program.
//		//Atoms that do are not abnormalities are added as both positive and negative Abducibles.
//		//Abornaml atoms are added only as negative Abducibles.
//		Set<Abducible> abducibles = new HashSet<Abducible>();
//		for(Atom a: allPossibleAtoms) {
//			if(!defAtoms.contains(a)) {
//				if(!a.getPredicate().isAbnormalPredicate()) {
//					abducibles.add(new Abducible(a, true));
//				}
//				abducibles.add(new Abducible(a, false));
//			}
//		}	
//		return new Abducibles(abducibles);
//	}

}
