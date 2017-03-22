package common;

import entailment.Conclusions;
import entailment.Entailment;

/**
 * Class that aggregates both an instance of {@link Interpretation}
 * and its {@link Conclusions} according to the entailment relation
 * defined in the configuration file.
 * 
 * <br />
 * <br />
 * Note that there is no direct way of updating Conclusions.
 * Conclusions are always derived with respect to the interpretation.
 * <br />
 * This guarantees consistency between Interpretation and Conclusion values.
 * 
 * @author Ana Oliveira da Costa
 * @since 2016
 */
public class ModelAndEntailment {
	
	private Interpretation interpretation;
	private Conclusions conclusions;
	private Mood weakerMood;
	
	/**
	 * Create an instance with empty Interpretation and Conclusions.
	 */
	public ModelAndEntailment(final Mood weakerMood) {
		this.interpretation = new Interpretation();
		this.conclusions = new Conclusions();
		this.weakerMood = weakerMood;
	}
	
	/**
	 * Create an instance with the given Interpretation.
	 * <br \> 
	 * Conclusions will be derived with respect to the given interpretation.
	 * @param interpretation
	 */
	public ModelAndEntailment(final Interpretation interpretation, final Mood weakerMood) {
		this.weakerMood = weakerMood;
		setInterpretation(interpretation);
	}
	
	
	public Interpretation getInterpretation() {
		return interpretation;
	}

	/**
	 * Change this intance's Interpretation to the given interpretation.
	 * <br />
	 * Important: Conclusions will be updated as a consequence of this change.
	 *  
	 * @param interpretation
	 */
	private void setInterpretation(Interpretation interpretation) {
		this.interpretation = interpretation;
		this.conclusions = Entailment.entails(interpretation, weakerMood);
	}

	public Conclusions getConclusions() {
		return conclusions;
	}

	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder();
		toString.append("\n[Interpretaion] ");
		toString.append(interpretation.toString());
		toString.append("\n[Conclusions] ");
		toString.append(conclusions.toString());
		return toString.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((conclusions == null) ? 0 : conclusions.hashCode());
		result = prime * result + ((interpretation == null) ? 0 : interpretation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModelAndEntailment other = (ModelAndEntailment) obj;
		if (conclusions == null) {
			if (other.conclusions != null)
				return false;
		} else if (!conclusions.equals(other.conclusions))
			return false;
		if (interpretation == null) {
			if (other.interpretation != null)
				return false;
		} else if (!interpretation.equals(other.interpretation))
			return false;
		return true;
	}

	
	
}
