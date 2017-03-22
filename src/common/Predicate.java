package common;

import java.util.HashSet;
import java.util.Set;

public enum Predicate {

	
	A,
	B,
	C,

	NA,
	NB,
	NC,
	
	ABAB,
	ABBA,
	
	ABAC,
	ABCA,
	
	ABBC,
	ABCB,
	
	
	ABANB,
	ABBNA,
	
	ABANC,
	ABCNA,
	
	ABBNC,
	ABCNB,
	
	ABNAB,
	ABNBA,
	
	ABNAC,
	ABNCA,
	
	ABNBC,
	ABNCB,
	
	ABANA,
	ABNAA,
	
	
	ABBNB,
	ABNBB,
	
	ABCNC,
	ABNCC,
	
	AAC,
	ACA,
	
	IAC,
	ICA,
	
	EAC,
	ECA,
	
	OAC,
	OCA,
	
	NAAC,
	NACA,
	
	NEAC,
	NECA,
	
	UGAC,
	UGCA,
	
	UGANC,
	UGCNA,
	
	ABAAC,
	ABACA,
	
	ABIAC,
	ABICA,
	
	ABEAC,
	ABECA,
	
	ABOAC,
	ABOCA,
	
	UA,
	UC,
	
	//PRINCIPLES
	RULEAC,
	RULECA,
	RULEACNEG,
	RULECANEG,
	
	REFUTEAC,
	NOREFUTEAC,
	REFUTECA,
	NOREFUTECA,
	
	REFUTEACNEG,
	NOREFUTEACNEG,
	REFUTECANEG,
	NOREFUTECANEG;
	
	/** List predicates that give us entailed conclusions. */
	public static Set<Predicate> getPredicateEntailment(){
		Set<Predicate> entail = new HashSet<Predicate>();
		
		entail.add(AAC);
		entail.add(ACA);
		entail.add(IAC);
		entail.add(ICA);
		entail.add(EAC);
		entail.add(ECA);
		entail.add(OAC);
		entail.add(OCA);
		
		return entail;
	}
	
	/**
	 * @return All related abdnormalities atoms for the predicates: {@link #A}, {@link #B}, {@link #C}.
	 */
	public Set<Predicate> getRelatedAbnormalities() {
		Set<Predicate> abnormalities = new HashSet<Predicate>();
		
		switch(this) {
		case A: 
			abnormalities.add(ABAB);
			abnormalities.add(ABBA);
			abnormalities.add(ABAC);
			abnormalities.add(ABCA);
			
			abnormalities.add(ABANA);
			abnormalities.add(ABNAA);
			break;
		case B:
			abnormalities.add(ABBA);
			abnormalities.add(ABAB);
			abnormalities.add(ABBC);
			abnormalities.add(ABCB);
			
			abnormalities.add(ABBNB);
			abnormalities.add(ABNBB);
			break;
		case C:
			abnormalities.add(ABCB);
			abnormalities.add(ABBC);
			abnormalities.add(ABAC);
			abnormalities.add(ABCA);
			
			abnormalities.add(ABCNC);
			abnormalities.add(ABNCC);
			break;
		default: break;
		}
		
		return abnormalities;
	}
	
	/**
	 * @return True if it is an abnormal predicate; false otherwise.
	 */
	public boolean isAbnormalPredicate(){
		return this.toString().startsWith("AB");
	}

	/**
	 * @return True if it is a negated predicate; false otherwise.
	 */
	public Predicate getNegatedPredicate() {
		
		switch(this) {
		case A: return NA;
		case B: return NB;
		case C: return NC;
			
		case NA: return A;
		case NB: return B;
		case NC: return C;
			
		default: return this;
		}
	}
	
}
