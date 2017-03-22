package entailment;


import common.Interpretation;
import common.Mood;
import main.Main;

public class Entailment {
	
	private static Entails currentEntailment;
	
	private static Entails getEntailment() {
		if(currentEntailment == null) {
			switch(Main.cp.getEntailment()) {
			case "EntailmentPaper":
				currentEntailment = new EntailmentPaper(); break;
			case "EntailmentTotalUniversal":
				currentEntailment = new EntailmentTotalUniversal(); break;
			case "EntailmentUniversalComplement":
				currentEntailment = new EntailmentUniversalComplement(); break;
			case "EntailmentPriority":
				currentEntailment = new EntailmentPriority(); break;
			default:
				currentEntailment = new EntailmentPaper(); break;
			}
		}
		
		return currentEntailment;
	}

	private Entailment() {}
	
	public static Conclusions entails(Interpretation interpretation, Mood weakerMood) {
		return getEntailment().entails(interpretation, weakerMood);
	}
}
