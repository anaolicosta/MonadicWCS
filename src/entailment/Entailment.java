package entailment;


import common.Interpretation;
import main.Main;

public class Entailment {
	
	private static Entails currentEntailment;
	
	private static Entails getEntailment() {
		if(currentEntailment == null) {
			switch(Main.cp.getEntailment()) {
			case "EntailmentPaper":
				currentEntailment = new EntailmentPaper(); break;
			case "EntailmentProlog":
				currentEntailment = new EntailmentProlog(); break;
			default:
				currentEntailment = new EntailmentPaper(); break;
			}
		}
		
		return currentEntailment;
	}

	private Entailment() {}
	
	public static Conclusions entails(Interpretation interpretation) {
		return getEntailment().entails(interpretation);
	}
}
