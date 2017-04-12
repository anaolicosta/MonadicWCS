package experiments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.Interpretation;
import entailment.Conclusion;
import entailment.Conclusions;
import main.Main;
import main.PrologConnector;
import output.PrintResultDescription;
import output.PrintResults;
import output.PrintableResults;
import output.SyllogismPrintable;
import strategies.StrategiesExperiment;
import strategies.StrategiesExperimentBlocks;
import syllogisms.SyllinProlog;
import syllogisms.Syllogism;
import syllogisms.SyllogismEnum;

public class ExperimentManager {
	
	private static final String EXPERIMENTS_BASE_DIR = "syllogisms/";
	private static int END_INDEX_SYLL_REF = 3;
	
	private PrintResults printResultsDescriptionFile;
	private PrintableResults syll_printable;
	
	public ExperimentManager(final String pattern)
			throws IOException {
		
		//Clean all the previous results and start a new set of experiments.
		Syllogism.clear();
		
		initSyllogisms(pattern);
		
		//Open description file.
		printResultsDescriptionFile = new PrintResultDescription(
				EXPERIMENTS_BASE_DIR + "description_" + pattern + ".txt");
		
		//Print initial data.
		printResultsDescriptionFile.printInitialDescription();
		
		syll_printable = new SyllogismPrintable(printResultsDescriptionFile);
		
		syll_printable.printInitialData();
		
		//Start!
		startExperiments();
		
		//Collect and print results
		collectOverallResults();
		
		//Print overall results.
		syll_printable.printOverallResults();
	
		//Close description file.
		printResultsDescriptionFile.closeDescriptionFile();
		
	}

	

	/**
	 * Read least models and programs inside Path params
	 * 'folderLeastModels' and 'folderPrograms', respectively.
	 * 
	 * @param folderLeastModels
	 * @param folderPrograms
	 * @throws IOException
	 */
	private void initSyllogisms(final String pattern) throws IOException {

		int patternA = Integer.valueOf(pattern.substring(0, 1));
		int patternI = Integer.valueOf(pattern.substring(1, 2));
		int patternE = Integer.valueOf(pattern.substring(2, 3));
		int patternO = Integer.valueOf(pattern.substring(3, 4));

		//Create original programs.
		SyllinProlog syllinProlog = new SyllinProlog();
		syllinProlog.createAllSyll(patternA, patternI, patternE, patternO);

		PrologConnector prologConnector = new PrologConnector();
		
		try { 
			prologConnector.runProlog(pattern); 
		}catch (Exception e) {
			e.printStackTrace();
		}

		
		//Read ground programs and least models.
		for (Path pathFile : Files.newDirectoryStream(Paths.get(Main.PATH + pattern))) {
			Syllogism syll = Syllogism.getSyllogism(getSyllogismKey(pathFile));
			if(pathFile.toString().endsWith("g.pl")) {
				syll.setProgram(Files.readAllLines(pathFile));	
			} else if (pathFile.toString().endsWith("glm.pl")){
				syll.setModelAndEntailment(new Interpretation(pathFile));
			}
		}
		
		
		double precision = 0.0;
		for (SyllogismEnum syllogismKey : SyllogismEnum.values()) {
			Syllogism syllogism = Syllogism.getSyllogism(syllogismKey);
			precision += syllogism.getPrecisionOriginalProgram();
		}
		
		Syllogism.originalOverallPrecision = precision/64;
		
	}


	private void startExperiments() {
				
		//We only run experiments for syllogisms that we want to test abduction.
		for (SyllogismEnum syllogismKey : SyllogismEnum.values()) {
			Syllogism syllogism = Syllogism.getSyllogism(syllogismKey);
			
			if(syllogism.isToTestAbduction()) {
				
				//Create experiment blocks.
				List<ExperimentBlock> expBlocks = StrategiesExperimentBlocks.getExperimentBlocks(syllogism);			
				syllogism.setExperimentBlocks(expBlocks);
				
				//Create first set of experiments for each block. 
				for(ExperimentBlock experimentBlock: expBlocks) {
					List<Experiment> experiments = StrategiesExperiment.getExperiments(experimentBlock);
					experimentBlock.setCurrentExperiments(experiments);
				}
				
				runSylogismExperimentBlocks(syllogism,syllogismKey);
				
				collectResultsSyllogism(syllogism, syllogismKey.peopleConclusions);
				
				syll_printable.printSummaryStep(syllogism, syllogismKey);
			}
				
		}	
		
	}
	
	public void collectOverallResults() {
		
		double cPrecision = 0;
		double sPrecision = 0;
		for (SyllogismEnum syllogismKey : SyllogismEnum.values()) {
			Syllogism syllogism = Syllogism.getSyllogism(syllogismKey);
			
			double originalPredication = syllogism.getPrecisionOriginalProgram();

			double accuracyCredoulous;
			double accuracySkeptical;
			
			if(syllogism.isToTestAbduction()) {	
				
				//collectResultsSyllogism(syllogism, syllogismKey.peopleConclusions);
	
				accuracyCredoulous = syllogism.credoulousAccuracy();
				accuracySkeptical = syllogism.skepticalAccuracy();
			} else {
				accuracyCredoulous = originalPredication;
				accuracySkeptical = originalPredication;
			}
			
			
			cPrecision += accuracyCredoulous;
			sPrecision += accuracySkeptical;
		}
		
		Syllogism.credoulousOverallPrecision = cPrecision/64;
		Syllogism.SkepticalOverallPrecision = sPrecision/64;
	
	}
	
	



	public void collectResultsSyllogism(final Syllogism syllogism, final Conclusions peopleConclusions) {
		
		final boolean testedAbduction = syllogism.isToTestAbduction();
		
		//To be sure everything is clean before evaluation.
		syllogism.getCredoulousConclusions().clearConclusions();
		syllogism.getSkepticalConclusions().clearConclusions();
		
		Conclusions credoulous = new Conclusions();
		Conclusions skeptical = new Conclusions();
		
		if(testedAbduction) {
			List<ExperimentBlock> expBlocks = syllogism.getExperimentBlocks(); 
			boolean isFirstSuccessfullBlock = true;	
			
			for(ExperimentBlock experimentBlock: expBlocks) {
				
				//Collect results for a block.
				collectResultsBlock(experimentBlock, peopleConclusions);

				if (experimentBlock.isHasSucessfullExperiments()) {
					
					credoulous.addConclusions(experimentBlock.getCredoulousConclusions());
					
					//Skeptical	
					if(isFirstSuccessfullBlock) {
						skeptical = experimentBlock.getSkepticalConclusions();
						isFirstSuccessfullBlock = false;
					} else {
						skeptical.intersectConclusions(experimentBlock.getSkepticalConclusions());
					}
				}
			}
			
			//TODO: Test for Emma
//			credoulous.addConclusion(Conclusion.NVC);
//			skeptical.addConclusion(Conclusion.NVC);
//			
			if(skeptical.isEmpty()) skeptical.addConclusion(Conclusion.NVC);
			if(credoulous.isEmpty()) credoulous.addConclusion(Conclusion.NVC);
			syllogism.setCredoulousConclusions(credoulous);
			syllogism.setSkepticalConclusions(skeptical);
			
		}
		
	}
	
	
	
	
	public void collectResultsBlock(final ExperimentBlock expBlock, final Conclusions peopleConclusions){
		
		Set<Conclusion> credoulous = new HashSet<Conclusion>();
		Set<Conclusion> skeptical = new HashSet<Conclusion>();
		
		final List<Experiment> successExperiments = expBlock.getSuccessExperiments();

		boolean isFirstSuccessfullExperiment = true;
		for(Experiment e: successExperiments) {
			
			if(e.areObservationsEntailed()) {
				Conclusions expConclusions = e.getModelAndEntailment().getConclusions();
				Set<Conclusion> setOfConclusions = expConclusions.getSetOfConclusions();
				
				e.setAccuracy(peopleConclusions.matchingConclusions(expConclusions) / 9.0);

				//Credoulous
				credoulous.addAll(setOfConclusions);
				//Skeptical
				if(isFirstSuccessfullExperiment) {
					expBlock.setHasSucessfullExperiments(true);
					skeptical.addAll(setOfConclusions);
					isFirstSuccessfullExperiment = false;
				} else {
					//TODO: check!
					skeptical.retainAll(setOfConclusions);
				}
			}
		}
		
		expBlock.setCredoulousConclusions(new Conclusions(credoulous));
		expBlock.setSkepticalConclusions(new Conclusions(skeptical));
		
	}
	
	
	
	public void runSylogismExperimentBlocks(final Syllogism syllogism, final SyllogismEnum syllogismKey) {
		
		if(syllogism.isToTestAbduction()) {
			
			System.out.println("[Start experiments] " + syllogismKey.toString());
			List<ExperimentBlock> expBlocks = syllogism.getExperimentBlocks();
			
			String syllPath = EXPERIMENTS_BASE_DIR + syllogismKey.toString() + "/";
			File theDir = new File(syllPath);
	
			if (!theDir.exists()) {
			    try{
			        theDir.mkdir();	
					//System.out.println("[Created folder for syllogism experiments] " + syllPath);
			    } catch(SecurityException se){
			        System.out.println("Failed to create dir: " + theDir.toString());
			    }          
			}
			
			//Delete old files
			File[] listFiles = theDir.listFiles();
			for(File dirExpBlocks: listFiles) {
				dirExpBlocks.delete();	
			}	
		
			for(int i = 0; i < expBlocks.size(); i++) {	
				try {
					PrintWriter writer = new PrintWriter( EXPERIMENTS_BASE_DIR + syllogismKey.toString() + 
							"/" + i + "_description.txt", "UTF-8");
					writer.println(expBlocks.get(i).toString());
					writer.close();
					
					writer = new PrintWriter( EXPERIMENTS_BASE_DIR + syllogismKey.toString() + 
							"/" + i + "_program.pl", "UTF-8");
					writer.println(expBlocks.get(i).getProgram().toString());
					writer.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				StrategiesExperimentBlocks.runExperiments(syllogismKey,expBlocks.get(i),i);
				
				List<Experiment> sExperiments = expBlocks.get(i).getSuccessExperiments();
				if(!sExperiments.isEmpty()) {
					
					System.out.println("Observation " + expBlocks.get(i).getObservations().toString()
							+ " sucess experiments with conclusions: ");
					for(Experiment e: sExperiments)
						System.out.println(e.getModelAndEntailment().getConclusions());

				}

			}
			
			//Clean created files and folder
			listFiles = theDir.listFiles();
			for(File dirExpBlocks: listFiles) {
				dirExpBlocks.delete();	
			}	
			theDir.delete();
		}
	}
	

	private SyllogismEnum getSyllogismKey(Path pathFileModel) {
		String syllogism_file = pathFileModel.getFileName().toString().
				substring(0, END_INDEX_SYLL_REF).toUpperCase();
		SyllogismEnum syllogism_key = SyllogismEnum.valueOf(syllogism_file);
		return syllogism_key;
	}


}
