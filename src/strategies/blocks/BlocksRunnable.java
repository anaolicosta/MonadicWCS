package strategies.blocks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import abduction.Abducibles;
import abduction.Observations;
import common.Atom;
import common.Interpretation;
import common.ModelAndEntailment;
import experiments.Experiment;
import experiments.ExperimentBlock;
import main.FileWatcher;
import main.Main;
import program.Clause;
import program.Program;
import strategies.StrategiesExperiment;
import syllogisms.SyllogismEnum;

public abstract class BlocksRunnable implements StrategyExperimentBlocks {
	
	//TODO: move to config??
	private static final String EXPERIMENTS_BASE_DIR = "syllogisms/";

	public void runExperiments(SyllogismEnum syllogismKey, ExperimentBlock experimentBlock, final int numberBlock) {
		
		boolean obsInHeadOfRule = false;
		Set<Atom> atomsInHead = experimentBlock.getProgram().getProgramHeadAtoms();
		for(Atom obs: experimentBlock.getObservations().getObservations()) {
			obsInHeadOfRule |= atomsInHead.contains(obs);
		}
		
		if(obsInHeadOfRule) {
		
			switch (Main.cp.getMinimalityCriteria()) {
			case "cardinal":
				runExperimentsCardinal(syllogismKey, experimentBlock, numberBlock); break;
			case "subset":
				runExperimentsSubset(syllogismKey, experimentBlock, numberBlock); break;
			default:
				runExperimentsCardinal(syllogismKey, experimentBlock, numberBlock); break;
			}
			
		} else {
//		System.out.println("> Observation: " + experimentBlock.getObservations().toString() +
//				" is not in the head of any program's rule.");
		}
	}
	
	private void runExperimentsSubset(SyllogismEnum syllogismKey, ExperimentBlock experimentBlock,
			final int numberBlock) {

		List<Experiment> successfullExperiments = new ArrayList<Experiment>();
		Set<Clause> abduciblesToUse = experimentBlock.getCurrentAbducibles().getAbduciblesAsSet();

		int sizeAbducibles = abduciblesToUse.size();
		Set<Abducibles> sucessfullAbducibles = new HashSet<Abducibles>();

		for (int round = 1; round <= sizeAbducibles; round++) {

			experimentBlock.setCurrentSizeExperiment(round);

			// Get experiments for current abducibles.
			// Experiments will have abducibles' subset with the size of the
			// round number
			List<Experiment> experiments = StrategiesExperiment.getExperiments(experimentBlock);
			List<Experiment> filterExperiments = new ArrayList<Experiment>();
			for(Experiment e: experiments) {
				Iterator<Abducibles> sAbIt = sucessfullAbducibles.iterator();
				
				boolean hasSubset = false;
				while(sAbIt.hasNext() && !hasSubset) {
					Abducibles sAbducibles = (Abducibles) sAbIt.next();
					if(e.getAbducibles().isSupersetAbduciblesOf(sAbducibles)) {
						hasSubset = true;
					}
				}
				
				if(!hasSubset) filterExperiments.add(e);
			}
			
			experimentBlock.setCurrentExperiments(filterExperiments);

			// Run experiment

			List<Experiment> lastSuccessfullExperiments = runExperimentBlock(syllogismKey, numberBlock,
					experimentBlock);

			if (!lastSuccessfullExperiments.isEmpty()) {

				successfullExperiments.addAll(lastSuccessfullExperiments);

				for (Experiment e : lastSuccessfullExperiments) {
					sucessfullAbducibles.add(e.getAbducibles());
				}

				// Update current abducibles in experiment block
				experimentBlock.setCurrentAbducibles(new Abducibles(abduciblesToUse));
			}
		}
		experimentBlock.setSuccessExperiments(successfullExperiments);

	}
	
	private void runExperimentsCardinal(SyllogismEnum syllogismKey, ExperimentBlock experimentBlock,
			final int numberBlock) {

		int sizeAbducibles = experimentBlock.getCurrentAbducibles().getNumberOfAbducibles();
		// This boolean flag set to true when a set of abducibles is found that
		// together with the program entails the observations
		boolean foundSuccessExperiments = false;

		for (int round = 1; round <= sizeAbducibles && !foundSuccessExperiments; round++) {

			experimentBlock.setCurrentSizeExperiment(round);
			List<Experiment> experiments = StrategiesExperiment.getExperiments(experimentBlock);
			experimentBlock.setCurrentExperiments(experiments);

			// Run experiment
			List<Experiment> successfullExperiments = runExperimentBlock(syllogismKey, numberBlock, experimentBlock);

			if (!successfullExperiments.isEmpty()) {
				experimentBlock.setSuccessExperiments(successfullExperiments);
				foundSuccessExperiments = true;
			}

		}

	}

	public List<Experiment> runExperimentBlock(final SyllogismEnum syllogismKey, final int numberBlock,
			final ExperimentBlock expBlock) {

		String experimentBasePath = EXPERIMENTS_BASE_DIR + syllogismKey.toString() + "/";
		String fileNameStart = numberBlock + "_";

		List<Experiment> experiments = expBlock.getCurrentExperiments();
		Program program = expBlock.getProgram();
		
		FileWatcher watcher = new FileWatcher(Paths.get(experimentBasePath));
		
		List<Experiment> successfullExperiments = new ArrayList<Experiment>();

		for (int i = 0; i < experiments.size(); i++) {

			String fileName = fileNameStart + i + "_" + syllogismKey.toString();
			String experimentPath = experimentBasePath + fileName;
			Path pathInterpretationFile = Paths.get(experimentPath + "glm.pl");

			Experiment experiment = experiments.get(i);
			createFiles(program, experimentPath, experiment);

			if (experiment.getAbducibles().areTheSameAsObservations(expBlock.getObservations())) {
				System.out.println("Trivial abducibles: #" + i + ". Not tested.");
			} else {
				ProcessBuilder pb = new ProcessBuilder("resources/run.sh",
						 syllogismKey.toString() + "/" + fileName);
				try {
					pb.start();
					//Watcher react on modify.
					watcher.watch(pathInterpretationFile);
				} catch (IOException e) {
					System.out.println(
							"Failed to wacth file in path " + pathInterpretationFile + "\nMessage: " + e.getMessage());
				}

				try {
					
					experiment
							.setModelAndEntailment(
									new ModelAndEntailment(new Interpretation(pathInterpretationFile)));

					if (experiment.areObservationsEntailed()) {
						successfullExperiments.add(experiment);
					}
					
					deleteFiles(experimentPath);

				} catch (IOException e) {
					System.out.println(
							"Failed to delete files in path " + experimentPath + "\nMessage: " + e.getMessage());
				}
			}
		}
		
		watcher.close();
		
		return successfullExperiments;
	}



	public void createFiles(Program program, String experimentPath, Experiment experiment) {
		try {
			PrintWriter writer = new PrintWriter(experimentPath + ".pl", "UTF-8");
			writer.println(program.toString());
			writer.println("%Abducibles");
			writer.println(experiment.getAbducibles().toString());
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteFiles(String experimentPath) {
		File fileToDelete = new File(experimentPath + ".pl");
		if (fileToDelete.exists())
			fileToDelete.delete();

		fileToDelete = new File(experimentPath + "g.pl");
		if (fileToDelete.exists())
			fileToDelete.delete();

		fileToDelete = new File(experimentPath + "glm.pl");
		if (fileToDelete.exists())
			fileToDelete.delete();
	}
	
	
	public String generalBlocksDescription() {
		return  "Common Experiment Blocks implmentation:"
				+ "\nObservations are facts choosen according to the value in property \'observations\' "
				+ "in config.properties file."
				+ "\nAn experiment block will consist of a set of experiments."
				+ "\nThe role of an experiment is to verify if a subset of program's abducibles "
				+ "entails observations in its experiment block."
				+ "\nAbducibles are generated according to Abducibles' strategy given a program."
				+ "The program considered is different for some strategies.";
	}

}

