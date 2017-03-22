package entailment;

import java.util.HashSet;
import java.util.Set;

/**
 * Class that keep a list of conclusions.
 * <br />
 * {@link #hasNVC()} can be used to check if
 * there is no conclusion in this list.
 * 
 * @author Ana Oliveira da Costa
 * @since 2016
 */
public class Conclusions {
	
	private Set<Conclusion> conclusions;	
	
	public Conclusions() {
		this.conclusions = new HashSet<Conclusion>();
	}
	
	public Conclusions(final Set<Conclusion> conclusions) {
		this.conclusions = new HashSet<Conclusion>();
		this.conclusions.addAll(conclusions);
	}
	
	
	public Conclusions(final Conclusion ... conclusions) {
		this.conclusions = new HashSet<Conclusion>();
		for(Conclusion c: conclusions)
			addConclusion(c);
	}
	
	
	public void addConclusion(final Conclusion conclusion) {
		this.conclusions.add(conclusion);
	}
	
	public void addConclusions(final Conclusions conclusions) {
		for(Conclusion c: conclusions.getSetOfConclusions()) {
			this.conclusions.add(c);
		}
	}
	
	public void intersectConclusions(final Conclusions conclusions) {
		this.conclusions.retainAll(conclusions.getSetOfConclusions());
	}
	
	public Set<Conclusion> getSetOfConclusions() {
		Set<Conclusion> newConclusions = new HashSet<Conclusion>();
		newConclusions.addAll(this.conclusions);
		return newConclusions;
	}
	
	public boolean isEmpty() {
		return this.conclusions.isEmpty();
	}
	
	public void clearConclusions(){
		this.conclusions.clear();
	}
	
	public boolean containsAllConclusions(Conclusions c) {
		return this.conclusions.containsAll(c.getSetOfConclusions());
	}
	
	public boolean hasNVC(){
		return this.conclusions.contains(Conclusion.NVC);
	}
	
	public boolean hasConclusionNotNVC() {
		return !this.conclusions.isEmpty() && 
				(this.conclusions.contains(Conclusion.AAC) || 
						this.conclusions.contains(Conclusion.ACA) ||
						this.conclusions.contains(Conclusion.EAC) ||
						this.conclusions.contains(Conclusion.ECA) ||
						this.conclusions.contains(Conclusion.IAC) ||
						this.conclusions.contains(Conclusion.ICA) ||
						this.conclusions.contains(Conclusion.OAC) ||
						this.conclusions.contains(Conclusion.OCA)  );
	}
	
	public int matchingConclusions(Conclusions conclusionsToCompare) {
		Set<Conclusion> mySet = this.getSetOfConclusions();
		Set<Conclusion> setToCompare = conclusionsToCompare.getSetOfConclusions();
		
		int matching = 0;
		for(Conclusion conclusion: Conclusion.allConclusions) {
			if(mySet.contains(conclusion) && setToCompare.contains(conclusion))
				matching ++;
			else if (!mySet.contains(conclusion) && !setToCompare.contains(conclusion))
				matching++;
		}
		
		return matching;
	}

	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder();
		conclusions.forEach(c -> toString.append(c.toString() + " "));
		return toString.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((conclusions == null) ? 0 : conclusions.hashCode());
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
		Conclusions other = (Conclusions) obj;
		if (conclusions == null) {
			if (other.conclusions != null)
				return false;
		} else if (!conclusions.equals(other.conclusions))
			return false;
		return true;
	}

}
