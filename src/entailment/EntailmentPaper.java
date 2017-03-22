package entailment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.Interpretation;
import common.Mood;
import common.Predicate;

public class EntailmentPaper implements Entails {

	public Conclusions entails(Interpretation interpretation) {
		
		Set<Conclusion> conclusions = new HashSet<Conclusion>();
		
		if(isAll(interpretation, Predicate.A, Predicate.C)) 
			conclusions.add(Conclusion.AAC);
		
		if(isAll(interpretation, Predicate.C, Predicate.A)) 
			conclusions.add(Conclusion.ACA);
		
		if(isSome(interpretation, Predicate.A, Predicate.C) && isSome(interpretation, Predicate.C, Predicate.A)) 
			conclusions.add(Conclusion.IAC);
		
		if(isSome(interpretation, Predicate.C, Predicate.A) && isSome(interpretation, Predicate.A, Predicate.C)) 
			conclusions.add(Conclusion.ICA);
		
		if(isNone(interpretation, Predicate.A, Predicate.C)) 
			conclusions.add(Conclusion.EAC);
		
		if(isNone(interpretation, Predicate.C, Predicate.A)) 
			conclusions.add(Conclusion.ECA);
		
		if(isSomeAreNot(interpretation, Predicate.A, Predicate.C)) 
			conclusions.add(Conclusion.OAC);
		
		if(isSomeAreNot(interpretation, Predicate.C, Predicate.A)) 
			conclusions.add(Conclusion.OCA);
		
		if(conclusions.isEmpty())
			conclusions.add(Conclusion.NVC);
		
		return new Conclusions(conclusions);
	}
	
	/**
	 * @param model
	 * @param firstPredicate
	 * @param secondPredicate
	 * @return
	 */
	public boolean isAll(final Interpretation model, final Predicate firstPredicate, final Predicate secondPredicate) {
		List<Integer> objectsTrueFirstP = model.getTrueObjectsOfPredicate(firstPredicate);
		List<Integer> objectsTrueSecondP = model.getTrueObjectsOfPredicate(secondPredicate);
		
		return objectsTrueFirstP.size() > 0 && objectsTrueSecondP.containsAll(objectsTrueFirstP);
	}
	
	public boolean isSome(final Interpretation model, final Predicate firstPredicate, final Predicate secondPredicate) {
		List<Integer> objectsTrueFirstP = model.getTrueObjectsOfPredicate(firstPredicate);
		List<Integer> objectsTrueSecondP = model.getTrueObjectsOfPredicate(secondPredicate);
		
		boolean isSome = false;
		boolean isUnknownGeneral = false;
		for(Integer objectRef: objectsTrueFirstP) {
			boolean bothTrue = objectsTrueSecondP.contains(objectRef);
			isSome |= bothTrue;
			isUnknownGeneral |= (!bothTrue);
		}
		
		return isSome && isUnknownGeneral;
	}
	
	public boolean isNone(final Interpretation model, final Predicate firstPredicate, final Predicate secondPredicate) {
		List<Integer> objectsTrueFirstP = model.getTrueObjectsOfPredicate(firstPredicate);
		List<Integer> objectsFalseSecondP = 
				//model.getTrueObjectsOfPredicate(secondPredicate.getNegatedPredicate());
				model.getFalseObjectsOfPredicate(secondPredicate);
		
		return objectsTrueFirstP.size() > 0 && objectsFalseSecondP.containsAll(objectsTrueFirstP);
	}
	
	public boolean isSomeAreNot(final Interpretation model, final Predicate firstPredicate, final Predicate secondPredicate) {
		List<Integer> objectsTrueFirstP = model.getTrueObjectsOfPredicate(firstPredicate);
		List<Integer> objectsFalseSecondP = 
				//model.getTrueObjectsOfPredicate(secondPredicate.getNegatedPredicate());
		model.getFalseObjectsOfPredicate(secondPredicate);
		
		
		boolean isSomeNot = false;
		boolean isUnknownGeneral = false;
		for(Integer objectRef: objectsTrueFirstP) {
			boolean oneTrueOtherFalse = objectsFalseSecondP.contains(objectRef);
			isSomeNot |= oneTrueOtherFalse;
			isUnknownGeneral |= (!oneTrueOtherFalse);
		}
		
		return isSomeNot && isUnknownGeneral;
	}

}
