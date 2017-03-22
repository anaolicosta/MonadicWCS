package abduction;

import java.util.HashSet;
import java.util.Set;

import common.Atom;
import common.Interpretation;

/**
 * Observation are sets of {@link Atom}s.
 * <br />
 * In the current version we only consider positive observations,
 * i.e. we expect the atoms to be evaluated to true.
 * 
 * @author Ana Oliveira da Costa
 * @since 2016
 */
public class Observations {

	private Set<Atom> observations;	
	
	/**
	 * Create a new instance of Observations with an empty set of atoms.
	 */
	public Observations() {
		this.observations = new HashSet<Atom>();
	}
	
	/**
	 * Create a new instance of Observations the set of atoms given by
	 * 'trueObservations'.
	 */
	public Observations(final Set<Atom> trueObservations) {
		this.observations = new HashSet<Atom>();
		this.observations.addAll(trueObservations);  
	}
	
	/**
	 * @param interpretation
	 * @return True if each atom  is entailed by the given interpretation; otherwise false.
	 */
	public boolean areObservationsEntailedBy(final Interpretation interpretation) {
		boolean allPositive = interpretation.getAtomsTrue().containsAll(observations);
		return allPositive;
	}
	
	
	/**
	 * @return A deep copy of the set of atoms.
	 */
	public Set<Atom> getObservations() {
		Set<Atom> newObservations = new HashSet<Atom>();
		newObservations.addAll(observations);
		return newObservations;
	}

	/**
	 * @return Object's references that occur in the 
	 * atoms in this instance.
	 */
	public Set<Integer> getObjectReferences() {
		Set<Integer> objReferences = new HashSet<Integer>();
		for(Atom obs: observations) {
			objReferences.add(obs.getObjectRef());
		}
		return objReferences;
	}

	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder();
		observations.forEach(a-> toString.append(a + " "));
		return toString.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((observations == null) ? 0 : observations.hashCode());
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
		Observations other = (Observations) obj;
		if (observations == null) {
			if (other.observations != null)
				return false;
		} else if (!observations.equals(other.observations))
			return false;
		return true;
	}
	
	
}
