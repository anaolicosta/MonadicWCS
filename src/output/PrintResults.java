package output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import main.Main;
import strategies.StrategiesAbducibles;
import strategies.StrategiesExperiment;
import strategies.StrategiesExperimentBlocks;

public class PrintResults {
	
	private String descriptionFilePath;
	private PrintWriter descriptionFileWriter;

	private String outputType;
	
	/**
	 * Creates a file to write the results.
	 * <br />
	 * If the file already exists, then file is deleted before creating a new one.
	 * 
	 * @param descriptionFilePath Path for the file with description
	 * @param outputType File output
	 */
	public PrintResults(final String descriptionFilePath, final String outputType) {
		this.descriptionFilePath = descriptionFilePath;
		this.outputType = outputType;
		
		File dFile = new File(this.descriptionFilePath);
		if(dFile.exists())
			dFile.delete();

		try {
			FileWriter fw = new FileWriter(this.descriptionFilePath, true);
			BufferedWriter bw = new BufferedWriter(fw);
			this.descriptionFileWriter = new PrintWriter(bw);		
		} catch (IOException e) {
			System.err.println("Failed to create description file: " + e.getMessage());
		}
	}
	
	
	public PrintWriter getDescriptionFileWriter() {
		return descriptionFileWriter;
	}
	

	public String getOutputType() {
		return outputType;
	}


	public void println(final String line){
		this.descriptionFileWriter.println(line);
	}
	
	public void print(final String line){
		this.descriptionFileWriter.print(line);
	}
	
	public void closeDescriptionFile() { this.descriptionFileWriter.close(); }
	
	/**
	 * Header for description file:
	 * <ul>
	 * <li> Description of strategies to create experiment blocks,
	 * experiments, abducibles, ...
	 * <li> pattern used for tests.
	 */
	public void printInitialDescription() {
		this.descriptionFileWriter.println("TEST PARAMETERS\n");
		
		this.descriptionFileWriter.println("\nEntailment criteria: " + Main.cp.getEntailment());

		this.descriptionFileWriter.println("\nMinimality criteria: " + Main.cp.getMinimalityCriteria());
		this.descriptionFileWriter.println("\nObservations criteria: " + Main.cp.getObservationsCriteria());

		this.descriptionFileWriter.println(StrategiesExperimentBlocks.getDescription());
		this.descriptionFileWriter.println(StrategiesAbducibles.getDescription());
		this.descriptionFileWriter.println(StrategiesExperiment.getDescription());
	}

}
