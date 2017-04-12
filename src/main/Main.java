package main;

import java.io.IOException;

import entailment.Conclusions;
import experiments.ExperimentManager;
import output.PrintResultCsv;
import output.PrintResults;
import syllogisms.Syllogism;
import syllogisms.SyllogismEnum;

public class Main {
	
	public static String PATH = ""; 
	public static ConfigurableProperties cp;
	
	public static PrintResults printCsvAccuracyForAllPatterns;
	public static PrintResults printCsvAnswersForAllPatterns;

	
	private static String[] patterns;
	public static String currentPattern;

	public static void main(String[] args) {
		
		
		cp = new ConfigurableProperties();
		PATH = cp.getPathValue();
		System.out.println("[Info]\tPath = " + PATH);
		
		printCsvAccuracyForAllPatterns = new PrintResultCsv("accuracy.csv");
		printCsvAccuracyForAllPatterns.print("Pattern");
		printCsvAccuracyForAllPatterns.print("Minimality");
		printCsvAccuracyForAllPatterns.print("Entailment");
		printCsvAccuracyForAllPatterns.print("Observations");
		printCsvAccuracyForAllPatterns.print("Abducibles");
		printCsvAccuracyForAllPatterns.print("Before Abduction");
		printCsvAccuracyForAllPatterns.print("After [Credulous]");
		printCsvAccuracyForAllPatterns.print("After [Skeptical]");
		printCsvAccuracyForAllPatterns.print("Improvement [Credulous]");
		printCsvAccuracyForAllPatterns.println("Improvement [Skeptical]");
		
		printCsvAnswersForAllPatterns = new PrintResultCsv("answers.csv");
		printCsvAnswersForAllPatterns.print("Pattern");
		printCsvAnswersForAllPatterns.print("Syllogism");
		printCsvAnswersForAllPatterns.print("P. Answers");
		printCsvAnswersForAllPatterns.print("[C]Before Ab.");
		printCsvAnswersForAllPatterns.print("[C]Credulous");
		printCsvAnswersForAllPatterns.print("[C]Skeptical");
		printCsvAnswersForAllPatterns.print("[A]Before Ab.");
		printCsvAnswersForAllPatterns.print("[A]Credulous");
		printCsvAnswersForAllPatterns.println("[A]Skeptical");
		


		patterns = cp.getPatterns();
		
			
		try {		
			for(int i=0; i < patterns.length; i++) {
				System.out.println("[Info]\tPattern = " + patterns[i]);
				currentPattern = patterns[i];
				new ExperimentManager(currentPattern);
				printAccuracyCsv(currentPattern);
				printAnswersCsv(currentPattern);
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("[Abducibles]\tTests.");
			
		printCsvAccuracyForAllPatterns.closeDescriptionFile();
		printCsvAnswersForAllPatterns.closeDescriptionFile();

	}
	
	
	
	private static void printAccuracyCsv(final String pattern) {
		printCsvAccuracyForAllPatterns.print(pattern);
		printCsvAccuracyForAllPatterns.print(Main.cp.getMinimalityCriteria());
		printCsvAccuracyForAllPatterns.print(Main.cp.getEntailment());
		printCsvAccuracyForAllPatterns.print(Main.cp.getObservationsCriteria());
		printCsvAccuracyForAllPatterns.print(Main.cp.getStrategiesAbducibles());
		printCsvAccuracyForAllPatterns.print(String.format("%.6f", Syllogism.originalOverallPrecision));
		printCsvAccuracyForAllPatterns.print(String.format("%.6f", Syllogism.credoulousOverallPrecision));
		printCsvAccuracyForAllPatterns.print(String.format("%.6f", Syllogism.SkepticalOverallPrecision));
		printCsvAccuracyForAllPatterns.print(
				String.format("%.6f", Syllogism.credoulousOverallPrecision - Syllogism.originalOverallPrecision));
		printCsvAccuracyForAllPatterns.println(
				String.format("%.6f", Syllogism.SkepticalOverallPrecision - Syllogism.originalOverallPrecision));
	}
	
	private static void printAnswersCsv(final String pattern) {
	
		for (SyllogismEnum syllogismKey : SyllogismEnum.values()) {
			
			Syllogism syllogism = Syllogism.getSyllogism(syllogismKey);
			printCsvAnswersForAllPatterns.print(pattern);
			printCsvAnswersForAllPatterns.print(syllogismKey.toString());
						
			printCsvAnswersForAllPatterns.print(syllogismKey.peopleConclusions.toString());
			
			String originalC = syllogism.getmodelAndEntailment().getConclusions().toString();
			String originalA = String.format("%.6f", syllogism.getPrecisionOriginalProgram());
					
			printCsvAnswersForAllPatterns.print(originalC);


			
			if(syllogism.isToTestAbduction()) {
				Conclusions conclusions = syllogism.getCredoulousConclusions();
				printCsvAnswersForAllPatterns.print(conclusions.toString());
				String accuracyC = String.format("%.6f", syllogism.evaluateAccuracy(conclusions));

				conclusions = syllogism.getSkepticalConclusions();
				printCsvAnswersForAllPatterns.print(conclusions.toString());
				String accuracyS = String.format("%.6f", syllogism.evaluateAccuracy(conclusions));
				
				
				printCsvAnswersForAllPatterns.print(originalA);
				printCsvAnswersForAllPatterns.print(accuracyC);
				printCsvAnswersForAllPatterns.println(accuracyS);
			} else {
				printCsvAnswersForAllPatterns.print(originalC);
				printCsvAnswersForAllPatterns.print(originalC);
				printCsvAnswersForAllPatterns.print(originalA);
				printCsvAnswersForAllPatterns.print(originalA);
				printCsvAnswersForAllPatterns.println(originalA);

			}
			

					
			
		}
		
	}

}
