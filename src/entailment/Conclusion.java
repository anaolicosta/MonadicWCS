package entailment;

import java.util.HashSet;
import java.util.Set;

import common.Mood;
import common.Predicate;

public class Conclusion {

	
	public static Conclusion AAC = new Conclusion(Mood.A, Predicate.A, Predicate.C); 
	public static Conclusion ACA = new Conclusion(Mood.A, Predicate.C, Predicate.A); 
	
	public static Conclusion IAC = new Conclusion(Mood.I, Predicate.A, Predicate.C); 
	public static Conclusion ICA = new Conclusion(Mood.I, Predicate.C, Predicate.A); 
	
	public static Conclusion EAC = new Conclusion(Mood.E, Predicate.A, Predicate.C); 
	public static Conclusion ECA = new Conclusion(Mood.E, Predicate.C, Predicate.A); 
	
	public static Conclusion OAC = new Conclusion(Mood.O, Predicate.A, Predicate.C); 
	public static Conclusion OCA = new Conclusion(Mood.O, Predicate.C, Predicate.A); 
	
	public static Conclusion NVC = new Conclusion(true); 

	public static Set<Conclusion> allConclusions = new HashSet<Conclusion>();

	public static Conclusion getConclusionFromEntailmentPredicate(final Predicate predicate) {
		switch(predicate) {
		case AAC: return Conclusion.AAC; 
		case ACA: return Conclusion.ACA;
		
		case IAC: return Conclusion.IAC; 
		case ICA: return Conclusion.ICA;
		
		case EAC: return Conclusion.EAC; 
		case ECA: return Conclusion.ECA;
		
		case OAC: return Conclusion.OAC; 
		case OCA: return Conclusion.OCA;
		
		default: return Conclusion.NVC;
		
		}
	}
	
	static {	
		allConclusions.add(Conclusion.AAC);
		allConclusions.add(Conclusion.ACA);
		allConclusions.add(Conclusion.IAC);
		allConclusions.add(Conclusion.ICA);
		allConclusions.add(Conclusion.EAC);
		allConclusions.add(Conclusion.ECA);
		allConclusions.add(Conclusion.OAC);
		allConclusions.add(Conclusion.OCA);
		allConclusions.add(Conclusion.NVC);
	}
	
	private Mood mood;
	private Predicate firstPredicate;
	private Predicate secondPredicate;
	private boolean isNVC;
	
	/**
	 * It is not possible to construct new conclusions.
	 */
	private Conclusion(final Mood mood, Predicate firstPredicate, Predicate secondPredicate) {
		this.mood = mood;
		this.firstPredicate = firstPredicate;
		this.secondPredicate = secondPredicate;
	}

	
	private Conclusion(final boolean isNVC) {
		this.isNVC = isNVC;
	}

	public boolean isNVC() {
		return isNVC;
	}

	@Override
	public String toString() {
		return isNVC ? "NVC"
				: mood.toString() + firstPredicate.toString().toLowerCase() 
								  + secondPredicate.toString().toLowerCase();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstPredicate == null) ? 0 : firstPredicate.hashCode());
		result = prime * result + (isNVC ? 1231 : 1237);
		result = prime * result + ((mood == null) ? 0 : mood.hashCode());
		result = prime * result + ((secondPredicate == null) ? 0 : secondPredicate.hashCode());
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
		Conclusion other = (Conclusion) obj;
		if (firstPredicate != other.firstPredicate)
			return false;
		if (isNVC != other.isNVC)
			return false;
		if (mood != other.mood)
			return false;
		if (secondPredicate != other.secondPredicate)
			return false;
		return true;
	}
	
}
