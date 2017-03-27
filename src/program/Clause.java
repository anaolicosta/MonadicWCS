package program;

import java.util.HashSet;
import java.util.Set;

import common.Atom;

public class Clause {
	
	
	/* Constants to build ground clauses.  */
	private static final String BEGIN_CLAUSE = "clause(";
	private static final String BEGIN_CLAUSE_G = "clause_g((";
	private static final String END_CLAUSE = ").";
	private static final String END_CLAUSE_G = ")).";
	
	private static final String TRUE = "t";
	private static final String FALSE = "f";
	
	private static final String IMPLIES = ":-";
	
	private static final String REGEX_NEGATION = "n(";
	
	
	private ClauseType type;
	private String body;
	private Atom head;
	
	public Clause(final Atom atom, final ClauseType type, final String body) {
		this.head = atom;
		this.type = type;
		switch(type) {
			case FACT: this.body = TRUE; break;
			case ASSUMPTION: this.body = FALSE; break;
			case RULE: this.body = body ;break;
			default: this.body = ""; break;
		}
	}
	
	public Clause(final Atom atom, final ClauseType type) {
		this.head = atom;
		this.type = type;
		switch(type) {
			case FACT: this.body = TRUE; break;
			case ASSUMPTION: this.body = FALSE; break;
			case RULE: this.body = "" ;break;
			default: this.body = ""; break;
		}
	}

	
	
	/**
	 * @return True if the clause is of type {@link ClauseType.ASSUMPTION}
	 */
	public boolean isAssumption() {
		return this.type.equals(ClauseType.ASSUMPTION);
	}
	
	/**
	 * @return True if the clause is of type {@link ClauseType.FACT}
	 */
	public boolean isFact() {
		return this.type.equals(ClauseType.FACT);
	}
	
	
	/**
	 * @return True if the clause is of type {@link ClauseType.RULE}
	 */
	public boolean isRule() {
		return this.type.equals(ClauseType.RULE);
	}
	
	
	/**	
	 * @return {@link Atom} in the head of the given clause.
	 */
	public Atom getHeadAtom() {
		return new Atom(this.head);
	}
	
	/**	
	 * @return The clause's body. 
	 */
	public String getBody() {
		return this.body;
	}
	
	public Set<Atom> getBodyAtoms() {
		
		Set<Atom> atomsInBody = new HashSet<Atom>();

		String[] bodyParts = body.split(",");
		for(String atom: bodyParts) {
			if(atom.startsWith(FALSE) || atom.startsWith(TRUE)) {
				break;
			} else if (atom.startsWith(REGEX_NEGATION)) {
				atomsInBody.add(new Atom(atom.substring(2, atom.length()-2)));
			} else {
				atomsInBody.add(new Atom(atom));
			}
		}
		return atomsInBody;
	}
	

	
	public String printGroundClause() {
		return BEGIN_CLAUSE_G + head.toString() + IMPLIES + "[" + body + "]" + END_CLAUSE_G;
	}
	
	public String printClause() {
		return BEGIN_CLAUSE + head.toString() + IMPLIES + "[" + body + "]" + END_CLAUSE;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((head == null) ? 0 : head.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Clause other = (Clause) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (head == null) {
			if (other.head != null)
				return false;
		} else if (!head.equals(other.head))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	
	
	
}
