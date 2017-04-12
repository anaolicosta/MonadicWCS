package program;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import common.Atom;
import common.Predicate;
import main.Main;

public class Program {

	private List<Clause> clauses;
	
	/**
	 * Set of atoms that occur in the head of some rule in the program.
	 */
	private Set<Atom> definedAtoms;
	/**
	 * Set of atoms that occur in the program (head or body).
	 */
	private Set<Atom> atomsInProgram;
	
	/**
	 * Creates a program with an empty List of clauses.
	 */
	public Program() {
		this.clauses = new ArrayList<Clause>();
		this.definedAtoms = new HashSet<Atom>();
		this.atomsInProgram = new HashSet<Atom>();
	}
	
	/**
	 * Creates a program with the given clauses.
	 * <br />
	 * Attributes 'definedAtoms' and 'atomsInProgram' are updated accordingly.
	 * 
	 * @see #setClauses(List)
	 * @param clauses
	 */
	public Program(final List<Clause> clauses) {
		setClauses(clauses);
	}
	
	/**
	 * Constructor to copy a program.
	 * @param clauses
	 * @param definedAtoms
	 * @param atomsInProgram
	 */
	private Program(final List<Clause> clauses, final Set<Atom> definedAtoms, final Set<Atom> atomsInProgram) {
		this.clauses = new ArrayList<Clause>();
		this.clauses.addAll(clauses);
		
		this.definedAtoms =  new HashSet<Atom>();
		this.definedAtoms.addAll(definedAtoms);
		
		this.atomsInProgram =  new HashSet<Atom>();
		this.atomsInProgram.addAll(atomsInProgram);
	}
	
	/**
	 * Given a list of clauses do the following:
	 * <ul>
	 * <li> Update this Program's instance clauses to the given list
	 * <li> Collect all the defined atoms and update {@link#definedAtoms}
	 * <li> Collect all the atoms that occur in the list of clauses and update {@link#atomsInProgram}
	 * @param clauses
	 */
	private void setClauses(final List<Clause> clauses) {
		
		this.clauses = clauses;
		
		this.definedAtoms = new HashSet<Atom>();
		this.atomsInProgram = new HashSet<Atom>();
		
		for(Clause cl: this.clauses) {
			Atom atomInHead = cl.getHeadAtom();
			this.definedAtoms.add(atomInHead);
			this.atomsInProgram.add(atomInHead);
			this.atomsInProgram.addAll(cl.getBodyAtoms());
		}
	}

	/**
	 * Check if a given clause is eligible to use to infer observations.
	 * <br />
	 * The eligibility criteria is defined in the config.file.
	 * <br />
	 * <br />
	 * Implemented criteria:
	 * <ol>
	 * <li> clause is an existential import;
	 * <li> clause is an unknown generalization;
	 * <li> clause is either an existential import or an unknown generalization.
	 * 
	 * <br />
	 * <br />
	 * TODO: If a class for clauses is created, move this method there.
	 * 
	 * @param clause
	 * @return True if this clause satisfies the criteria to be used to infer observations;
	 * false otherwise.
	 */
	public boolean isClauseForObservations(Clause clause) {
		
		boolean isClauseForObservations = false;
		
		if(clause.isFact()) {
			Integer objRef = clause.getHeadAtom().getObjectRef();
			
			switch (Main.cp.getObservationsCriteria()) {
			case "existentialImp":
				isClauseForObservations = (objRef % 2 == 1); break;
			case "unknownGen":
				isClauseForObservations = (objRef % 2 == 0); break;
			case "existentialUnknown":
				isClauseForObservations = true; break;
			case "foundedExistentialImport":
				boolean isExistentialImport = (objRef % 2 == 1);
				isClauseForObservations = isExistentialImport && !(this.isOnlyClauseWithThisHead(clause)); break;
			case "supportedFact":
				isClauseForObservations = !(this.isOnlyClauseWithThisHead(clause)); break;
			default: isClauseForObservations = false;
			}
			
		} 
		
		return isClauseForObservations;
	}
	
	/**	 
	 * @param clause
	 * @return True if there is no other clause in the program with the 
	 * atom's head of the given clause in its head.
	 */
	public boolean isOnlyClauseWithThisHead(final Clause clause) {
		boolean isHeadOfOther = false;
		for(Clause clauseP: this.clauses) {
			if(!clause.equals(clauseP))
				isHeadOfOther |= (clauseP.getHeadAtom()).equals(clause.getHeadAtom());
		}
		
		return !isHeadOfOther;
	}
	/**
	 * @return Set of atoms that occur in the head of clauses.
	 */
	public Set<Atom> getProgramHeadAtoms() {
		Set<Atom> newDefinedAtoms = new HashSet<Atom>();
		newDefinedAtoms.addAll(this.definedAtoms);
		return newDefinedAtoms;
	}
	
	/**
	 * @return Set of atoms that occur in the program.
	 */
	public Set<Atom> getAtomsInProgram() {
		Set<Atom> newAtomsInProgram = new HashSet<Atom>();
		newAtomsInProgram.addAll(this.atomsInProgram);
		return newAtomsInProgram;
	}
	
	/**
	 * @return List of clauses.
	 */
	public List<Clause> getClauses() {
		List<Clause> newClauses = new ArrayList<Clause>();
		newClauses.addAll(this.clauses);
		return newClauses;
	}
	
	
	/**
	 * @return All possible Atoms considering  all the predicates 
	 * and object references that occur in the program.
	 */
	public Set<Atom> allPossibleAtoms() {
		
		Set<Atom> atomsInProgram = this.atomsInProgram;
	
		Set<Predicate> predicatesInProgram = new HashSet<Predicate>();
		Set<Integer> objectsRefInProgram = new HashSet<Integer>();
		Set<Atom> allPossibleAtoms = new HashSet<Atom>();

		//Collect all predicates and object references
		//from list of atoms in the given program.
		for(Atom a: atomsInProgram) {
			predicatesInProgram.add(a.getPredicate());
			objectsRefInProgram.add(a.getObjectRef());
		}
		
		//Build all possible Atoms given 
		//all the predicates and object references
		//that occur in the program.
		for(Predicate p: predicatesInProgram) {
			for(Integer objRef: objectsRefInProgram) {
				allPossibleAtoms.add(new Atom(p, objRef));
			}
		}
		
		return allPossibleAtoms;
	}
	
	@Override
	public String toString() {
		StringBuilder resultProgram = new StringBuilder();
		clauses.forEach(c -> resultProgram
				.append(c.printClause() + "\n"));
		return resultProgram.toString();
	}

	@Override
	public Object clone() {
		return new Program(this.clauses, this.definedAtoms, this.atomsInProgram);
	}
	

}
