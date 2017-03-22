package common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Interpretation is defined by a set of {@link Atom}s that
 * are true and a set of {@link Atom}s that are false.
 * 
 * @author Ana Oliveira da Costa
 * @since 2016
 */
public class Interpretation {
	
	private static final int SPLIT_TRUE_POS = 0;
	private static final int SPLIT_FALSE_POS = 1;
	
	private Set<Atom> atomsTrue;
	private Set<Atom> atomsFalse;
	
	/**
	 * Creates a new Interpretation's instance with
	 * both sets of atoms that are true and atoms that are false empty.
	 */
	public Interpretation() {
		this.atomsTrue = new HashSet<Atom>();
		this.atomsFalse = new HashSet<Atom>();
	}
	
	/**
	 * An interpretation is build using the information in a file with the following format:
	 * <br \>
	 * <br \>
	 * <code>[LIST_OF_TRUE_ATOMS]-[LIST_OF_TRUE_ATOMS]\n
	 * <br \>
	 * LIST_OF_TRUE_ATOMS = ATOM
	 * <br \>
	 * LIST_OF_TRUE_ATOMS = ATOM,LIST_OF_TRUE_ATOMS
	 * <br \> 
	 * ATOM = PREDICATE(OBJECT_REF)
	 * <br \> 
	 * PREDICATE = Letter
	 * <br \>
	 * OBJECT_REF = 'o'Integer
	 * </code>
	 * <br/>
	 * <br/>
	 * Note: The interpretation should be only in one line.
	 * 
	 * @param pathFileModel Path to the file with the Interpretation data.
	 * @throws IOException When there is some problem with the file.
	 */
	public Interpretation(Path pathFileModel) throws IOException {
		
		this.atomsTrue = new HashSet<Atom>();
		this.atomsFalse = new HashSet<Atom>();
		
		//Get line with interpretation and remove '[' and ']'.
		String model_file = Files.readAllLines(pathFileModel).get(0);
		
		//Split lists of true and false atoms.
		String[] evaluation = model_file.split("-");
		
		//Get lists with strings of true and false atoms.
		String[] evaluation_true = (evaluation[SPLIT_TRUE_POS].equals("[]")) ? 
				new String[0] : evaluation[SPLIT_TRUE_POS].replace("[", "").replace("]", "").split(",");
		String[] evaluation_false = (evaluation[SPLIT_FALSE_POS].equals("[]")) ? 
				new String[0] : evaluation[SPLIT_FALSE_POS].replace("[", "").replace("]", "").split(",");
		
		//Parse and save the true and false atoms.
		if(evaluation_true.length != 0) {
			for(String true_atom: evaluation_true)
				atomsTrue.add(new Atom(true_atom));
		}
		
		if(evaluation_false.length != 0) {
			for(String false_atom: evaluation_false)
				atomsFalse.add(new Atom(false_atom));
		}
		
	}

	/**
	 * Add an {@link Atom} to the list of true atoms in this interpretation.
	 * @param atom The atom to add.
	 */
	public void addTrueAtom(final Atom atom) {
		atomsTrue.add(atom);
	}
	
	/**
	 * Add an {@link Atom} to the list of false atoms in this interpretation.
	 * @param atom The atom to add.
	 * */
	public void addFalseAtom(final Atom atom) {
		atomsFalse.add(atom);
	}
	
	/**
	 * Remove an {@link Atom} to the list of true atoms in this interpretation.
	 * @param atom The atom to remove.
	 */
	public void removeTrueAtom(final Atom atom) {
		atomsTrue.remove(atom);
	}
	
	/**
	 * Remove an {@link Atom} to the list of false atoms in this interpretation.
	 * @param atom The atom to remove.
	 * */
	public void removeFalseAtom(final Atom atom) {
		atomsFalse.remove(atom);
	}
	
	/**
	 * @param p Predicate to consider.
	 * @param listOfAtoms List to get the information from.
	 * @return Objects' references that occur in <code>listOfAtoms</code>
	 * in Predicate <code>p</code>
	 */
	private List<Integer> getObjectsOfPredicate(final Predicate p, final Set<Atom> listOfAtoms) {
		List<Integer> objects = new ArrayList<Integer>();

		for (Atom a : listOfAtoms) {
			if (a.isSamePredicate(p))
				objects.add(a.getObjectRef());
		}

		return objects;
	}
	
	 
	/**
	 * @param p The predicate to consider.
	 * @return All object references that are true under this interpretation
	 * given the predicate <code>p</code>.
	 */
	public List<Integer> getTrueObjectsOfPredicate(final Predicate p) {
		return getObjectsOfPredicate(p, atomsTrue);
	}
	
	/**
	 * @param p The predicate to consider.
	 * @return All object references that are false under this interpretation
	 * given the predicate <code>p</code>.
	 */
	public List<Integer> getFalseObjectsOfPredicate(final Predicate p) {
		return getObjectsOfPredicate(p, atomsFalse);
	}
	
	
	

	/**
	 * @return Deep copy of the Set of Atoms that are true under this interpretation.
	 */
	public Set<Atom> getAtomsTrue() {
		Set<Atom> newAtomsTrue = new HashSet<Atom>();
		newAtomsTrue.addAll(this.atomsTrue);
		return newAtomsTrue;
	}

	/**
	 * @return Deep copy of the Set of Atoms that are false under this interpretation.
	 */
	public Set<Atom> getAtomsFalse() {
		Set<Atom> newAtomsFalse = new HashSet<Atom>();
		newAtomsFalse.addAll(this.atomsFalse);
		return newAtomsFalse;	
	}


	@Override
	public String toString() {
		StringBuilder interpretation = new StringBuilder("True: ");
		atomsTrue.forEach(a->interpretation.append(a.toString()));
		
		interpretation.append("\nFalse: ");
		atomsFalse.forEach(a->interpretation.append(a.toString()));
		
		return interpretation.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atomsFalse == null) ? 0 : atomsFalse.hashCode());
		result = prime * result + ((atomsTrue == null) ? 0 : atomsTrue.hashCode());
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
		Interpretation other = (Interpretation) obj;
		if (atomsFalse == null) {
			if (other.atomsFalse != null)
				return false;
		} else if (!atomsFalse.equals(other.atomsFalse))
			return false;
		if (atomsTrue == null) {
			if (other.atomsTrue != null)
				return false;
		} else if (!atomsTrue.equals(other.atomsTrue))
			return false;
		return true;
	}

}
