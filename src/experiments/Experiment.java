package experiments;

import abduction.Abducibles;
import abduction.Observations;
import common.ModelAndEntailment;
import common.Mood;

public class Experiment {

	private Abducibles abducibles;
	private ModelAndEntailment modelAndEntailment;
	//TODO: check if areObservationsEntailed can be updated by ExperimentBlock
	private Observations observationToCheck;
	private boolean areObservationsEntailed;
	private double accuracy;
	
	
	public Experiment(final Abducibles abducibles, final Observations observations) {
		this.abducibles = abducibles;
		
		this.modelAndEntailment = new ModelAndEntailment();
		this.areObservationsEntailed = false;
		
		this.observationToCheck = observations;
		
		this.accuracy = 0.0;
	}

	/** GETs and SETs. */
	public ModelAndEntailment getModelAndEntailment() {
		return modelAndEntailment;
	}

	public void setModelAndEntailment(ModelAndEntailment modelAndEntailment) {
		this.modelAndEntailment = modelAndEntailment;
		this.areObservationsEntailed = this.observationToCheck
				.areObservationsEntailedBy(this.modelAndEntailment.getInterpretation());
	}

	public Abducibles getAbducibles() {
		return abducibles;
	}

	public boolean areObservationsEntailed() {
		return this.areObservationsEntailed;
	}

	
	
	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	@Override
	public String toString() {
		return abducibles.toString();
	}
	
	
	
	
	
}
