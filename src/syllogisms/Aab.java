package syllogisms;

/**
 * All y are z.
 * @author Emma
 *
 */
public class Aab {
	

	public static int nrOfPatterns = 2;
	
	public String getAll(String a, String b, String objectOne, String objectTwo, String objectThree, String objectFour, int patternAab){
		
		if(patternAab == 1){
			return thesisVersionAll(a, b, objectOne, objectTwo);
		}
		if(patternAab == 2){
			return thesisVersionAll(a, b, objectOne, objectTwo) + "\n" + 
		thesisVersionAll(b, a, objectThree, objectFour);
		}
		return null;
		
	}
	
	private String thesisVersionAll(String A, String B, String objectOne, String objectTwo){
		
		String comment = "% All " + A + " are " + B + "."; 
		
		String cl1 = "clause(" + B + "(X)" + " :- " + "[" + A + "(X)" +"," + "n(ab" + A + B + "(X))]" + ")" + ".";

		String cl2 = "clause(ab" + A + B + "(X) :- [f]).";
		
		String cl3 = "clause(" + A + "(" + objectOne + ")" + " :- " + "[t]" + ").";
		
		return comment + "\n" + cl1 + "\n" + cl2 + "\n" + cl3;
		
	}
}
