package main;

import java.io.IOException;
import java.nio.file.Paths;

import experiments.ExperimentManager;
import output.PrintResultCsv;
import output.PrintResultDescription;
import output.PrintResults;

public class Main {
	
	public static String PATH = ""; 
	public static ConfigurableProperties cp;
	
	//TODO: consider changing this
	public static PrintResults printCsv;
	
	private static String[] patterns;

	public static void main(String[] args) {
		
		
		cp = new ConfigurableProperties();
		PATH = cp.getPathValue();
		System.out.println("[Info]\tPath = " + PATH);
		
		printCsv = new PrintResultCsv("results.csv");
		printCsv.print("Pattern");
		printCsv.print("Minimality");
		printCsv.print("Entailment");
		printCsv.print("Observations");
		printCsv.print("Abducibles");
		printCsv.print("Before Abduction");
		printCsv.print("After [Credolous]");
		printCsv.print("After [Skeptical]");
		printCsv.print("Improvement [Credolous]");
		printCsv.println("Improvement [Skeptical]");


		patterns = cp.getPatterns();
		
			
		try {
						
			for(int i=0; i < patterns.length; i++) {
				System.out.println("[Info]\tPattern = " + patterns[i]);
				new ExperimentManager(patterns[i]);
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("[Abducibles]\tTests.");
			
		printCsv.closeDescriptionFile();
	}
	

}
