package strategies.abducibles;

import abduction.Abducibles;
import program.Program;
import strategies.Strategy;

public interface StrategyAbducibles extends Strategy {
	public Abducibles getAbducibles(final Program program);
}
