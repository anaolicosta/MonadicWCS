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
		
		String cl1 = "clause(" + "n" + A  + "(X)" + " :- [" + B + "(X)" + "," + "n(ab" + B + "n" +  A + "(X))]" + ")" + ".";
		
		String cl2 = "clause(ab" + B + "n" +  A + "(X) :- [f]).";
		
		String cl3 = "clause(" + A + "(X)" + " :- [" + "n(" + "n" + A + "(X))" + "," + "n(abn" + A + A + "(X))]).";
		
		String cl4 = "clause(" + B + "(" + objectOne + ")" + " :- " + "[t]" + ").";

		String cl5 = "clause(abn" + A + A + "(" + objectOne + ") :- [f]).";
		

		return comment + "\n" + cl1 + "\n" +  cl2 + "\n" + cl3 + "\n" + cl4 + "\n" + cl5;// + "\n" +cl42;
		
	}
}
