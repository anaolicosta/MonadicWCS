package common;

/**
 * An atom is a predicate that is ground.
 * <br \>
 * An atom in defined by a {@link Predicate} and 
 * a reference (integer) for the object that belongs
 * to that predicate.
 * <br >
 * Note that all our predicates are <b>unary</b>.
 * 
 * @author Ana Oliveira da Costa
 * @since 2016
 */
public class Atom {
	
	private static final String REGEX_REMOVE_NON_INT = "[\\D]";
	private static final String SPLIT_PREDICATE_FROM_OBJ = "\\(";
	
	private static final int SPLIT_PREDICATE_POS = 0;
	private static final int SPLIT_OBJ_POS = 1;
	
	
	private Predicate predicate;
	private int objectRef;
	
	/**
	 * Creates a new Atom's instance with the given predicate and object.
	 */
	public Atom(final Predicate predicate, final int object) {
		this.predicate = predicate;
		this.objectRef = object;
	}
	
	/**
	 * Create an {@link Atom} instance given a string with the following format:
	 * <br />
	 * <br />
	 * ATOM = PREDICATE'('OBJECT_REF')'
	 * <br \> 
	 * PREDICATE = Letter
	 * <br \>
	 * OBJECT_REF = 'o'Integer
	 * </code>
	 */
	public Atom(final String atom) {
		String[] atomSplit = atom.split(SPLIT_PREDICATE_FROM_OBJ);
		
		this.predicate = Predicate.valueOf(atomSplit[SPLIT_PREDICATE_POS].toUpperCase());
		this.objectRef =  Integer.parseInt(atomSplit[SPLIT_OBJ_POS].replaceAll(REGEX_REMOVE_NON_INT, ""));
	}
	
	/**
	 * Create an {@link Atom} instance given an instance of {@link Atom}.
	 */
	public Atom(final Atom atom) {
		this.predicate = atom.getPredicate();
		this.objectRef = atom.getObjectRef();
	}

	/**
	 * @return Atom's predicate.
	 */
	public Predicate getPredicate() {
		return predicate;
	}

	/**
	 * @return Atom's object reference.
	 */
	public int getObjectRef() {
		return objectRef;
	}

	/**
	 * Uses {@link Predicate#equals(Object)} to check
	 * if two atoms have the same Predicate.
	 * 
	 * @param p Predicate to check
	 * @return True if it is the same {@link Predicate}
	 * as the one from this {@link Atom} instance, false otherwise.
	 */
	public boolean isSamePredicate(final Predicate p) {
		return predicate.equals(p);
	}
	
	@Override
	public String toString() {
		return predicate.toString().toLowerCase() + "(o" + objectRef + ")";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + objectRef;
		result = prime * result + ((predicate == null) ? 0 : predicate.hashCode());
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
		Atom other = (Atom) obj;
		if (objectRef != other.objectRef)
			return false;
		if (predicate != other.predicate)
			return false;
		return true;
	}

	@Override
	protected Object clone() {
		return new Atom(this.predicate, this.objectRef);
	}
	
	

}
