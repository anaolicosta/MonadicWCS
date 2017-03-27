package main;

import java.io.File;
import java.util.ArrayList;

/**
 * 
 * @author Emma
 *
 */
public class PrologConnector{
	
	public void runProlog(String pattern) throws Exception{
			  
	    String[] syll = {
	    		"aa1", "aa2", "aa3", "aa4", 
	    		"ae1", "ae2", "ae3", "ae4",
	    		"ai1", "ai2", "ai3", "ai4",
	    		"ao1", "ao2", "ao3", "ao4",
	    		
	    		"ea1", "ea2", "ea3", "ea4",
	    		"ee1", "ee2", "ee3", "ee4",
	    		"ei1", "ei2", "ei3", "ei4",
	    		"eo1", "eo2", "eo3", "eo4",

	    		"ia1", "ia2", "ia3", "ia4",
	    		"ie1", "ie2", "ie3", "ie4",
	    		"ii1", "ii2", "ii3", "ii4",
	    		"io1", "io2", "io3", "io4",

	    		"oa1", "oa2", "oa3", "oa4",
	    		"oe1", "oe2", "oe3", "oe4",
	    		"oi1", "oi2", "oi3", "oi4",
	    		"oo1", "oo2", "oo3", "oo4",
	    }; 
	    
	      	
    	for(int j =0; j < syll.length; j++){
			ProcessBuilder pb = new ProcessBuilder("resources/run.sh", pattern + "/" + syll[j]);
			Process process = pb.start();
			process.waitFor();
    	}
	    
	 }
	
}