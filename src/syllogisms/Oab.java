package syllogisms;

/**
 * Some y are not z.
 * @author Emma
 *
 */
public class Oab {
	
	public static int nrOfPatterns = 2;

	public String getSomeNot(String a, String b, String objectOne, String objectTwo, String objectThree, String objectFour,
			int patternOab) {

		if (patternOab == 1) {
			return thesisVersionSomeNot(a, b, objectOne, objectTwo);
		}
		if (patternOab == 2) {
			return thesisVersionSomeNot(a, b, objectOne, objectTwo) + "\n" +
					thesisVersionSomeNot(b, a, objectThree, objectFour);
		} 
		return null;
	}

	private String thesisVersionSomeNot(String A, String B, String objectOne,
			String objectTwo) {

		String comment = "% Some " + A + " are not " + B + ".";

		String cl1 = "clause(" + "n" + B + "(X)" + " :- [" + A + "(X)" + "," + "n(ab" + A + "n" + B + "(X))]" + ").";

		String cl2 = "clause(" + "ab" + A + "n" + B + "(" + objectOne + ")"	+ " :- " + "[f]" + ").";

		String cl3 = "clause(" + B + "(X)" + " :- [" + "n(" + "n" + B + "(X))"  + "," +  "n(ab" + "n" + B + B + "(X)" + ")]).";

		String cl4 = "clause(" + A + "(" + objectOne + ")" + " :- " + "[t]"
				+ ").";

		String cl5 = "clause(" + A + "(" + objectTwo + ")" + " :- " + "[t]"	+ ").";

		String cl6 = "clause(" + "abn" + B + B + "(" + objectOne + ")" + " :- "	+ "[f]" + ").";

		String cl7 = "clause(" + "abn" + B + B + "(" + objectTwo + ")" + " :- " 	+ "[f]" + ").";

		return comment + "\n" + cl1 + "\n" + cl2 + "\n" + cl3 + "\n" + cl4
				+ "\n" + cl5 + "\n" + cl6 + "\n" + cl7;
	}
}
