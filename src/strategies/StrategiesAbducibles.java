package strategies;

import abduction.Abducibles;
import main.Main;
import program.Program;
import strategies.abducibles.AbduciblesFromUndefinedAtomsAndAssumptions;
import strategies.abducibles.StrategyAbducibles;

public class StrategiesAbducibles {
	
	private static StrategyAbducibles currentStrategy;
	
	
	private static StrategyAbducibles getStrategy() {
		if(currentStrategy == null) {
			String nameStrategy = Main.cp.getStrategiesAbducibles();
			switch (nameStrategy) {
			case "AbduciblesFromUndefinedAtomsAndAssumptions":
				currentStrategy = new AbduciblesFromUndefinedAtomsAndAssumptions(); 
				break;
			default:
				currentStrategy = new AbduciblesFromUndefinedAtomsAndAssumptions();
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
	
}
