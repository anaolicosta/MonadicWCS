package syllogisms;


/**
 * No y are z.
 * @author Emma
 *
 */
public class Eab {



	public static int nrOfPatterns = 2;
	
	public String getNone(String a, String b, String objectOne, String objectTwo, String objectThree, String objectFour, int patternEab){

		if (patternEab == 1){	
			return thesisVersionNone(a,b, objectOne, objectTwo);
			} 
		if (patternEab == 2){	
			return thesisVersionNone(a,b, objectOne, objectTwo) + "\n" + 
		thesisVersionNone(b,a, objectThree, objectFour);
			}
		return null;
	}
	
	
	private String thesisVersionNone(String A, String B, String objectOne, String objectTwo){
		
		String comment = "% No " + A + " is " + B + ".";
		
		String cl1 = "clause(" + "n" + B  + "(X)" + " :- [" + A + "(X)" + "," + "n(ab" + A + "n" +  B + "(X))]" + ")" + ".";
		
		String cl2 = "clause(ab" + A + "n" +  B + "(X) :- [f]).";
		
		String cl3 = "clause(" + B + "(X)" + " :- [" + "n(" + "n" + B + "(X))" + "," + "n(abn" + B + B + "(X))]).";
		
		String cl4 = "clause(" + A + "(" + objectOne + ")" + " :- " + "[t]" + ").";

		String cl5 = "clause(abn" + B + B + "(" + objectOne + ") :- [f]).";
		

		return comment + "\n" + cl1 + "\n" +  cl2 + "\n" + cl3 + "\n" + cl4 + "\n" + cl5;// + "\n" +cl42;
		
	}
}
