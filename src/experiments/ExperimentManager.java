package experiments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.Atom;
import common.Interpretation;
import common.Predicate;
import entailment.Conclusion;
import entailment.Conclusions;
import main.Main;
import main.TestPrologEntailment;
import output.PrintResultDescription;
import output.PrintResults;
import output.PrintableResults;
import output.SyllogismPrintable;
import strategies.StrategiesExperiment;
import strategies.StrategiesExperimentBlocks;
import syllogisms.Syllogism;
import syllogisms.SyllogismEnum;

public class ExperimentManager {
	
	private static final String EXPERIMENTS_BASE_DIR = "syllogisms/";
	private static int END_INDEX_SYLL_REF = 3;
	
	private PrintResults printResults;
	
	public ExperimentManager(final Path folderLeastModels, final Path folderPrograms, final String pattern)
			throws IOException {
		
		//Clean all the previous results and start a new set of experiments.
		Syllogism.clear();
		
		initSyllogisms(folderLeastModels, folderPrograms);
		

		//To print results
		printResults = new PrintResultDescription(EXPERIMENTS_BASE_DIR + "/description_" + pattern + ".txt");
		
		printResults.printInitialDescription();
		
		PrintableResults printable = new SyllogismPrintable(printResults);
		
		printable.printInitialData();
		
		//Start!
//		startExperiments();
//		
//		
//		//Collect and print results
//		collectOverallResults();
//		
//		printOverallResults();
//		
//		printCsv(pattern);
		
		test();
	}

	
	//TODO: In progress
	private void test(){

		for (SyllogismEnum syllogismKey : SyllogismEnum.values()) {
			Syllogism syllogism = Syllogism.getSyllogism(syllogismKey);
			
			String fileBaseName = EXPERIMENTS_BASE_DIR + syllogismKey.toString() + "/";
			//FileWatcher watcher = new FileWatcher(Paths.get(fileBaseName));

			String syllPath = fileBaseName;
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
		
		
			try {
				PrintWriter writer = new PrintWriter(fileBaseName + "entailment.pl", "UTF-8");
				String firstProgram = TestPrologEntailment
						.getProgramWithModel(syllogism.getmodelAndEntailment().getInterpretation());
				writer.println(firstProgram);

				//writer.println(TestPrologEntailment.addWeaker(firstProgram, syllogismKey.weakerMood));
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			String experimentPath = fileBaseName + "entailment";
			Path pathInterpretationFile = Paths.get(experimentPath + "glm.pl");

			
			ProcessBuilder pb = new ProcessBuilder(
					"/home/madame/Documents/Logic/EMCL/Project/Syllogisms/git/MonadicReasoning/resources/run.sh",
					syllogismKey.toString() + "/" + "entailment");
			try {
				pb.start();
				Thread.sleep(300);
				//watcher.watch(pathInterpretationFile);
			} catch (IOException | InterruptedException e) {
				System.out.println(
						"Failed to wacth file in path " + pathInterpretationFile + "\nMessage: " + e.getMessage());
			}

			//watcher.close();
			
			try {
				
				System.out.println("[" + syllogismKey + "]");
				
				Interpretation i = new Interpretation(pathInterpretationFile);
				Conclusions conc = new Conclusions();
				
				System.out.print(" Entailed: ");
				for(Atom a: i.getAtomsTrue()) {
					Predicate predicate = a.getPredicate();
					if(Predicate.getPredicateEntailment().contains(predicate)) {
						System.out.print(a + " ");
						conc.addConclusion(Conclusion.getConclusionFromEntailmentPredicate(predicate));
					}
				}
				
				System.out.print("\n False: ");

				boolean iDontknow = true;
				for(Atom a: i.getAtomsFalse()) {
					Predicate predicate = a.getPredicate();
					if(Predicate.getPredicateEntailment().contains(predicate)) {
						System.out.print(a + " ");
						iDontknow = false;
					}
				}
				
				if (conc.isEmpty()) conc.addConclusion(Conclusion.NVC);
				
				
				if (iDontknow) System.out.println("! I don't know ! ");
				
				System.out.println("\n Does it entail the same conclusions? "
						+ syllogism.getmodelAndEntailment().getConclusions().equals(conc));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				
				
			
		}
	}
	
	
	private void printCsv(final String pattern) {
		Main.printCsv.print(pattern);
		Main.printCsv.print(Main.cp.getMinimalityCriteria());
		Main.printCsv.print(Main.cp.getEntailment());
		Main.printCsv.print(Main.cp.getObservationsCriteria());
		Main.printCsv.print(Main.cp.getStrategiesAbducibles());
		Main.printCsv.print(String.format("%.6f", Syllogism.originalOverallPrecision));
		Main.printCsv.print(String.format("%.6f", Syllogism.credoulousOverallPrecision));
		Main.printCsv.print(String.format("%.6f", Syllogism.SkepticalOverallPrecision));
		Main.printCsv.print(String.format("%.6f", Syllogism.credoulousOverallPrecision - Syllogism.originalOverallPrecision));
		Main.printCsv.println(String.format("%.6f", Syllogism.SkepticalOverallPrecision - Syllogism.originalOverallPrecision));
		
	}

	/**
	 * Read least models and programs inside Path params
	 * 'folderLeastModels' and 'folderPrograms', respectively.
	 * 
	 * @param folderLeastModels
	 * @param folderPrograms
	 * @throws IOException
	 */
	private void initSyllogisms(final Path folderLeastModels, final Path folderPrograms) throws IOException {
		DirectoryStream<Path> dirLeastModels = Files.newDirectoryStream(folderLeastModels);
		DirectoryStream<Path> dirPrograms = Files.newDirectoryStream(folderPrograms);

		//Read least models.
		for (Path pathFileModel : dirLeastModels) {
			Syllogism syll = Syllogism.getSyllogism(getSyllogismKey(pathFileModel));
			syll.setModelAndEntailment(new Interpretation(pathFileModel));
		}
		
		//Read ground programs.
		for (Path pathProgram : dirPrograms) {
			Syllogism syll = Syllogism.getSyllogism(getSyllogismKey(pathProgram));
			syll.setProgram(Files.readAllLines(pathProgram));		
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
				
				printSummarySyllogism(syllogism, syllogismKey);
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
		
		closeDescriptionFile();
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
	
	public void printSummarySyllogism(final Syllogism syllogism, final SyllogismEnum syllogismKey) {
		
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
	
	public void  printSummaryBlock(final ExperimentBlock expBlock) {
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
	
	public void closeDescriptionFile() { this.printResults.closeDescriptionFile(); }
	
	
	
	
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