package experiments;

import java.util.ArrayList;
import java.util.List;

import abduction.Abducibles;
import abduction.Observations;
import entailment.Conclusions;
import program.Program;

public class ExperimentBlock {

	private Observations observations;
	private Program program;
	
	private Abducibles initialAbducibles;
	//use for subset implementation
	private Abducibles currentAbducibles;

	//Keeps track of the size of the
	//abducibles' subsets while running experiments.
	private int currentSizeExperiment;

	//Experiments to run on the current iteration.
	private List<Experiment> currentExperiments;
	
	//Experiments that were successful.
	private List<Experiment> successExperiments;

	private Conclusions credoulousConclusions;
	private Conclusions skepticalConclusions;
	
	private boolean hasSucessfullExperiments;
	
	
	public ExperimentBlock(final Observations observations, 
			final Program program, final Abducibles abducibles ) {
		this.observations = observations;
		this.program = program;
		this.initialAbducibles = abducibles;
		this.currentAbducibles = abducibles;

		this.currentSizeExperiment = 1;
		
		this.currentExperiments = new ArrayList<Experiment>();
		this.successExperiments = new ArrayList<Experiment>();
		
		this.credoulousConclusions = new Conclusions();
		this.skepticalConclusions = new Conclusions();
		
		this.hasSucessfullExperiments = false;
		
	}

	public List<Experiment> getCurrentExperiments() {
		return currentExperiments;
	}
	
	public List<Experiment> getSuccessExperiments() {
		return successExperiments;
	}

	public void setCurrentExperiments(List<Experiment> experiments) {
//		List<Experiment> newExperiments = new ArrayList<Experiment>();
//		newExperiments.addAll(experiments);
		this.currentExperiments = experiments;
	}
	
	public void setSuccessExperiments(List<Experiment> experiments) {
//		List<Experiment> newExperiments = new ArrayList<Experiment>();
//		newExperiments.addAll(experiments);
		this.successExperiments = experiments;
	}
	
	
	public int getCurrentSizeExperiment() {
		return currentSizeExperiment;
	}

	public void setCurrentSizeExperiment(int currentSizeExperiment) {
		this.currentSizeExperiment = currentSizeExperiment;
	}

	public Observations getObservations() {
		return observations;
	}

	
	public Abducibles getInitialAbducibles() {
		return initialAbducibles;
	}
	
	public Abducibles getCurrentAbducibles() {
		return currentAbducibles;
	}
	
	public void setCurrentAbducibles(Abducibles currentAbducibles) {
		this.currentAbducibles = currentAbducibles;
	}
	
	public Program getProgram() {
		return program;
	}

	public Conclusions getCredoulousConclusions() {
		return credoulousConclusions;
	}

	public void setCredoulousConclusions(Conclusions conclusions) {
		this.credoulousConclusions = conclusions;
	}

	public Conclusions getSkepticalConclusions() {
		return skepticalConclusions;
	}

	public void setSkepticalConclusions(Conclusions skepticalConclusions) {
		this.skepticalConclusions = skepticalConclusions;
	}
	
	

	public boolean isHasSucessfullExperiments() {
		return hasSucessfullExperiments;
	}

	public void setHasSucessfullExperiments(boolean hasSucessfullExperiments) {
		this.hasSucessfullExperiments = hasSucessfullExperiments;
	}

	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder();
		toString.append("\n--> Observations");
		toString.append(observations.toString());
		//toString.append(program.toString() + "\n");
		toString.append("\n--> Abducibles");
		toString.append(initialAbducibles.toString());
		
		toString.append("\n--> Experiments:");
		for(int i = 0; i < currentExperiments.size(); i++) {
			toString.append("\nExperiment " + i +":");
			toString.append(currentExperiments.get(i).toString());
		}
		
		toString.append("\n--> Successful Experiments:");
		for(int i = 0; i < successExperiments.size(); i++) {
			toString.append("\nExperiment " + i +":");
			toString.append(successExperiments.get(i).toString());
		}
		
		toString.append("\n--> Credoulous conclusions:");
		toString.append(this.credoulousConclusions.toString());
		
		toString.append("\n--> Skeptical conclusions:");
		toString.append(this.skepticalConclusions.toString());
		
		return toString.toString();
	}

}
