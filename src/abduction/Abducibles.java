package abduction;

import java.util.HashSet;
import java.util.Set;

import common.Atom;
import program.Clause;
import program.ClauseType;

/**
 * Set of Abducibles. An abducible is a {@link Clause}. 
 * <br \>
 * 
 * Implements auxiliary methods needed to
 * reason about sets of Abducibles. 
 * 
 * 
 * @author Ana Oliveira da Costa
 * @since 2016
 */
public class Abducibles {
	
	private Set<Clause> setAbducibles;
	
	/**
	 * Create Abducibles with an empty set of Abducibles. 
	 */
	public Abducibles() {
		this.setAbducibles = new HashSet<Clause>();
	}
	
	/**
	 * Create Abducibles with the set of Abducibles given as parameter.
	 */
	public Abducibles(final Set<Clause> setAbducibles) {
		this.setAbducibles = new HashSet<Clause>();
		this.setAbducibles.addAll(setAbducibles);
	}
	
	/**
	 * @param sizeOfSubset Size of the  subsets to return.
	 * @return All subsets of the size given as parameter.
	 */
	public Set<Abducibles> getSubsetsOfSize(final int sizeOfSubset) {
		
		Set<Abducibles> abduciblesOfSize = new HashSet<Abducibles>();
			
		//Start by creating one set for each element in the original set.
		//Then it evokes the recursive method 
		//to create all supersets of that single set with the intended size. 
		for(Clause a: this.setAbducibles) {
			
			Set<Clause> startSubset = new HashSet<Clause>();
			startSubset.add(a);
			
			//'abduciblesToConsider' has all the possible members of a superset of 'startSubset'.
			//'abduciblesToConsider' is initialized with all abducibles 
			//except for the single abducible in startSubset.
			Set<Clause> abduciblesToConsider = new HashSet<Clause>();
			abduciblesToConsider.addAll(this.setAbducibles);
			abduciblesToConsider.remove(a);
			
			//Recursive method
			Set<Abducibles> subsetsOfSize = getSubsetsOfSize(startSubset, abduciblesToConsider, sizeOfSubset);
			
			//It is assumed that if 'subsetsOfSize' is not empty
			//then all its elements are of the intended size.
			if(!subsetsOfSize.isEmpty())
				abduciblesOfSize.addAll(subsetsOfSize);
		}
	
		return abduciblesOfSize;
	}

	/**
	 * Recursive method to create all subsets of a given size.
	 * 
	 * @param currentSubset Subset build so far.
	 * @param abduciblesCandidates Set with all the elements that can be added to the currentSubset.
	 * 					This set is used to prevent considering unnecessary elements.
	 *                  (Duplicates are preventing already by using Sets) 
	 * @param maxSizeOfSubset The intend size of subsets to generate.
	 * @return All the subsets with size 'maxSizeOfSubset'.
	 */
	private Set<Abducibles> getSubsetsOfSize(final Set<Clause> currentSubset, 
			final Set<Clause> abduciblesCandidates,  
			final int maxSizeOfSubset) {
	
		Set<Abducibles> abduciblesOfSize = new HashSet<Abducibles>();
		
		
		// Base cases.
		if(currentSubset.size() == maxSizeOfSubset) {
			abduciblesOfSize.add(new Abducibles(currentSubset));
		}else if(abduciblesCandidates.isEmpty()) {
			//DO NOTHING.
			//We don't want to have in our output sets that are smaller than maxSizeOfSubset.
		} else {
			
			for(Clause a: abduciblesCandidates) {
				Set<Clause> newSubset = new HashSet<Clause>();
				newSubset.addAll(currentSubset);
				newSubset.add(a);
				
				Set<Clause> updatedCandidates = new HashSet<Clause>();
				updatedCandidates.addAll(abduciblesCandidates);
				updatedCandidates.remove(a);
				
				abduciblesOfSize.addAll(getSubsetsOfSize(newSubset, updatedCandidates, maxSizeOfSubset));
			}
		}
		
		return abduciblesOfSize;
		
	}
	
	/**
	 * @return A deep copy of the set of {@link Abducible}s. 
	 */
	public Set<Clause> getAbduciblesAsSet() {
		Set<Clause> newAbducibles = new HashSet<Clause>();
		newAbducibles.addAll(this.setAbducibles);
		return newAbducibles;
	}
	
	/**
	 * @param abducibles New set of {@link Abducible}s.
	 */
	public void setAbducibles(final Set<Clause> abducibles) {
		this.setAbducibles = new HashSet<Clause>();
		this.setAbducibles.addAll(abducibles);
	}
	
	/**
	 * @return Number of {@link Abducible}s in this instance.
	 */
	public int getNumberOfAbducibles() {
		return setAbducibles.size();
	}
	
	/**
	 * @return A set with all the object's references that occur 
	 * in each of the {@link Abducible}s.
	 */
	public Set<Integer> getAllObjReferences(){
		Set<Integer> objRef = new HashSet<Integer>();
		for(Clause a: setAbducibles)
			objRef.add(a.getHeadAtom().getObjectRef());
		return objRef;
	}
	
	/**
	 * @param abduciblesToCheck
	 * @return True if the set of {@link Abducible}s in this instance is a superset
	 * of {@link Abducible}s in 'abduciblesToCheck'
	 */
	public boolean isSupersetAbduciblesOf(final Abducibles abduciblesToCheck){
		return this.getAbduciblesAsSet().containsAll(abduciblesToCheck.getAbduciblesAsSet());
	}
	
	
	/**
	 * Important: So far we only modeled positive observations. 
	 * Thus, for this comparison we only look to atoms in head
	 * of abducibles that are facts.
	 * 
	 * @param observations
	 * @return True if the atoms in the head of facts in 
	 * this instance {@link Abducible}s' set are the same
	 * as in the given observations.
	 */
	public boolean areTheSameAsObservations(final Observations observations) {
		Set<Atom> trueObservations = observations.getObservations();
		boolean containAll = true;
		for(Atom a: trueObservations) {
			containAll &= setAbducibles.contains(new Clause(a, ClauseType.FACT, ""));
		}
		
		return containAll;
	}
	
	@Override
	//This is used to add the abducibles to the program.
	public String toString() {
		StringBuilder toString = new StringBuilder();
		for(Clause c: setAbducibles) {
			toString.append(c.printClause() + "\n");
		}
		return toString.toString();
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((setAbducibles == null) ? 0 : setAbducibles.hashCode());
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
		Abducibles other = (Abducibles) obj;
		if (setAbducibles == null) {
			if (other.setAbducibles != null)
				return false;
		} else if (!setAbducibles.equals(other.setAbducibles))
			return false;
		return true;
	}
	
	

}
