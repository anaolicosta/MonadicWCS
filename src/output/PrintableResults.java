package output;

import syllogisms.Syllogism;
import syllogisms.SyllogismEnum;

public interface PrintableResults {

	public void printInitialData();

	public void printOverallResults();

	/** TODO: make parameters more general. 
	 * This should not have references to syllogism classes at all!!*/
	public void printSummaryStep(Syllogism syllogism, SyllogismEnum syllogismKey);
}
