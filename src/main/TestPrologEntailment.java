package main;

import java.util.HashSet;
import java.util.Set;

import common.Atom;
import common.Interpretation;
import common.Mood;
import common.Predicate;

public class TestPrologEntailment {

	private static StringBuilder entailmentProgram;
	
	static {
		entailmentProgram = new StringBuilder();
		entailmentProgram.append("% Abdnormality for inferences from a to c" + "\n");
		entailmentProgram.append("clause(abac(X) :- [ua(X)] ).\n");
		entailmentProgram.append("% Abdnormality for inferences from c to a" + "\n");
		entailmentProgram.append("clause(abca(X) :- [uc(X)]).\n");

		entailmentProgram.append("\n% Unknow value for object e1" + "\n");
		entailmentProgram.append("clause(ua(e1) :- [t]).\n");
		entailmentProgram.append("clause(uc(e1) :- [t]).\n\n");
		
		// PRINCIPLES
		entailmentProgram.append("% Rule with import" + "\n");
		entailmentProgram.append("clause(ruleac(e1) :- [a(X),c(X),n(abac(X))]).\n");
		entailmentProgram.append("clause(ruleca(e1) :- [c(X),a(X),n(abca(X))]).\n");
		entailmentProgram.append("clause(ruleacneg(e1) :- [a(X),n(c(X)),n(abac(X))]).\n");
		entailmentProgram.append("clause(rulecaneg(e1) :- [c(X),n(a(X)),n(abca(X))]).\n");

		entailmentProgram.append("% No Refutation" + "\n");
		entailmentProgram.append("clause(norefuteac(e1) :- [n(refuteac(e1))]).\n");
		entailmentProgram.append("clause(refuteac(e1) :- [a(X),n(c(X)),n(abac(X))]).\n");
		entailmentProgram.append("clause(norefuteca(e1) :- [n(refuteca(e1))]).\n");
		entailmentProgram.append("clause(refuteca(e1) :- [c(X),n(a(X)),n(abca(X))]).\n");
		
		entailmentProgram.append("clause(norefuteacneg(e1) :- [n(refuteacneg(e1))]).\n");
		entailmentProgram.append("clause(refuteacneg(e1) :- [a(X),c(X),n(abac(X))]).\n");
		entailmentProgram.append("clause(norefutecaneg(e1) :- [n(refutecaneg(e1))]).\n");
		entailmentProgram.append("clause(refutecaneg(e1) :- [c(X),a(X),n(abca(X))]).\n");

		
		entailmentProgram.append("% Unknown Generalization " + "\n");
		entailmentProgram.append("clause(ugac(e1) :- [a(X),uc(X),n(abac(X))]).\n");
		entailmentProgram.append("clause(ugac(e1) :- [a(X),n(c(X)),n(abac(X))]).\n");
		entailmentProgram.append("clause(ugca(e1) :- [c(X),ua(X),n(abca(X))]).\n");
		entailmentProgram.append("clause(ugca(e1) :- [c(X),n(a(X)),n(abca(X))]).\n");
		entailmentProgram.append("clause(uganc(e1) :- [a(X),c(X),n(abac(X))]).\n");
		entailmentProgram.append("clause(uganc(e1) :- [a(X),uc(X),n(abac(X))]).\n");
		entailmentProgram.append("clause(ugcna(e1) :- [c(X),a(X),n(abca(X))]).\n");
		entailmentProgram.append("clause(ugcna(e1) :- [c(X),ua(X),n(abca(X))]).\n");
		
		
		entailmentProgram.append("% All a are c" + "\n");
		entailmentProgram.append("clause(aac(e1) :- [ruleac(e1),norefuteac(e1),n(abaac(e1))]).\n");
		//entailmentProgram.append("clause(abaac(e1) :- [n(norefuteac(e1))]).\n");
		entailmentProgram.append("clause(abaac(e1) :- [f]).\n");
		
		entailmentProgram.append("\n% All c are a" + "\n");
		entailmentProgram.append("clause(aca(e1) :- [ruleca(e1),norefuteca(e1),n(abaca(e1))]).\n");
		//entailmentProgram.append("clause(abaca(e1) :- [n(norefuteca(e1))]).\n");
		entailmentProgram.append("clause(abaca(e1) :- [f]).\n");

		
		
		entailmentProgram.append("\n% No a are c" + "\n");
		entailmentProgram.append("clause(eac(e1) :- [ruleacneg(e1),norefuteacneg(e1),n(abeac(e1))]).\n");
		//entailmentProgram.append("clause(abeac(e1) :- [n(norefuteacneg(e1))]).\n");
		entailmentProgram.append("clause(abeac(e1) :- [f]).\n");

		
		entailmentProgram.append("\n% No c are a" + "\n");
		entailmentProgram.append("clause(eca(e1) :- [rulecaneg(e1),norefutecaneg(e1),n(abeca(e1))]).\n");
		//entailmentProgram.append("clause(abeca(e1) :- [n(norefutecaneg(e1))]).\n");
		entailmentProgram.append("clause(abeca(e1) :- [f]).\n");

		
	
		entailmentProgram.append("\n% Some a are c" + "\n");
		entailmentProgram.append("clause(iac(e1) :- [ruleca(e1),ruleac(e1),ugac(e1),ugca(e1),n(abiac(e1))]).\n");
		//entailmentProgram.append("clause(abiac(e1) :- [n(ugac(e1))]).\n");
		//entailmentProgram.append("clause(abiac(e1) :- [n(ugca(e1))]).\n");
		entailmentProgram.append("clause(abiac(e1) :- [f]).\n");


		entailmentProgram.append("\n% Some c are a" + "\n");
		entailmentProgram.append("clause(ica(e1) :- [ruleca(e1),ruleac(e1),ugca(e1),ugac(e1),n(abica(e1))]).\n");
		//entailmentProgram.append("clause(abica(e1) :- [n(ugca(e1))]).\n");
		//entailmentProgram.append("clause(abica(e1) :- [n(ugac(e1))]).\n");
		entailmentProgram.append("clause(abica(e1) :- [f]).\n");

		
		
		entailmentProgram.append("\n% Some a are not c" + "\n");
		entailmentProgram.append("clause(oac(e1) :- [ruleacneg(e1),uganc(e1),n(aboac(e1))]).\n");
		//entailmentProgram.append("clause(aboac(e1) :- [n(uganc(e1))]).\n");
		entailmentProgram.append("clause(aboac(e1) :- [f]).\n");

		
		entailmentProgram.append("\n% Some c are not a" + "\n");
		entailmentProgram.append("clause(oca(e1) :- [rulecaneg(e1),ugcna(e1),n(aboca(e1))]).\n");
		//entailmentProgram.append("clause(aboca(e1) :- [n(ugcna(e1))]).\n");
		entailmentProgram.append("clause(aboca(e1) :- [f]).\n");

		
	}
	

	
	public static String getProgramWithModel(final Interpretation interpretation){
	
		StringBuilder newProgram = new StringBuilder(entailmentProgram);
		
		Set<Atom> atomsTrue = interpretation.getAtomsTrue();
		Set<Atom> atomsFalse = interpretation.getAtomsFalse();
		
		Set<Atom> defAtoms = new HashSet<Atom>();
		Set<Atom> undefAtoms = new HashSet<Atom>();
		for(Atom atom: atomsTrue) {
			if(atom.getPredicate().equals(Predicate.A) || atom.getPredicate().equals(Predicate.C)) {
				newProgram.append("clause(" + atom.toString().toLowerCase() + " :- [t]).\n");
				newProgram.append("clause(u" + atom.toString().toLowerCase() + " :- [f]).\n");
				
				defAtoms.add(atom);
				undefAtoms.remove(atom);
				
				if(atom.getPredicate().equals(Predicate.A)) {
					Atom c = new Atom(Predicate.C, atom.getObjectRef());
					if(!defAtoms.contains(c)) undefAtoms.add(c);
				} else {
					Atom a = new Atom(Predicate.A, atom.getObjectRef());
					if(!defAtoms.contains(a)) undefAtoms.add(a);
				}
			}
		}
		
		for(Atom atom: atomsFalse) {
			if(atom.getPredicate().equals(Predicate.A) || atom.getPredicate().equals(Predicate.C)) {

				newProgram.append("clause(" + atom.toString().toLowerCase() + " :- [f]).\n");
				newProgram.append("clause(u" + atom.toString().toLowerCase() + " :- [f]).\n");

				
				defAtoms.add(atom);
				undefAtoms.remove(atom);
				
				if(atom.getPredicate().equals(Predicate.A)) {
					Atom c = new Atom(Predicate.C, atom.getObjectRef());
					if(!defAtoms.contains(c)) undefAtoms.add(c);
				} else {
					Atom a = new Atom(Predicate.A, atom.getObjectRef());
					if(!defAtoms.contains(a)) undefAtoms.add(a);
				}
			}
		}
		
		for(Atom atom: undefAtoms) {
			newProgram.append("clause(u" + atom.toString().toLowerCase() + " :- [t]).\n");
//			int o = atom.getObjectRef();
//			Predicate p = atom.getPredicate();
//			if (Predicate.A.equals(p)) {
//				newProgram.append("% Abdnormality for inferences from a to c" + "\n");
//				newProgram.append("clause(abac(o" + o +") :- [t]).\n");
//			} else if (Predicate.C.equals(p)) {
//				newProgram.append("% Abdnormality for inferences from c to a" + "\n");
//				newProgram.append("clause(abca(o" + o +") :- [t]).\n");
//			}

		}
		
		return newProgram.toString();
	}
	
	public static String addWeaker(String program, Mood weaker) {
		StringBuilder toAdd = new StringBuilder(program);
	
		switch(weaker) {
		case A: 
			toAdd.append("clause(abiac(e1) :- [aac(e1)]).\n");
			toAdd.append("clause(abica(e1) :- [aca(e1)]).\n");
			toAdd.append("clause(abiac(e1) :- [aca(e1)]).\n");
			toAdd.append("clause(abica(e1) :- [aac(e1)]).\n");
			
			toAdd.append("clause(abeac(e1) :- [aac(e1)]).\n");
			toAdd.append("clause(abeca(e1) :- [aca(e1)]).\n");
			toAdd.append("clause(abeac(e1) :- [aca(e1)]).\n");
			toAdd.append("clause(abeca(e1) :- [aaa(e1)]).\n");
			
			toAdd.append("clause(aboac(e1) :- [aac(e1)]).\n");
			toAdd.append("clause(aboca(e1) :- [aca(e1)]).\n"); 
			toAdd.append("clause(aboac(e1) :- [aca(e1)]).\n");
			toAdd.append("clause(aboca(e1) :- [aac(e1)]).\n"); 
			break;

		case I:
			toAdd.append("clause(abaac(e1) :- [iac(e1)]).\n");
			toAdd.append("clause(abaca(e1) :- [ica(e1)]).\n");
			toAdd.append("clause(abaac(e1) :- [ica(e1)]).\n");
			toAdd.append("clause(abaca(e1) :- [iac(e1)]).\n");
			
			toAdd.append("clause(abeac(e1) :- [iac(e1)]).\n");
			toAdd.append("clause(abeca(e1) :- [ica(e1)]).\n");
			toAdd.append("clause(abeac(e1) :- [ica(e1)]).\n");
			toAdd.append("clause(abeca(e1) :- [iac(e1)]).\n");
			
			toAdd.append("clause(aboac(e1) :- [iac(e1)]).\n");
			toAdd.append("clause(aboca(e1) :- [ica(e1)]).\n"); 
			toAdd.append("clause(aboac(e1) :- [ica(e1)]).\n");
			toAdd.append("clause(aboca(e1) :- [iac(e1)]).\n");
			break;
			
		case E: 
			toAdd.append("clause(abaac(e1) :- [eac(e1)]).\n");
			toAdd.append("clause(abaca(e1) :- [eca(e1)]).\n");
			toAdd.append("clause(abaac(e1) :- [eca(e1)]).\n");
			toAdd.append("clause(abaca(e1) :- [eac(e1)]).\n");
			
			toAdd.append("clause(abiac(e1) :- [eac(e1)]).\n");
			toAdd.append("clause(abica(e1) :- [eca(e1)]).\n");
			toAdd.append("clause(abiac(e1) :- [eca(e1)]).\n");
			toAdd.append("clause(abica(e1) :- [eac(e1)]).\n");
			
			toAdd.append("clause(aboac(e1) :- [eac(e1)]).\n");
			toAdd.append("clause(aboca(e1) :- [eca(e1)]).\n"); 
			toAdd.append("clause(aboac(e1) :- [eca(e1)]).\n");
			toAdd.append("clause(aboca(e1) :- [eac(e1)]).\n");
			break;
			
		case O:
			toAdd.append("clause(abaac(e1) :- [oac(e1)]).\n");
			toAdd.append("clause(abaca(e1) :- [oca(e1)]).\n");
			toAdd.append("clause(abaac(e1) :- [oca(e1)]).\n");
			toAdd.append("clause(abaca(e1) :- [oac(e1)]).\n");
			
			toAdd.append("clause(abeac(e1) :- [oac(e1)]).\n");
			toAdd.append("clause(abeca(e1) :- [oca(e1)]).\n");
			toAdd.append("clause(abeac(e1) :- [oca(e1)]).\n");
			toAdd.append("clause(abeca(e1) :- [oac(e1)]).\n");
			
			toAdd.append("clause(abiac(e1) :- [oac(e1)]).\n");
			toAdd.append("clause(abica(e1) :- [oca(e1)]).\n"); 
			toAdd.append("clause(abiac(e1) :- [oca(e1)]).\n");
			toAdd.append("clause(abica(e1) :- [oac(e1)]).\n");
			break;
		default: break;
		}
		
		return toAdd.toString();
	}
	
}
