package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurableProperties {
	
	InputStream inputStream;
 
	private String getPropertyValue(final String key) {
		
		String result = "";
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
 
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("Property file '" + propFileName + "' not found in the classpath");
			}
 
			result = prop.getProperty(key);
			
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				System.out.println("Problem to retrive configuration for " + key +": "+ e.getMessage() );
			}
		}
		return result;
	}
	
	public String getPathValue() {
		return getPropertyValue("path");
	}
	
	public String[] getPatterns() {
		String patterns = getPropertyValue("patterns");
		
		return patterns.split(",");
	}
	
	public String[] getOutputType() {
		String patterns = getPropertyValue("output");
		
		return patterns.split(",");
	}
	
	public String getStrategiesBlocks() {
		return getPropertyValue("strategiesBlocks");
	}
	
	public String getStrategiesExperiments() {
		return getPropertyValue("strategiesExperiments");
	}
	
	public String getStrategiesAbducibles() {
		return getPropertyValue("strategiesAbducibles");
	}
	
	public String getEntailment()  {
		return getPropertyValue("entailment");
	}
	
	public String getObservationsCriteria() {
		return getPropertyValue("observations");
	}
	
	public String getMinimalityCriteria() {
		return getPropertyValue("minimality");
	}
}