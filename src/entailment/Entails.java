package entailment;

import common.Interpretation;
import common.Mood;
import common.Predicate;

public interface Entails {
	Conclusions entails(Interpretation interpretation);

	boolean isAll(final Interpretation model, final Predicate firstPredicate, final Predicate secondPredicate);

	boolean isSome(final Interpretation model, final Predicate firstPredicate, final Predicate secondPredicate);

	boolean isNone(final Interpretation model, final Predicate firstPredicate, final Predicate secondPredicate);

	boolean isSomeAreNot(final Interpretation model, final Predicate firstPredicate, final Predicate secondPredicate);
	
}
