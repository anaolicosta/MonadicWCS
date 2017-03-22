package entailment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.Interpretation;
import common.Mood;
import common.Predicate;

public class EntailmentUniversalComplement implements Entails {


public Conclusions entails(Interpretation interpretation, Mood weakerMood) {
		
		Set<Conclusion> conclusions = new HashSet<Conclusion>();
		
		if(isAll(interpretation, Predicate.A, Predicate.C)) 
			conclusions.add(Conclusion.AAC);
		
		if(isAll(interpretation, Predicate.C, Predicate.A)) 
			conclusions.add(Conclusion.ACA);
		
		if(isSome(interpretation, Predicate.A, Predicate.C)) 
			conclusions.add(Conclusion.IAC);
		
		if(isSome(interpretation, Predicate.C, Predicate.A)) 
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
	
	
	public boolean isAll(final Interpretation model, final Predicate firstPredicate, final Predicate secondPredicate) {
		List<Integer> objectsTrueFirstP = model.getTrueObjectsOfPredicate(firstPredicate);
		List<Integer> objectsFalseFirstP = model.getFalseObjectsOfPredicate(firstPredicate);

		List<Integer> objectsTrueSecondP = model.getTrueObjectsOfPredicate(secondPredicate);
		List<Integer> objectsFalseSecondP = model.getFalseObjectsOfPredicate(secondPredicate);

		
		boolean atLeastOneElement = objectsTrueFirstP.size() > 0;
		boolean allObjectsInFirstAreInSecond = objectsTrueSecondP.containsAll(objectsTrueFirstP);		
		
		boolean falseObjInSecondIsUndefInFirst = false;
		
		for(Integer i: objectsFalseSecondP) {
			boolean isTrueInFirst = objectsTrueFirstP.contains(i);
			boolean isFalseInFirst = objectsFalseFirstP.contains(i);
			
			boolean isUndefined = !(isTrueInFirst || isFalseInFirst);
			
			falseObjInSecondIsUndefInFirst |=  isUndefined;
		}
				
				
		return atLeastOneElement && allObjectsInFirstAreInSecond && !falseObjInSecondIsUndefInFirst;
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
		List<Integer> objectsFalseFirstP = model.getFalseObjectsOfPredicate(firstPredicate);

		List<Integer> objectsFalseSecondP = model.getFalseObjectsOfPredicate(secondPredicate);
		List<Integer> objectsTrueSecondP = model.getFalseObjectsOfPredicate(secondPredicate);
		
		boolean atLeastOneElement = objectsTrueFirstP.size() > 0;
		boolean allObjectsInFirstAreInSecond = objectsFalseSecondP.containsAll(objectsTrueFirstP);
		
		boolean objTrueInSecondIsUndefInFirst = false;
		
		for(Integer i: objectsTrueSecondP) {
			boolean isTrueInFirst = objectsTrueFirstP.contains(i);
			boolean isFalseInFirst = objectsFalseFirstP.contains(i);
			
			boolean isUndefined = !(isTrueInFirst || isFalseInFirst);
			
			objTrueInSecondIsUndefInFirst |=  isUndefined;
		}
		
		
		return atLeastOneElement && allObjectsInFirstAreInSecond && !objTrueInSecondIsUndefInFirst;
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
