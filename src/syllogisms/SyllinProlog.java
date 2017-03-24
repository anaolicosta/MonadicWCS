package syllogisms;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Creat programs.
 * 
 * @author Emma
 *
 */
public class SyllinProlog {
	
	String pattern;
	
	public void createAllSyll(int patternAab, int patternIab, int patternEab, int patternOab) {
		
		pattern = patternAab + "" + patternIab + "" + patternEab + "" + patternOab;
		
		File file = null;
		FileWriter fw = null;
		BufferedWriter bw = null;

		String a = "a";
		String b = "b";
		String c = "c";
		String content;
		
		Aab forAll = new Aab();
		Iab some = new Iab();
		Eab none = new Eab();
		Oab someNot = new Oab();
		
		String[] allPrem1 = {forAll.getAll(a, b, "o1", "o2", "o5", "o6", patternAab), forAll.getAll(b, a, "o1", "o2", "o5", "o6", patternAab), 
				forAll.getAll(a, b, "o1", "o2", "o5", "o6", patternAab), forAll.getAll(b, a, "o1", "o2", "o5", "o6", patternAab)};
		String[] allPrem2 = {forAll.getAll(b, c, "o3", "o4", "o7", "o8", patternAab), forAll.getAll(c, b, "o3", "o4", "o7", "o8", patternAab), 
				forAll.getAll(c, b, "o3", "o4", "o7", "o8", patternAab), forAll.getAll(b, c, "o3", "o4", "o7", "o8", patternAab)};
		
		String[] somePrem1 = {some.getSome(a,b, "o1", "o2", "o5", "o6", patternIab), some.getSome(b,a, "o1", "o2", "o5", "o6",  patternIab), 
				some.getSome(a,b, "o1", "o2", "o5", "o6", patternIab), some.getSome(b,a,"o1", "o2", "o5", "o6", patternIab)};
		String[] somePrem2 = {some.getSome(b,c, "o3", "o4", "o7", "o8", patternIab), some.getSome(c,b, "o3", "o4", "o7", "o8", patternIab), 
				some.getSome(c,b, "o3", "o4", "o7", "o8", patternIab), some.getSome(b,c, "o3", "o4", "o7", "o8",patternIab)};
		
		String[] nonePrem1 = {none.getNone(a,b, "o1", "o2", "o5", "o6", patternEab), none.getNone(b,a, "o1", "o2", "o5", "o6", patternEab), 
				none.getNone(a,b, "o1", "o2", "o5", "o6", patternEab), none.getNone(b,a, "o1","o2", "o5", "o6", patternEab)};
		String[] nonePrem2 = {none.getNone(b,c, "o3", "o4", "o7", "o8", patternEab), none.getNone(c,b, "o3", "o4", "o7", "o8", patternEab), 
				none.getNone(c,b, "o3", "o4", "o7", "o8", patternEab), none.getNone(b,c, "o3", "o4", "o7", "o8", patternEab)};
		
		String[] someNotPrem1 = {someNot.getSomeNot(a,b, "o1", "o2", "o5", "o6", patternOab), someNot.getSomeNot(b,a, "o1", "o2", "o5", "o6", patternOab), 
				someNot.getSomeNot(a,b,"o1", "o2", "o5", "o6", patternOab), someNot.getSomeNot(b,a, "o1", "o2", "o5", "o6", patternOab)};
		String[] someNotPrem2 = {someNot.getSomeNot(b,c, "o3", "o4", "o7", "o8", patternOab), someNot.getSomeNot(c,b, "o3", "o4", "o7", "o8", patternOab), 
				someNot.getSomeNot(c,b, "o3", "o4", "o7", "o8", patternOab), someNot.getSomeNot(b,c, "o3", "o4", "o7", "o8", patternOab)};
		
		try{
			
		for(int i = 0; i <= 3; i++){
			
			// AA
			
			content = allPrem1[i] + "\n" + allPrem2[i];
			createSyll(file, fw, bw, i+1, content, "aa");

			// AI 
			
			content = allPrem1[i] + "\n" + somePrem2[i];
			createSyll(file, fw, bw, i+1, content, "ai");			

			// AE
			
			content = allPrem1[i] + "\n" + nonePrem2[i]; // + "\n" + exceptionEA;
			createSyll(file, fw, bw, i+1, content, "ae");

			// AO
			
			content = allPrem1[i] + "\n" + someNotPrem2[i];
			createSyll(file, fw, bw, i+1, content, "ao");
			
			/////////////////////////////////////////////////////
			
			// IA
			
			content = somePrem1[i] + "\n" + allPrem2[i];
			createSyll(file, fw, bw, i+1, content, "ia");

			// II 
			
			content = somePrem1[i]  + "\n" + somePrem2[i];
			createSyll(file, fw, bw, i+1, content, "ii");			

			// IE
			
			content = somePrem1[i]  + "\n" + nonePrem2[i]; // + "\n" + exceptionEA;
			createSyll(file, fw, bw, i+1, content, "ie");

			// AO
			
			content = somePrem1[i]  + "\n" + someNotPrem2[i];
			createSyll(file, fw, bw, i+1, content, "io");
			
			/////////////////////////////////////////////////////
						
			// EA
			
			content = nonePrem1[i] + "\n" + allPrem2[i]; // + "\n" + exceptionEA;
			createSyll(file, fw, bw, i+1, content, "ea");
			
			// EI 
			
			content = nonePrem1[i]  + "\n" + somePrem2[i]; // + "\n" + exceptionEA;
			createSyll(file, fw, bw, i+1, content, "ei");			
			
			// EE
			
			content = nonePrem1[i]  + "\n" + nonePrem2[i]; // + "\n" + exception;
			createSyll(file, fw, bw, i+1, content, "ee");
			
			// EO
			
			content = nonePrem1[i]  + "\n" + someNotPrem2[i]; // + "\n" + exceptionEA;
			createSyll(file, fw, bw, i+1, content, "eo");
			
			/////////////////////////////////////////////////////
						
			// OA
			
			content = someNotPrem1[i] + "\n" + allPrem2[i];
			createSyll(file, fw, bw, i+1, content, "oa");
			
			// OI 
			
			content = someNotPrem1[i]  + "\n" + somePrem2[i];
			createSyll(file, fw, bw, i+1, content, "oi");			
			
			// OE
			
			content = someNotPrem1[i]  + "\n" + nonePrem2[i]; // + "\n" + exceptionEA;
			createSyll(file, fw, bw, i+1, content, "oe");
			
			// OO
			
			content = someNotPrem1[i]  + "\n" + someNotPrem2[i];
			createSyll(file, fw, bw, i+1, content, "oo");
				
			}
		
		} catch (IOException e) {e.printStackTrace();}
		

	}
	
	public void createSyll(File file, FileWriter fw, BufferedWriter bw, int nr, String content, String mood) throws IOException {
		
		File dir = new File("syllogisms/" + pattern +"");
		dir.mkdir();
	
		file = new File("syllogisms/" + pattern + "/" + mood + nr + ".pl");
		fw = new FileWriter(file);
		bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
		
	}
}
