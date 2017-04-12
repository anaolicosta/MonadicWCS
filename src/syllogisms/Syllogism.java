package syllogisms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import common.Atom;
import common.Interpretation;
import common.ModelAndEntailment;
import entailment.Conclusions;
import experiments.ExperimentBlock;
import program.Clause;
import program.ClauseType;
import program.Program;

/**
 * A Syllogism is <b>uniquely</b> identified by its name that must be 
 * an Enum value from {@link SyllogismEnum}.
 * <br \>
 * This class is responsible to manage all relevant information for the experiments
 * of the syllogism it represents.
 * <br />
 * Relevant information is:
 * <ul>
 * <li> State before the experiments:
 * <ul>
 * <li> Original program 
 *      ({@link #getProgram()}, {@link #setProgram(List)})
 * <li> Original interpretation and conclusions
 * 		({@link #setModelAndEntailment(ModelAndEntailment)}, {@link #setModelAndEntailment(Interpretation)}
 * 		{@link #getmodelAndEntailment()})
 * <li> Original WCS accuracy
 * 		({@link #getPrecisionOriginalProgram()})
 * </ul>
 * 
 * <li> Whether abduction should be applied to this syllogism 
 * 		({@link #isToTestAbduction()})
 * 
 * <li> List of experiment blocks
 *      ({@link Syllogism#getExperimentBlocks()}, {@link #setExperimentBlocks(List)})
 *      
 * <li> Final state:
 * <ul>
 * <li> Conclusions (Credulous and Skeptical)
 *      ({@link #getCredoulousConclusions()}, {@link #setCredoulousConclusions(Conclusions)},
 *      {@link #getSkepticalConclusions()}, {@link #setCredoulousConclusions(Conclusions)})
 * 
 * </ul>
 * <br />
 * <b> Implementation details: </b>
 * <br />
 * To get the representation of a syllogism use 
 * {@link #getSyllogism(SyllogismEnum)}.
 * <br />
 * Original conclusions are updated when the original interpretation is updated 
 * ({@link #setModelAndEntailment(Interpretation)}, {@link #setModelAndEntailment(ModelAndEntailment)}).
 * 
 * @author Ana Oliveira da Costa
 * @since 2016
 *
 */
public class Syllogism  {

	
	/** TODO: Consider moving to Program. */
	private static final String REGEX_BEGIN_BODY = "\\[";
	private static final String REGEX_END_CLAUSE = "\\]\\)\\).";
	private static final String REGEX_IMPLICATION = ":-";
	private static final String REGEX_START_CLAUSE = "\\(\\(";
	private static final String TRUE = "t";
	private static final String FALSE = "f";
	
	/* STATIC. Map of syllogisms name to syllogism instances. */
	private static Map<SyllogismEnum, Syllogism> listOfSyllogisms = new HashMap<SyllogismEnum, Syllogism>();
	
	public static double originalOverallPrecision;
	
	public static double credoulousOverallPrecision;
	
	public static double SkepticalOverallPrecision;
	
	public static Syllogism getSyllogism(final SyllogismEnum syllogismName) {
		if(!listOfSyllogisms.containsKey(syllogismName)) {
			listOfSyllogisms.put(syllogismName, new Syllogism(syllogismName));
		}
		
		return listOfSyllogisms.get(syllogismName);
	}
	
	public static String toStringMapSyllogism(){
		StringBuilder result = new StringBuilder();
		listOfSyllogisms.forEach((e,s) -> result.append(s.toStringWithoutProgram()));
		return result.toString();
	}
	
	public static void clear(){
		listOfSyllogisms = new HashMap<SyllogismEnum, Syllogism>();
	}
	
	
	/* INSTANCES. */
	private SyllogismEnum syllogismName;
	
	private Program originalProgram;
	private ModelAndEntailment originalModelAndEntailment;
	private double precisionOriginalProgram;
	
	private boolean isToTestAbduction;
		
	private List<ExperimentBlock> experimentBlocks;
		
	private Conclusions credoulousConclusions;
	private Conclusions skepticalConclusions;
	
	private Syllogism(final SyllogismEnum syllogismName) {
		this.syllogismName = syllogismName;
		this.originalProgram = new Program();
		this.originalModelAndEntailment = new ModelAndEntailment();
		this.experimentBlocks = new ArrayList<ExperimentBlock>();
		this.isToTestAbduction = false;
		this.precisionOriginalProgram = 0.0;
		this.credoulousConclusions = new Conclusions();
		this.skepticalConclusions = new Conclusions();
	}

	/* GETs and SETs. */
	public ModelAndEntailment getmodelAndEntailment() {
		return originalModelAndEntailment;
	}

	public void setModelAndEntailment(ModelAndEntailment modelAndEntailment) {
		this.originalModelAndEntailment = modelAndEntailment;
		this.isToTestAbduction = modelAndEntailment.getConclusions().hasNVC();
	}
	
	public void setModelAndEntailment(Interpretation interpretation) {
		this.originalModelAndEntailment = new ModelAndEntailment(interpretation);
		this.precisionOriginalProgram = evaluatePrecision() / 9;
		this.isToTestAbduction = this.originalModelAndEntailment.getConclusions().hasNVC();
	}

	public Program getProgram() {
		return originalProgram;
	}

	public void setProgram(List<String> programClauses) {
		
		List<Clause> clauses = new ArrayList<Clause>();
		for(String line: programClauses) {
			String[] lineParts = line.split(REGEX_START_CLAUSE);
			String[] clauseParts = lineParts[1].split(REGEX_IMPLICATION);

			Atom atomInHead = new Atom(clauseParts[0]);
			
			String body = clauseParts[1].replaceAll(REGEX_BEGIN_BODY, "").replaceAll(REGEX_END_CLAUSE, "");
			String[] bodyParts = body.split(",");
			ClauseType type;
			String firstAtom = bodyParts[0];
			if(firstAtom.startsWith(FALSE)) {
				type = ClauseType.ASSUMPTION;
			}
			else if (firstAtom.startsWith(TRUE)) {
				type = ClauseType.FACT;
			} else {
				type = ClauseType.RULE;
			}
			
			clauses.add(new Clause(atomInHead, type, body));
		}
		
		this.originalProgram = new Program(clauses);
		
	}
	
	public void setProgram(Program program) {
		this.originalProgram = program;
	}
	
	public List<ExperimentBlock> getExperimentBlocks() {
		List<ExperimentBlock> newExpBlock = new ArrayList<ExperimentBlock>();
		newExpBlock.addAll(this.experimentBlocks);
		return newExpBlock;
	}

	public void setExperimentBlocks(List<ExperimentBlock> experimentBlock) {
		this.experimentBlocks = new ArrayList<ExperimentBlock>();
		this.experimentBlocks.addAll(experimentBlock);
	}
	
	public boolean isToTestAbduction(){
		return isToTestAbduction;
	}
	
	
	public SyllogismEnum getSyllogismName() {
		return syllogismName;
	}
	
	public Conclusions getCredoulousConclusions() {
		return credoulousConclusions;
	}

	public void setCredoulousConclusions(Conclusions credoulousConclusions) {
		this.credoulousConclusions = credoulousConclusions;
	}

	public Conclusions getSkepticalConclusions() {
		return skepticalConclusions;
	}

	public void setSkepticalConclusions(Conclusions skepticalConclusions) {
		this.skepticalConclusions = skepticalConclusions;
	}
	
	public double getPrecisionOriginalProgram() {
		return precisionOriginalProgram;
	}

	private double evaluatePrecision(){
		return evaluatePrecision(this.getmodelAndEntailment().getConclusions());
	}
	
	public double evaluatePrecision(Conclusions conclusions){
		return syllogismName.peopleConclusions.matchingConclusions(conclusions);
	}
	
	public double evaluateAccuracy(Conclusions conclusions){
		return (conclusions.isEmpty()) ? this.precisionOriginalProgram : evaluatePrecision(conclusions) / 9.0;
	}

	public double credoulousAccuracy(){
		return (this.credoulousConclusions.isEmpty()) ? this.precisionOriginalProgram
				: syllogismName.peopleConclusions.matchingConclusions(this.credoulousConclusions) / 9.0;
	}
	
	public double skepticalAccuracy(){
		return (this.skepticalConclusions.isEmpty()) ? this.precisionOriginalProgram
				: syllogismName.peopleConclusions.matchingConclusions(this.skepticalConclusions) / 9.0;
	}
	
	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder("\n[" + syllogismName.toString() + "]\n");
		toString.append(originalProgram.toString() + "\n");
		toString.append(originalModelAndEntailment.toString() + "\n");
		toString.append("Precision: " + precisionOriginalProgram);
		return toString.toString();
	}
	
	public String toStringWithoutProgram() {
		StringBuilder toString = new StringBuilder("\n[" + syllogismName.toString() + "]\n");
		toString.append(originalModelAndEntailment.toString() + "\n");
		toString.append("Precision: " + precisionOriginalProgram);
		toString.append("\n-> Experiment Blocks");
		experimentBlocks.forEach(eb-> toString.append(eb.toString()));
		return toString.toString();
	}

}
