package output;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entailment.Conclusions;
import experiments.Experiment;
import experiments.ExperimentBlock;
import syllogisms.Syllogism;
import syllogisms.SyllogismEnum;

/**
 * Implements logic specific to printing of Syllogisms' results.
 * <br />
 * 
 * It has an attribute of type {@link PrintResults} to allow to print the results.
 * 
 * 
 * @author Ana Oliveira da Costa
 * @since 2016
 */
public class SyllogismPrintable implements PrintableResults {
	
	private PrintResults printResults;

	public SyllogismPrintable(final PrintResults printResults) {
		this.printResults = printResults;
	}


	@Override
	public void printInitialData() {
		int nvcWcsConclusions = 0;
		int nvcPeopleConclusions = 0;
		int nvcMatch = 0;
		int nvcWithOthers = 0;
		
		printResults.println("\n\nRESULTS BEFORE ABDUCTION \n");
		
		Set<Syllogism> missingNVC = new HashSet<Syllogism>();
		Set<Syllogism> wrongNVC = new HashSet<Syllogism>();
		
		for (SyllogismEnum syllogismKey : SyllogismEnum.values()) {
			Syllogism syllogism = Syllogism.getSyllogism(syllogismKey);
			
			printResults.print("[" + syllogismKey + ": ");
			printResults.print(String.format( "%.2f",syllogism.getPrecisionOriginalProgram())  + "] ");
			
			printResults.println("Participants:" + syllogismKey.peopleConclusions.toString() + " \t WCS: "
					+ syllogism.getmodelAndEntailment().getConclusions().toString());

			final boolean hasNVCWcs = syllogism.getmodelAndEntailment().getConclusions().hasNVC();
			final boolean hasNVCPeople = syllogismKey.peopleConclusions.hasNVC();

			if (hasNVCWcs)
				nvcWcsConclusions++;
			if (hasNVCPeople)
				nvcPeopleConclusions++;
			if (hasNVCWcs && hasNVCPeople)
				nvcMatch++;

			if (hasNVCPeople && !hasNVCWcs)
				missingNVC.add(syllogism);

			if (!hasNVCPeople && hasNVCWcs)
				wrongNVC.add(syllogism);

			if (hasNVCPeople && syllogismKey.peopleConclusions.hasConclusionNotNVC()) nvcWithOthers++;
		}
		
		String finalPrecision = String.format("%.4f", Syllogism.originalOverallPrecision);
		printResults.println("\n> Precision = " + finalPrecision);
		printResults.println("> Number NVC in the study = " + nvcPeopleConclusions);
		printResults.println("> Number Weak NVC in the study = " + nvcWithOthers);
		printResults.println("> Our encoding number of NVC = " + nvcWcsConclusions);
		printResults.println("> Matching NVC answers between study and WCS = " + nvcMatch);
		printResults.print("> Syll with NVC missing in WCS prediction: ");
		missingNVC.forEach(
				s -> printResults.print("\'" + s.getSyllogismName().toString().toLowerCase() + "\', "));
		printResults.print("\n> Syll with Wrong NVC in WCS prediction: ");
		wrongNVC.forEach(
				s -> printResults.print("\'" + s.getSyllogismName().toString().toLowerCase() + "\', "));
		
		printResults.println("\n\nEXPERIMENTS RESULTS \n");
	}

	
	public void printOverallResults(){	
		
		for (SyllogismEnum syllogismKey : SyllogismEnum.values()) {
			Syllogism syllogism = Syllogism.getSyllogism(syllogismKey);
			
			printResults.print("[" + syllogismKey + "] ");
			
			printResults.print("Participants:" + syllogismKey.peopleConclusions.toString()  
					+ " \t WCS [");
			
			if(syllogism.isToTestAbduction())
				printResults.println(String.format("%.2f", syllogism.skepticalAccuracy()) +  "]: "
						+ syllogism.getSkepticalConclusions().toString() );
			else printResults.println(String.format("%.2f", syllogism.getPrecisionOriginalProgram()) +  "]: "
					+ syllogism.getmodelAndEntailment().getConclusions().toString() );

					
			
		}
		
		String finalPrecision = String.format( "%.6f", Syllogism.credoulousOverallPrecision);
		this.printResults.print("\n\nCredoulous overall accuracy ");
		this.printResults.println(finalPrecision);
		
		finalPrecision = String.format( "%.6f", Syllogism.SkepticalOverallPrecision);
		this.printResults.print("Skeptical overall accuracy ");
		this.printResults.println(finalPrecision);
		
	}
	
	
	
	public void printSummaryStep(final Syllogism syllogism, final SyllogismEnum syllogismKey) {
		
		final boolean testedAbduction = syllogism.isToTestAbduction();
		String precision = String.format("%.2f", syllogism.getPrecisionOriginalProgram());
		this.printResults.println("\n[" + syllogismKey.toString() + 
				"] Previous accuracy: " + precision);
		
		
		if(testedAbduction) {
			
			List<ExperimentBlock> expBlocks = syllogism.getExperimentBlocks(); 
			int sizeExpBlocks = expBlocks.size();
						
			for(int i = 0; i < sizeExpBlocks; i++) {
				this.printResults.println("-> Experiment block #" + i);
				ExperimentBlock experimentBlock = expBlocks.get(i);
				
				printSummaryBlock(experimentBlock);
			}
			
			Conclusions cConclusions = syllogism.getCredoulousConclusions();
			Conclusions sConclusions = syllogism.getSkepticalConclusions();
			
			if(!(cConclusions.isEmpty() && sConclusions.isEmpty())) {

				double accuracyCredoulous = syllogism.credoulousAccuracy();
				double accuracySkeptical = syllogism.skepticalAccuracy();

				this.printResults.println("\n\tResults of abduction (observations were entailed): ");
				this.printResults.println("\tOriginal answers: " + syllogismKey.peopleConclusions.toString());
				this.printResults.println("\tCredoulous answers: "+ cConclusions.toString());
				this.printResults.println("\tCredoulous Accuracy: " + 
						((accuracyCredoulous == 0)? "-" : String.format("%.4f", accuracyCredoulous)));
				this.printResults.println("\tSkeptical answers: "+ sConclusions.toString());
				this.printResults.println("\tSkeptical Accuracy: " + 
						((accuracySkeptical == 0)? "-" : String.format("%.4f", accuracySkeptical)));
			}
		}
		
	}
	
	
	private void  printSummaryBlock(final ExperimentBlock expBlock) {
		final List<Experiment> experiments = expBlock.getSuccessExperiments();
		this.printResults.println("- Observations: " + expBlock.getObservations().toString());
		
		for(Experiment e: experiments) {

			if (e.areObservationsEntailed()) {

				this.printResults.println("--> Succesful Experiment");
				this.printResults.print("-- Abducibles: " + e.getAbducibles().toString() + "\n");
				// this.mainDescriptionFile.println("-- Model: " +
				// e.getModelAndEntailment().getInterpretation().toString()
				// +"\n");
				this.printResults.println("-- Conclusions: " + e.getModelAndEntailment().getConclusions().toString()
						+ ". Accuracy: " + String.format("%.4f", e.getAccuracy()));
			}
		}
		
	}
	
	

}
