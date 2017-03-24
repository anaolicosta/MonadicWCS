package syllogisms;

/**
 * Some are z.
 * @author Emma
 *
 */
public class Iab {
	
	public static int nrOfPatterns = 2;


	public String getSome(String a, String b, String objectOne, String objectTwo, String objectThree, String objectFour, int patternIab){

		if(patternIab == 1){
			return thesisVersionSome(a,b, objectOne, objectTwo);
			}
		if(patternIab == 2){
			return thesisVersionSome(a,b, objectOne, objectTwo) + "\n" + 
		thesisVersionSome(b,a, objectThree, objectFour);
			}
		return null;
	}

	private String thesisVersionSome(String A, String B, String objectOne, String objectTwo){


		String comment = "% Some " + A + " are " + B + ".";
		
		String cl1 = "clause(" + B + "(X)" + " :- [" + A + "(X)" + "," + "n(ab" + A + B + "(X)" + ")]).";

		String cl2 = "clause(" +"ab" + A + B + "(" + objectOne + ")" + " :- " + "[f]" + ").";
				
		String cl3 = "clause(" + A + "(" + objectOne + ")" + " :- " + "[t]" + ").";
		
		String cl4 = "clause(" + A + "(" + objectTwo + ")" + " :- " + "[t]" + ").";
		
		
		return comment + "\n" +  cl1 + "\n" + cl2 + "\n" + cl3 +  "\n" + cl4; 
	}
}
