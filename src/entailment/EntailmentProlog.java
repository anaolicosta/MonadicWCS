package entailment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import common.Atom;
import common.Interpretation;
import common.Predicate;
import main.FileWatcher;

public class EntailmentProlog implements Entails {

	private static final String EXPERIMENTS_BASE_DIR_ENTAILMENT = "syllogisms/entailment/";

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
		
		//Quantified Assertions
		//ALL
		entailmentProgram.append("% All a are c" + "\n");
		entailmentProgram.append("clause(aac(e1) :- [ruleac(e1),norefuteac(e1),n(abaac(e1))]).\n");
		entailmentProgram.append("clause(abaac(e1) :- [f]).\n");
		
		entailmentProgram.append("\n% All c are a" + "\n");
		entailmentProgram.append("clause(aca(e1) :- [ruleca(e1),norefuteca(e1),n(abaca(e1))]).\n");
		entailmentProgram.append("clause(abaca(e1) :- [f]).\n");

		//NONE
		entailmentProgram.append("\n% No a are c" + "\n");
		entailmentProgram.append("clause(eac(e1) :- [ruleacneg(e1),norefuteacneg(e1),n(abeac(e1))]).\n");
		entailmentProgram.append("clause(abeac(e1) :- [f]).\n");

		entailmentProgram.append("\n% No c are a" + "\n");
		entailmentProgram.append("clause(eca(e1) :- [rulecaneg(e1),norefutecaneg(e1),n(abeca(e1))]).\n");
		entailmentProgram.append("clause(abeca(e1) :- [f]).\n");

		//SOME
		entailmentProgram.append("\n% Some a are c" + "\n");
		entailmentProgram.append("clause(iac(e1) :- [ruleca(e1),ruleac(e1),ugac(e1),ugca(e1),n(abiac(e1))]).\n");
		entailmentProgram.append("clause(abiac(e1) :- [f]).\n");

		entailmentProgram.append("\n% Some c are a" + "\n");
		entailmentProgram.append("clause(ica(e1) :- [ruleca(e1),ruleac(e1),ugca(e1),ugac(e1),n(abica(e1))]).\n");
		entailmentProgram.append("clause(abica(e1) :- [f]).\n");

		//SOME ARE NOT
		entailmentProgram.append("\n% Some a are not c" + "\n");
		entailmentProgram.append("clause(oac(e1) :- [ruleacneg(e1),uganc(e1),n(aboac(e1))]).\n");
		entailmentProgram.append("clause(aboac(e1) :- [f]).\n");

		entailmentProgram.append("\n% Some c are not a" + "\n");
		entailmentProgram.append("clause(oca(e1) :- [rulecaneg(e1),ugcna(e1),n(aboca(e1))]).\n");
		entailmentProgram.append("clause(aboca(e1) :- [f]).\n");
	}
		
	
	
	public Conclusions entails(Interpretation interpretation) {
		
		Conclusions conc = new Conclusions();

		String fileBaseName = EXPERIMENTS_BASE_DIR_ENTAILMENT;
		
		String entailPath = fileBaseName;
		File theDir = new File(entailPath);
		
		


		if (!theDir.exists()) {
			//Try to create directory
		    try{
		        theDir.mkdir();	
		    } catch(SecurityException se){
		        System.out.println("Failed to create dir: " + theDir.toString());
		    }          
		} else {
			//Delete old files
			File[] listFiles = theDir.listFiles();
			for(File dirExpBlocks: listFiles) {
				dirExpBlocks.delete();	
			}	
		}
		
		//Print Prolog program
		try {
			PrintWriter writer = new PrintWriter(fileBaseName + "entailment.pl", "UTF-8");
			String firstProgram = getProgramWithModel(interpretation);
			writer.println(firstProgram);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String experimentPath = fileBaseName + "entailment";
		Path pathInterpretationFile = Paths.get(experimentPath + "glm.pl");

		FileWatcher watcher = new FileWatcher(Paths.get(entailPath));
		
		ProcessBuilder pb = new ProcessBuilder("resources/run.sh", "entailment/entailment");
		
		try {
			pb.start();
			watcher.watch(pathInterpretationFile);
		} catch (IOException e) {
			System.out.println(
					"Failed to wacth file in path " + pathInterpretationFile + "\nMessage: " + e.getMessage());
		}
		
		
		watcher.close();
		
		try {
			
			Interpretation i = new Interpretation(pathInterpretationFile);
			
//			System.out.print(" Entailed: ");
			for(Atom a: i.getAtomsTrue()) {
				Predicate predicate = a.getPredicate();
				if(Predicate.getPredicateEntailment().contains(predicate)) {
//					System.out.print(a + " ");
					conc.addConclusion(Conclusion.getConclusionFromEntailmentPredicate(predicate));
				}
			}
			
//			System.out.print("\n False: ");
//
//			boolean iDontknow = true;
//			for(Atom a: i.getAtomsFalse()) {
//				Predicate predicate = a.getPredicate();
//				if(Predicate.getPredicateEntailment().contains(predicate)) {
//					System.out.print(a + " ");
//					iDontknow = false;
//				}
//			}
			
			if (conc.isEmpty()) conc.addConclusion(Conclusion.NVC);
			
//			if (iDontknow) System.out.println("! I don't know ! ");
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return conc;
		
	}
	
	
		
	public static String getProgramWithModel(final Interpretation interpretation){
		
		StringBuilder newProgram = new StringBuilder(entailmentProgram);
		
		Set<Atom> atomsTrue = interpretation.getAtomsTrue();
		Set<Atom> atomsFalse = interpretation.getAtomsFalse();
		
		//Set of defined and undefined atoms are used to later
		//encode information about relevant unknown evaluation.
		Set<Atom> defAtoms = new HashSet<Atom>();
		Set<Atom> undefAtoms = new HashSet<Atom>();
		
		//Add clauses for atoms in True Evaluation:
		//Add fact + Assumption that the atom is not undefined
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
		
		//Add clauses for atoms in False Evaluation:
		//Add assumption + Assumption that the atom is not undefined
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
		
		//Encode relevant unknown evaluation.
		for(Atom atom: undefAtoms) {
			newProgram.append("clause(u" + atom.toString().toLowerCase() + " :- [t]).\n");
		}
		
		return newProgram.toString();
	}




	@Override
	public boolean isAll(Interpretation model, Predicate firstPredicate, Predicate secondPredicate) {
		// TODO Auto-generated method stub
		return false;
	}




	@Override
	public boolean isSome(Interpretation model, Predicate firstPredicate, Predicate secondPredicate) {
		// TODO Auto-generated method stub
		return false;
	}




	@Override
	public boolean isNone(Interpretation model, Predicate firstPredicate, Predicate secondPredicate) {
		// TODO Auto-generated method stub
		return false;
	}




	@Override
	public boolean isSomeAreNot(Interpretation model, Predicate firstPredicate, Predicate secondPredicate) {
		// TODO Auto-generated method stub
		return false;
	}
		
}
